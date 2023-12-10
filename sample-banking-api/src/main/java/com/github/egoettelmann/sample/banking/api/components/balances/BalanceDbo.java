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
@Table(name = "balance")
class BalanceDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "value")
    private BigDecimal value;

    @Column(name = "valueDate")
    private LocalDate valueDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BalanceStatus status;

}
