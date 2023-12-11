package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.InvalidIbanException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentFilter;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CreatePaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BalanceService balanceService;

    @Test
    public void whenRetrievingPayments_sizeShouldMatch() {
        AppUser appUser = buildUser("user1@test.com");
        final PaymentFilter filter = new PaymentFilter();
        filter.setOriginAccountNumber("LU510011111111111111");
        Page<Payment> payments = paymentService.searchPayments(appUser, filter, Pageable.unpaged());

        Assertions.assertEquals(1, payments.getTotalElements(), "Wrong number of payments");
    }

    @Test
    public void whenCreateValidPayment_referenceShouldNotBeNullAndSizeShouldMatch() {
        AppUser appUser = buildUser("user1@test.com");

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(834.56));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("LU510011111111111111");
        paymentRequest.setBeneficiaryAccountNumber("LU090012222222222222");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Payment payment = paymentService.createPayment(appUser, paymentRequest);

        Assertions.assertNotNull(payment.getReference(), "Payment reference should not be null");

        final PaymentFilter filter = new PaymentFilter();
        filter.setOriginAccountNumber("LU510011111111111111");
        Page<Payment> payments = paymentService.searchPayments(appUser, filter, Pageable.unpaged());
        Assertions.assertEquals(2, payments.getTotalElements(), "Wrong number of payments");
    }

    @Test
    public void whenCreatePaymentWithWrongAccount_throwsDataNotFound() {
        AppUser appUser = buildUser("user2@test.com");

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(834.56));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("LU510011111111111111");
        paymentRequest.setBeneficiaryAccountNumber("LU090012222222222222");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertThrows(DataNotFoundException.class, () -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
        });
    }

    @Test
    public void whenCreateValidPayment_balanceShouldBeCorrect() {
        AppUser appUser = buildUser("user1@test.com");

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(800.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("LU510011111111111111");
        paymentRequest.setBeneficiaryAccountNumber("LU090012222222222222");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertDoesNotThrow(() -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
        });

        Optional<Balance> balance = balanceService.getCurrentBalance(appUser, "LU510011111111111111");
        Assertions.assertTrue(balance.isPresent(), "No current balance found");
        Assertions.assertEquals(BalanceStatus.PROVISIONAL, balance.get().getStatus(), "Wrong balance status");
        Assertions.assertEquals(0, BigDecimal.valueOf(200.00).compareTo(balance.get().getValue()), "Wrong current balance amount");
    }

    @Test
    public void whenCreateValidInternalPayment_balanceOfBothAccountsShouldBeCorrect() {
        AppUser appUser = buildUser("user1@test.com");

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(70.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("LU510011111111111111");
        paymentRequest.setBeneficiaryAccountNumber("LU640013333333333333");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertDoesNotThrow(() -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
        });

        Optional<Balance> balance1 = balanceService.getCurrentBalance(appUser, "LU510011111111111111");
        Assertions.assertTrue(balance1.isPresent(), "No current balance found");
        Assertions.assertEquals(BalanceStatus.PROVISIONAL, balance1.get().getStatus(), "Wrong balance status");
        Assertions.assertEquals(0, BigDecimal.valueOf(930).compareTo(balance1.get().getValue()), "Wrong current balance amount");

        AppUser beneficiary = buildUser("user2@test.com");

        Optional<Balance> balance2 = balanceService.getCurrentBalance(beneficiary, "LU640013333333333333");
        Assertions.assertTrue(balance2.isPresent(), "No current balance found");
        Assertions.assertEquals(BalanceStatus.PROVISIONAL, balance2.get().getStatus(), "Wrong balance status");
        Assertions.assertEquals(0, BigDecimal.valueOf(1070).compareTo(balance2.get().getValue()), "Wrong current balance amount");
    }

    @Test
    public void whenCreatePaymentWithInvalidIban_throwsInvalidIban() {
        AppUser appUser = buildUser("user1@test.com");

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(834.56));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setOriginAccountNumber("LU510011111111111111");
        paymentRequest.setBeneficiaryAccountNumber("INVALID_IBAN");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertThrows(InvalidIbanException.class, () -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
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

}
