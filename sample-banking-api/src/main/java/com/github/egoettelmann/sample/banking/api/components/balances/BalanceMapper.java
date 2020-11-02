package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.components.accounts.BankAccountMapper;
import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BankAccountMapper.class})
public interface BalanceMapper {

    Balance to(BalanceDbo dbo);

    List<Balance> to(List<BalanceDbo> dbo);

    BalanceDbo from(Balance dto);

}
