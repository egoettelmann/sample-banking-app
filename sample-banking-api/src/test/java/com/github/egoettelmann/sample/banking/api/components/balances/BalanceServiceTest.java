package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.BalanceService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BalanceServiceTest {

    @Autowired
    private BalanceService balanceService;

    @SpyBean
    private SqlBalanceRepositoryService balanceRepositoryService;

    @Test
    public void whenRegisterTransactionFails_rollbackBalances() {
        AppUser appUser = buildUser("user1@test.com");

        Mockito.doCallRealMethod()
                .doThrow(new DataIntegrityViolationException("Save failed"))
                .when(balanceRepositoryService)
                .save(Mockito.any());
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            balanceService.registerTransaction(appUser, "LU510011111111111111", "LU640013333333333333", BigDecimal.valueOf(300.00));
        });

        Optional<Balance> balance1 = balanceService.getCurrentBalance(appUser, "LU510011111111111111");
        Assertions.assertTrue(balance1.isPresent(), "No current balance found");
        Assertions.assertEquals(BalanceStatus.VALIDATED, balance1.get().getStatus(), "Wrong balance status");
        Assertions.assertEquals(0, BigDecimal.valueOf(1000).compareTo(balance1.get().getValue()), "Wrong current balance amount");

        AppUser beneficiary = buildUser("user2@test.com");

        Optional<Balance> balance2 = balanceService.getCurrentBalance(beneficiary, "LU640013333333333333");
        Assertions.assertTrue(balance2.isPresent(), "No current balance found");
        Assertions.assertEquals(BalanceStatus.VALIDATED, balance2.get().getStatus(), "Wrong balance status");
        Assertions.assertEquals(0, BigDecimal.valueOf(1000).compareTo(balance2.get().getValue()), "Wrong current balance amount");
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
