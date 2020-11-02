package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.exceptions.DataNotFoundException;
import com.github.egoettelmann.sample.banking.api.core.exceptions.payment.PaymentCannotBeDeletedException;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DeletePaymentServiceTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private BalanceService balanceService;

    @SpyBean
    private SqlPaymentRepositoryService sqlPaymentRepositoryService;

    @Test
    public void whenDeleteExecutedPayment_throwsCannotBeDeleted() {
        AppUser appUser = buildUser(1L);

        Assertions.assertThrows(PaymentCannotBeDeletedException.class, () -> {
            paymentService.deletePayment(appUser, 1L);
        });
    }

    @Test
    public void whenDeletePayment_balanceShouldBeCorrect() {
        AppUser appUser = buildUser(1L);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(800.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setGiverAccountId(1L);
        paymentRequest.setBeneficiaryAccountNumber("LU120010001234567892");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertDoesNotThrow(() -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
            paymentService.deletePayment(appUser, payment.getId());
        });

        List<Balance> balances = balanceService.getBalancesForUserAndAccount(appUser, 1L);
        Assertions.assertEquals(2, balances.size(), "Wrong size of balances");
        Optional<Balance> availableBalance = balances.stream()
                .filter(b -> BalanceStatus.AVAILABLE.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(availableBalance.isPresent(), "No available balance found");
        Optional<Balance> endOfDayBalance = balances.stream()
                .filter(b -> BalanceStatus.END_OF_DAY.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(endOfDayBalance.isPresent(), "No endOfDay balance found");
        Assertions.assertEquals(0, BigDecimal.valueOf(1000.00).compareTo(endOfDayBalance.get().getAmount()), "Wrong endOfDay balance amount");
    }

    @Test
    public void whenDeleteInternalPayment_balanceOfBothAccountsShouldBeCorrect() {
        AppUser appUser = buildUser(1L);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(70.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setGiverAccountId(1L);
        paymentRequest.setBeneficiaryAccountNumber("LU220010001234567891");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Assertions.assertDoesNotThrow(() -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
            paymentService.deletePayment(appUser, payment.getId());
        });

        List<Balance> balances = balanceService.getBalancesForUserAndAccount(appUser, 1L);
        Assertions.assertEquals(2, balances.size(), "Wrong size of balances");
        Optional<Balance> availableBalance = balances.stream()
                .filter(b -> BalanceStatus.AVAILABLE.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(availableBalance.isPresent(), "No available balance found");
        Optional<Balance> endOfDayBalance = balances.stream()
                .filter(b -> BalanceStatus.END_OF_DAY.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(endOfDayBalance.isPresent(), "No endOfDay balance found");
        Assertions.assertEquals(0, BigDecimal.valueOf(1000.00).compareTo(endOfDayBalance.get().getAmount()), "Wrong endOfDay balance amount");

        AppUser beneficiary = buildUser(2L);
        List<Balance> beneficiaryBalances = balanceService.getBalancesForUserAndAccount(beneficiary, 3L);

        Assertions.assertEquals(2, beneficiaryBalances.size(), "Wrong size of beneficiary balances");
        Optional<Balance> availableBeneficiaryBalance = beneficiaryBalances.stream()
                .filter(b -> BalanceStatus.AVAILABLE.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(availableBeneficiaryBalance.isPresent(), "No available beneficiary balance found");
        Optional<Balance> endOfDayBeneficiaryBalance = beneficiaryBalances.stream()
                .filter(b -> BalanceStatus.END_OF_DAY.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(endOfDayBeneficiaryBalance.isPresent(), "No endOfDay beneficiary balance found");
        Assertions.assertEquals(0, BigDecimal.valueOf(1000.00).compareTo(endOfDayBeneficiaryBalance.get().getAmount()), "Wrong endOfDay beneficiary balance amount");
    }

    @Test
    public void whenPaymentDeleteFails_rollbackBalances() {
        AppUser appUser = buildUser(1L);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(BigDecimal.valueOf(300.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setGiverAccountId(1L);
        paymentRequest.setBeneficiaryAccountNumber("LU220010001234567891");
        paymentRequest.setBeneficiaryName("Test Beneficiary");

        Mockito.doThrow(new DataIntegrityViolationException("Delete failed")).when(sqlPaymentRepositoryService).deletePayment(Mockito.any());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            Payment payment = paymentService.createPayment(appUser, paymentRequest);
            paymentService.deletePayment(appUser, payment.getId());
        });

        List<Balance> balances = balanceService.getBalancesForUserAndAccount(appUser, 1L);
        Assertions.assertEquals(2, balances.size(), "Wrong size of balances");
        Optional<Balance> availableBalance = balances.stream()
                .filter(b -> BalanceStatus.AVAILABLE.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(availableBalance.isPresent(), "No available balance found");
        Optional<Balance> endOfDayBalance = balances.stream()
                .filter(b -> BalanceStatus.END_OF_DAY.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(endOfDayBalance.isPresent(), "No endOfDay balance found");
        Assertions.assertEquals(0, BigDecimal.valueOf(700.00).compareTo(endOfDayBalance.get().getAmount()), "Wrong endOfDay balance amount");

        AppUser beneficiary = buildUser(2L);
        List<Balance> beneficiaryBalances = balanceService.getBalancesForUserAndAccount(beneficiary, 3L);

        Assertions.assertEquals(2, beneficiaryBalances.size(), "Wrong size of beneficiary balances");
        Optional<Balance> availableBeneficiaryBalance = beneficiaryBalances.stream()
                .filter(b -> BalanceStatus.AVAILABLE.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(availableBeneficiaryBalance.isPresent(), "No available beneficiary balance found");
        Optional<Balance> endOfDayBeneficiaryBalance = beneficiaryBalances.stream()
                .filter(b -> BalanceStatus.END_OF_DAY.equals(b.getStatus()))
                .findFirst();
        Assertions.assertTrue(endOfDayBeneficiaryBalance.isPresent(), "No endOfDay beneficiary balance found");
        Assertions.assertEquals(0, BigDecimal.valueOf(1300.00).compareTo(endOfDayBeneficiaryBalance.get().getAmount()), "Wrong endOfDay beneficiary balance amount");
    }

    private AppUser buildUser(Long userId) {
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        appUser.setUsername("Test User");
        return appUser;
    }

}
