package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.BalanceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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

    @Version
    @Column(name = "VERSION")
    private Long version;

}
