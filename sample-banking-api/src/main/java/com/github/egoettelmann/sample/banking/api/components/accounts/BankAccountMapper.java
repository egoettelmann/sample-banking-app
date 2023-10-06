package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface BankAccountMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    BankAccount to(BankAccountDbo dbo);

}
