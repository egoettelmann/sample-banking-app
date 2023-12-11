package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.BankAccountService;
import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.dtos.PaymentStatus;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentFilter;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

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
    public Page<Payment> searchPayments(AppUser user, PaymentFilter filter, Pageable pageable) {
        if (StringUtils.isBlank(filter.getOriginAccountNumber())) {
            throw new DataNotFoundException("Specifying originAccountNumber for payments is mandatory");
        }
        // Checking user allowed to retrieve payments of account
        final Optional<BankAccount> bankAccount = this.bankAccountService.getAccount(user, filter.getOriginAccountNumber());
        if (bankAccount.isEmpty()) {
            throw new DataNotFoundException("No bank account found for originAccountNumber " + filter.getOriginAccountNumber());
        }

        return sqlPaymentRepositoryService.findAll(filter, pageable);
    }

    @Override
    public Payment createPayment(AppUser user, PaymentRequest paymentRequest) {
        // Retrieving origin account
        final Optional<BankAccount> originAccount = bankAccountService.getAccount(user, paymentRequest.getOriginAccountNumber());

        // Checking that current user is authorized
        if (originAccount.isEmpty()) {
            throw new DataNotFoundException("No bank account found for originAccountNumber " + paymentRequest.getOriginAccountNumber());
        }

        // Validating payment request
        paymentValidationService.checkPaymentCreation(user, paymentRequest);

        // Creating payment
        Payment payment = new Payment();
        payment.setReference(UUID.randomUUID().toString());
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setOriginAccountNumber(paymentRequest.getOriginAccountNumber());
        payment.setBeneficiaryAccountNumber(paymentRequest.getBeneficiaryAccountNumber());
        payment.setBeneficiaryName(paymentRequest.getBeneficiaryName());
        payment.setCommunication(paymentRequest.getCommunication());
        payment.setCreationDate(ZonedDateTime.now());
        payment.setStatus(PaymentStatus.ACCEPTED);

        // Registering transaction
        balanceService.registerTransaction(user, originAccount.get().getNumber(), payment.getBeneficiaryAccountNumber(), payment.getAmount());

        // Saving
        return sqlPaymentRepositoryService.save(payment);
    }

}
