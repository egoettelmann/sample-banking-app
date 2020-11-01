package com.github.egoettelmann.sample.banking.api.components.validation;

import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultPaymentValidationService implements PaymentValidationService {

    @Override
    public void validateInternalPayment(Payment payment) {
        validatePayment(payment);
    }

    @Override
    public void validateExternalPayment(Payment payment) {
        validatePayment(payment);

        // Checking valid IBAN
        // TODO: call remote web service
    }

    private void validatePayment(Payment payment) {
        // Null checks
        if (payment == null || payment.getGiverAccount() == null) {
            throw new InvalidPaymentException("Payment or giverAccount cannot be empty");
        }
        if (payment.getBeneficiaryAccountNumber() == null) {
            throw new InvalidPaymentException("Beneficiary account cannot be null");
        }

        // Checking that amount is correct
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Incorrect amount");
        }

        // Checking that the account number is not the same
        if (payment.getBeneficiaryAccountNumber().equals(payment.getGiverAccount().getAccountNumber())) {
            throw new InvalidPaymentException("Payments to the same account number are not valid");
        }

        // Checking that balance allows payment
        // TODO: get latest balance from DB and compare amount

        // Checking for forbidden accounts
        // TODO: check if account number is in database of forbidden accounts
    }

}
