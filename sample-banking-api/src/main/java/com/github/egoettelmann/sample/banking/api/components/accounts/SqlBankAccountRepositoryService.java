package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class SqlBankAccountRepositoryService {

    private final BankAccountRepository bankAccountRepository;

    private final BankAccountMapper bankAccountMapper;

    @Autowired
    public SqlBankAccountRepositoryService(
            BankAccountRepository bankAccountRepository,
            BankAccountMapper bankAccountMapper
    ) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
    }

    public Page<BankAccount> getAllForUserId(Long userId, Pageable pageable) {
        return bankAccountRepository.findAllByUserId(userId, pageable).map(bankAccountMapper::to);
    }

    public BankAccount getOneForUserId(Long bankAccountId, Long userId) {
        BankAccountDbo dbo = bankAccountRepository.getByIdAndUserId(bankAccountId, userId);
        return bankAccountMapper.to(dbo);
    }

    public BankAccount getOneByAccountNumber(String bankAccountNumber) {
        BankAccountDbo dbo = bankAccountRepository.getByAccountNumber(bankAccountNumber);
        return bankAccountMapper.to(dbo);
    }

}
