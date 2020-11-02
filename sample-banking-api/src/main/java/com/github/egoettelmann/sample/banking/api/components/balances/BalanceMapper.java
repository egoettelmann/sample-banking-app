package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface BalanceMapper {

    Balance to(BalanceDbo dbo);

    List<Balance> to(List<BalanceDbo> dbo);

    BalanceDbo from(Balance dto);

}
