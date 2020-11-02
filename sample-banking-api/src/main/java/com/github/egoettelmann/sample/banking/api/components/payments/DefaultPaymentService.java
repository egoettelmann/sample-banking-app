package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
class DefaultPaymentService implements PaymentService {

    private final SqlPaymentRepositoryService sqlPaymentRepositoryService;

    private final BankAccountService bankAccountService;

    private final PaymentValidationService paymentValidationService;

    private final BalanceService balanceService;

    @Autowired
    public DefaultPaymentService(
            SqlPaymentRepositoryService sqlPaymentRepositoryService,
            BankAccountService bankAccountService,
            PaymentValidationService paymentValidationService,
            BalanceService balanceService
    ) {
        this.sqlPaymentRepositoryService = sqlPaymentRepositoryService;
        this.bankAccountService = bankAccountService;
        this.paymentValidationService = paymentValidationService;
        this.balanceService = balanceService;
    }

    @Override
    public Page<Payment> getPaymentsForUser(AppUser user, Pageable pageable) {
        return sqlPaymentRepositoryService.getPaymentsForUserId(user.getId(), pageable);
    }

    @Transactional
    @Override
    public Payment createPayment(AppUser user, PaymentRequest paymentRequest) {
        // Retrieving giver account
        Optional<BankAccount> giverBankAccount = bankAccountService.getBankAccountForUserById(user, paymentRequest.getGiverAccountId());

        // Checking that current user is authorized
        if (!giverBankAccount.isPresent()) {
            String message = String.format("BankAccount with id='%s' not found for userId='%s'", paymentRequest.getGiverAccountId(), user.getId());
            throw new DataNotFoundException(message);
        }

        // Creating payment
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setGiverAccount(giverBankAccount.get());
        payment.setBeneficiaryAccountNumber(paymentRequest.getBeneficiaryAccountNumber());
        payment.setBeneficiaryName(paymentRequest.getBeneficiaryName());
        payment.setCommunication(paymentRequest.getCommunication());
        payment.setCreationDate(ZonedDateTime.now());

        // Differentiating between internal and external payment
        Optional<BankAccount> beneficiaryBankAccount = bankAccountService.getBankAccountByAccountNumber(paymentRequest.getBeneficiaryAccountNumber());
        if (beneficiaryBankAccount.isPresent()) {
            paymentValidationService.validateInternalPayment(payment);
            balanceService.subtractAmountFromBalance(payment.getAmount(), giverBankAccount.get());
            balanceService.addAmountToBalance(payment.getAmount(), beneficiaryBankAccount.get());
        } else {
            paymentValidationService.validateExternalPayment(payment);
            balanceService.subtractAmountFromBalance(payment.getAmount(), giverBankAccount.get());
        }

        // Saving
        return sqlPaymentRepositoryService.savePayment(payment);
    }

    @Transactional
    @Override
    public void deletePayment(AppUser user, Long paymentId) {
        // Retrieving the payment (and checking that it belongs to the user)
        Payment payment = sqlPaymentRepositoryService.getPaymentForUserId(user.getId(), paymentId);
        if (payment == null) {
            String message = String.format("Payment with id='%s' not found for userId='%s'", paymentId, user.getId());
            throw new DataNotFoundException(message);
        }

        // Validating that it can be deleted
        paymentValidationService.validatePaymentDeletion(payment);

        // Differentiating between internal and external payment
        Optional<BankAccount> beneficiaryBankAccount = bankAccountService.getBankAccountByAccountNumber(payment.getBeneficiaryAccountNumber());
        if (beneficiaryBankAccount.isPresent()) {
            balanceService.addAmountToBalance(payment.getAmount(), payment.getGiverAccount());
            balanceService.subtractAmountFromBalance(payment.getAmount(), beneficiaryBankAccount.get());
        } else {
            balanceService.addAmountToBalance(payment.getAmount(), payment.getGiverAccount());
        }

        // Saving
        sqlPaymentRepositoryService.deletePayment(payment);
    }

}
