package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
interface BalanceMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"id"})
    Balance to(BalanceDbo dbo);

    @Mapping(target = "id", ignore = true)
    BalanceDbo from(Balance dto, @MappingTarget BalanceDbo dbo);

}
