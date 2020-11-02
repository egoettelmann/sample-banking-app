package com.github.egoettelmann.sample.banking.api.components.validation;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.ForbiddenIbanException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentBeneficiarySameAsGiverAccountException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentExceedsAccountBalanceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentValidationServiceTest {

    private final BalanceService balanceService = Mockito.mock(BalanceService.class);
    private final ForbiddenIbanRepository forbiddenIbanRepository = Mockito.mock(ForbiddenIbanRepository.class);
    private final RestIbanService restIbanService = Mockito.mock(RestIbanService.class);

    private PaymentValidationService paymentValidationService;

    @BeforeEach
    public void init() {
        paymentValidationService = new DefaultPaymentValidationService(
                balanceService,
                forbiddenIbanRepository,
                restIbanService
        );
    }

    @Test
    public void whenValidPayment_noException() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        Payment payment = buildPayment(BigDecimal.valueOf(834.56));

        Assertions.assertDoesNotThrow(() -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Assertions.assertDoesNotThrow(() -> {
            paymentValidationService.validateInternalPayment(payment);
        });
    }

    @Test
    public void whenExternalPayment_restServiceCalled() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        Payment payment = buildPayment(BigDecimal.valueOf(834.56));

        Assertions.assertDoesNotThrow(() -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Mockito.verify(restIbanService, Mockito.times(1)).validate(Mockito.eq(payment.getBeneficiaryAccountNumber()));
    }

    @Test
    public void whenPaymentWithNegativeAmount_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        Payment payment = buildPayment(BigDecimal.valueOf(-43.00));

        Assertions.assertThrows(InvalidPaymentException.class, () -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Assertions.assertThrows(InvalidPaymentException.class, () -> {
            paymentValidationService.validateInternalPayment(payment);
        });
    }

    @Test
    public void whenPaymentToSameAccount_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        Payment payment = buildPayment(BigDecimal.valueOf(43.00));
        payment.setBeneficiaryAccountNumber(payment.getGiverAccount().getAccountNumber());

        Assertions.assertThrows(PaymentBeneficiarySameAsGiverAccountException.class, () -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Assertions.assertThrows(PaymentBeneficiarySameAsGiverAccountException.class, () -> {
            paymentValidationService.validateInternalPayment(payment);
        });
    }

    @Test
    public void whenPaymentExceedsAvailableBalance_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        Payment payment = buildPayment(BigDecimal.valueOf(1234.56));

        Assertions.assertThrows(PaymentExceedsAccountBalanceException.class, () -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Assertions.assertThrows(PaymentExceedsAccountBalanceException.class, () -> {
            paymentValidationService.validateInternalPayment(payment);
        });
    }

    @Test
    public void whenForbiddenIban_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setAmount(BigDecimal.valueOf(1000));
        balance.setCurrency("EUR");
        Mockito.when(balanceService.getEndOfDayBalanceForAccount(Mockito.anyLong())).thenReturn(balance);
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(true);

        Payment payment = buildPayment(BigDecimal.valueOf(834.56));

        Assertions.assertThrows(ForbiddenIbanException.class, () -> {
            paymentValidationService.validateExternalPayment(payment);
        });

        Assertions.assertThrows(ForbiddenIbanException.class, () -> {
            paymentValidationService.validateInternalPayment(payment);
        });
    }

    private Payment buildPayment(BigDecimal amount) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency("EUR");
        BankAccount bankAccount = new BankAccount();
        bankAccount.setId(1234L);
        bankAccount.setAccountName("Giver Name");
        bankAccount.setAccountNumber("Giver Account Number");
        payment.setGiverAccount(bankAccount);
        payment.setBeneficiaryName("Beneficiary Name");
        payment.setBeneficiaryAccountNumber("Beneficiary Account Number");
        payment.setCreationDate(ZonedDateTime.now());
        return payment;
    }
}
