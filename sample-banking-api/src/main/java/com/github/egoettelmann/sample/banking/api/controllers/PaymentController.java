package com.github.egoettelmann.sample.banking.api.controllers;

import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentFilter;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import jakarta.validation.Valid;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @PageableAsQueryParam
    @PreAuthorize("hasAuthority(T(com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority).PAYMENTS_VIEW.name())")
    public Page<Payment> findAllPayments(
            PaymentFilter filter,
            @PageableDefault(sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal AppUser appUser
    ) {
        return paymentService.searchPayments(appUser, filter, pageable);
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.github.egoettelmann.sample.banking.api.core.dtos.AppAuthority).PAYMENTS_CREATE.name())")
    public Payment createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest,
            @AuthenticationPrincipal AppUser appUser
    ) {
        return paymentService.createPayment(appUser, paymentRequest);
    }

}
