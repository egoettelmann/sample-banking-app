package com.github.egoettelmann.sample.banking.api.components.payments;

import com.github.egoettelmann.sample.banking.api.core.dtos.Payment;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface PaymentMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    Payment to(PaymentDbo dbo);

    @Mapping(target = "id", ignore = true)
    PaymentDbo from(Payment dto, @MappingTarget PaymentDbo dbo);

}
