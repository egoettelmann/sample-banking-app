package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.dtos.PaymentStatus;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
class DefaultPaymentService implements PaymentService {

    private final SqlPaymentRepositoryService sqlPaymentRepositoryService;

    private final BankAccountService bankAccountService;

    private final PaymentValidationService paymentValidationService;

    @Autowired
    public DefaultPaymentService(
            SqlPaymentRepositoryService sqlPaymentRepositoryService,
            BankAccountService bankAccountService,
            PaymentValidationService paymentValidationService
    ) {
        this.sqlPaymentRepositoryService = sqlPaymentRepositoryService;
        this.bankAccountService = bankAccountService;
        this.paymentValidationService = paymentValidationService;
    }

    @Override
    public Page<Payment> getPaymentsForUser(AppUser user, Pageable pageable) {
        return sqlPaymentRepositoryService.getPaymentsForUserId(user.getId(), pageable);
    }

    @Override
    public Payment createPayment(AppUser user, PaymentRequest paymentRequest) {
        Optional<BankAccount> giverBankAccount = bankAccountService.getBankAccountForUserById(user, paymentRequest.getGiverAccountId());
        if (!giverBankAccount.isPresent()) {
            String message = String.format("BankAccount with id='%s' not found for userId='%s'", paymentRequest.getGiverAccountId(), user.getId());
            throw new DataNotFoundException(message);
        }

        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setGiverAccount(giverBankAccount.get());
        payment.setBeneficiaryAccountNumber(paymentRequest.getBeneficiaryAccountNumber());
        payment.setBeneficiaryName(paymentRequest.getBeneficiaryName());
        payment.setCommunication(paymentRequest.getCommunication());
        payment.setCreationDate(ZonedDateTime.now());
        payment.setStatus(PaymentStatus.EXECUTED);

        Optional<BankAccount> beneficiaryBankAccount = bankAccountService.getBankAccountByAccountNumber(paymentRequest.getBeneficiaryAccountNumber());
        if (beneficiaryBankAccount.isPresent()) {
            paymentValidationService.validateInternalPayment(payment);
            // TODO: create balance for beneficiary
        } else {
            paymentValidationService.validateExternalPayment(payment);
        }

        return sqlPaymentRepositoryService.savePayment(payment);
    }
}
