package com.github.egoettelmann.sample.banking.api.components.accounts;

import com.github.egoettelmann.sample.banking.api.core.dtos.BankAccount;
import com.github.egoettelmann.sample.banking.api.core.requests.BankAccountFilter;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Page<BankAccount> findAll(BankAccountFilter filter, Pageable pageable) {
        return bankAccountRepository.findAll(
                accountSpecifications(filter),
                pageable
        ).map(bankAccountMapper::to);
    }

    public Optional<BankAccount> findOne(BankAccountFilter filter) {
        return bankAccountRepository.findOne(
                accountSpecifications(filter)
        ).map(bankAccountMapper::to);
    }

    private Specification<BankAccountDbo> accountSpecifications(BankAccountFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            // Account number
            if (StringUtils.isNotBlank(filter.getAccountNumber())) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BankAccountDbo.Fields.number),
                                filter.getAccountNumber()
                        )
                );
            }

            // Owner
            if (StringUtils.isNotBlank(filter.getOwner())) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BankAccountDbo.Fields.owner),
                                filter.getOwner()
                        )
                );
            }

            // Partial account name
            if (StringUtils.isNotBlank(filter.getPartialName())) {
                predicates.add(
                        criteriaBuilder.like(
                                root.get(BankAccountDbo.Fields.name),
                                "%" + filter.getPartialName() + "%"
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
