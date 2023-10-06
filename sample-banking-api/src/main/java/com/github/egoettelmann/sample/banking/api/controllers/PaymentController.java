package com.github.egoettelmann.sample.banking.api.controllers;

import com.github.egoettelmann.sample.banking.api.core.PaymentService;
import com.github.egoettelmann.sample.banking.api.core.dtos.AppUser;
import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentFilter;
import com.github.egoettelmann.sample.banking.api.core.requests.PaymentRequest;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public Page<Payment> findAllPayments(
            PaymentFilter filter,
            @PageableDefault(sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return paymentService.searchPayments(AppUser.current(), filter, pageable);
    }

    @PostMapping
    public Payment createPayment(
            @Valid @RequestBody PaymentRequest paymentRequest
    ) {
        return paymentService.createPayment(AppUser.current(), paymentRequest);
    }

}
