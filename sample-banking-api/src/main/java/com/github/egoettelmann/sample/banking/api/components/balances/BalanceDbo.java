package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.components.accounts.BankAccountDbo;
import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "balance")
public class BalanceDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private BankAccountDbo account;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BalanceStatus status;

}
