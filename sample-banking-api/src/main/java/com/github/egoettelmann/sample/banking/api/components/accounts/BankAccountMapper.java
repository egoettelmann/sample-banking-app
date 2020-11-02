package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccount to(BankAccountDbo dbo);

    BankAccountDbo from(BankAccount dto);

}
