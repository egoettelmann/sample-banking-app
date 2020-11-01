package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.components.accounts.BankAccountDbo;
import com.github.egoettelmann.sample.banking.api.core.dtos.PaymentStatus;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "payment")
public class PaymentDbo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "giver_account_id")
    private BankAccountDbo giverAccount;

    @Column(name = "beneficiary_account_number")
    private String beneficiaryAccountNumber;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "communication")
    private String communication;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
