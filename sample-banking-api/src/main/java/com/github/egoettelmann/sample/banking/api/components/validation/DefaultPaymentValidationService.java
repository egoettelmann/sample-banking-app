package com.github.egoettelmann.sample.banking.api.components.validation;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.ForbiddenIbanException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentBeneficiarySameAsGiverAccountException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentExceedsAccountBalanceException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DefaultPaymentValidationService implements PaymentValidationService {

    private final BalanceService balanceService;

    private final ForbiddenIbanRepository forbiddenIbanRepository;

    private final RestIbanService restIbanService;

    @Autowired
    public DefaultPaymentValidationService(
            BalanceService balanceService,
            ForbiddenIbanRepository forbiddenIbanRepository,
            RestIbanService restIbanService
    ) {
        this.balanceService = balanceService;
        this.restIbanService = restIbanService;
        this.forbiddenIbanRepository = forbiddenIbanRepository;
    }

    @Override
    public void checkPaymentCreation(AppUser user, PaymentRequest paymentRequest) {
        // Null checks
        if (paymentRequest == null || StringUtils.isBlank(paymentRequest.getOriginAccountNumber())) {
            throw new InvalidPaymentException("Payment or giverAccount cannot be empty");
        }
        if (StringUtils.isBlank(paymentRequest.getBeneficiaryAccountNumber())) {
            throw new InvalidPaymentException("Beneficiary account cannot be null");
        }

        // Checking that amount is correct
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Incorrect amount");
        }

        // Checking that the account number is not the same
        if (paymentRequest.getBeneficiaryAccountNumber().equals(paymentRequest.getOriginAccountNumber())) {
            throw new PaymentBeneficiarySameAsGiverAccountException("Payments to the same account number are not valid");
        }

        // Checking that balance allows payment
        final Optional<Balance> originAccountBalance = balanceService.getCurrentBalance(user, paymentRequest.getOriginAccountNumber());
        if (originAccountBalance.isEmpty()) {
            throw new InvalidPaymentException("Unknown origin account " + paymentRequest.getOriginAccountNumber());
        }
        if (originAccountBalance.get().getValue().compareTo(paymentRequest.getAmount()) < 0) {
            throw new PaymentExceedsAccountBalanceException("Payment exceeds available balance of account");
        }

        // Checking for forbidden accounts
        if (forbiddenIbanRepository.existsByIban(paymentRequest.getBeneficiaryAccountNumber())) {
            throw new ForbiddenIbanException("Payment to provided account number is not allowed");
        }

        // Checking valid IBAN
        restIbanService.validate(paymentRequest.getBeneficiaryAccountNumber());
    }

}
