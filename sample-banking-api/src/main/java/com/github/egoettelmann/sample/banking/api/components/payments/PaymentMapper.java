package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface PaymentMapper {

    Payment to(PaymentDbo dbo);

    PaymentDbo from(Payment dto);

}
