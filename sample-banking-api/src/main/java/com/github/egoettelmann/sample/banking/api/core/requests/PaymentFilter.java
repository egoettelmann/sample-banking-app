package com.github.egoettelmann.sample.banking.api.core.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ParameterObject
public class PaymentFilter {

    private String reference;
    private String originAccountNumber;

}
