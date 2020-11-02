package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface BalanceMapper {

    Balance to(BalanceDbo dbo);

    BalanceDbo from(Balance dto);

}
