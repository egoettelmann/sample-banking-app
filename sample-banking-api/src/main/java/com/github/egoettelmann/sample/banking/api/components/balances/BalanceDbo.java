package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@FieldNameConstants
@Entity
@Table(name = "BALANCE")
class BalanceDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Column(name = "VALUE_DATE")
    private LocalDate valueDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private BalanceStatus status;

}
