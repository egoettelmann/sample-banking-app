package com.github.egoettelmann.sample.banking.api.components.validation;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.PaymentValidationService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.exceptions.InvalidPaymentException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.ForbiddenIbanException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentBeneficiarySameAsGiverAccountException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentExceedsAccountBalanceException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

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
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(834.56));

        Assertions.assertDoesNotThrow(() -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });
    }

    @Test
    public void whenExternalPayment_restServiceCalled() {
        Balance balance = new Balance();
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(834.56));

        Assertions.assertDoesNotThrow(() -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });

        Mockito.verify(restIbanService, Mockito.times(1)).validate(Mockito.eq(paymentRequest.getBeneficiaryAccountNumber()));
    }

    @Test
    public void whenPaymentWithNegativeAmount_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(-43.00));

        Assertions.assertThrows(InvalidPaymentException.class, () -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });
    }

    @Test
    public void whenPaymentToSameAccount_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(43.00));
        paymentRequest.setBeneficiaryAccountNumber(paymentRequest.getOriginAccountNumber());

        Assertions.assertThrows(PaymentBeneficiarySameAsGiverAccountException.class, () -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });
    }

    @Test
    public void whenPaymentExceedsAvailableBalance_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(false);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(1234.56));

        Assertions.assertThrows(PaymentExceedsAccountBalanceException.class, () -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });
    }

    @Test
    public void whenForbiddenIban_throwsInvalidPayment() {
        Balance balance = new Balance();
        balance.setValue(BigDecimal.valueOf(1000));
        Mockito.when(balanceService.getCurrentBalance(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(balance));
        Mockito.when(forbiddenIbanRepository.existsByIban(Mockito.anyString())).thenReturn(true);

        final AppUser appUser = buildUser("user1@test.com");
        final PaymentRequest paymentRequest = buildPaymentRequest(BigDecimal.valueOf(834));

        Assertions.assertThrows(ForbiddenIbanException.class, () -> {
            paymentValidationService.checkPaymentCreation(appUser, paymentRequest);
        });
    }

    private AppUser buildUser(String userName) {
        return new AppUser(
                userName,
                new HashSet<>(Arrays.asList(
                        AppAuthority.ACCOUNTS_VIEW,
                        AppAuthority.BALANCES_VIEW,
                        AppAuthority.PAYMENTS_VIEW,
                        AppAuthority.PAYMENTS_CREATE
                ))
        );
    }

    private PaymentRequest buildPaymentRequest(BigDecimal amount) {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("Origin Account Number");
        paymentRequest.setBeneficiaryName("Beneficiary Name");
        paymentRequest.setBeneficiaryAccountNumber("Beneficiary Account Number");
        return paymentRequest;
    }
}
