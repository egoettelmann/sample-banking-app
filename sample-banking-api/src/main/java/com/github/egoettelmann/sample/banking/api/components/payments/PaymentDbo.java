package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.dtos.PaymentStatus;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@FieldNameConstants
@Entity
@Table(name = "PAYMENT")
class PaymentDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "AMOUNT")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "ORIGIN_ACCOUNT_NUMBER")
    private String originAccountNumber;

    @Column(name = "BENEFICIARY_ACCOUNT_NUMBER")
    private String beneficiaryAccountNumber;

    @Column(name = "BENEFICIARY_NAME")
    private String beneficiaryName;

    @Column(name = "COMMUNICATION")
    private String communication;

    @Column(name = "CREATION_DATE")
    private ZonedDateTime creationDate;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
