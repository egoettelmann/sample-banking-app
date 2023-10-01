package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface BalanceMapper {

    Balance to(BalanceDbo dbo);

    @Mapping(target = "id", ignore = true)
    BalanceDbo from(Balance dto, @MappingTarget BalanceDbo dbo);

}
