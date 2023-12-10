package com.github.egoettelmann.sample.banking.api.components.balances;

import com.github.egoettelmann.sample.banking.api.core.dtos.Balance;
import com.github.egoettelmann.sample.banking.api.core.requests.BalanceFilter;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class SqlBalanceRepositoryService {

    private final BalanceRepository balanceRepository;

    private final BalanceMapper balanceMapper;

    @Autowired
    public SqlBalanceRepositoryService(
            BalanceRepository balanceRepository,
            BalanceMapper balanceMapper
    ) {
        this.balanceRepository = balanceRepository;
        this.balanceMapper = balanceMapper;
    }

    public Page<Balance> findAll(BalanceFilter filter, Pageable pageable) {
        return balanceRepository.findAll(
                balanceSpecifications(filter),
                pageable
        ).map(balanceMapper::to);
    }

    public Optional<Balance> findOne(BalanceFilter filter) {
        return balanceRepository.findOne(
                balanceSpecifications(filter)
        ).map(balanceMapper::to);
    }

    public Balance save(Balance balance) {
        final BalanceFilter filter = BalanceFilter.builder()
                .accountNumber(balance.getAccountNumber())
                .valueDate(balance.getValueDate())
                .build();
        final Optional<BalanceDbo> existing = balanceRepository.findOne(
                balanceSpecifications(filter)
        );
        BalanceDbo dbo = existing.orElse(new BalanceDbo());
        dbo = balanceRepository.save(
                balanceMapper.from(balance, dbo)
        );
        return balanceMapper.to(dbo);
    }

    private Specification<BalanceDbo> balanceSpecifications(BalanceFilter filter) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final List<Predicate> predicates = new ArrayList<>();

            // Account number
            if (StringUtils.isNotBlank(filter.getAccountNumber())) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BalanceDbo.Fields.accountNumber),
                                filter.getAccountNumber()
                        )
                );
            }

            // Value Date
            if (filter.getValueDate() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BalanceDbo.Fields.valueDate),
                                filter.getValueDate()
                        )
                );
            } else {
                final Subquery<LocalDate> subQuery = criteriaQuery.subquery(LocalDate.class);
                final Root<BalanceDbo> root2 = subQuery.from(BalanceDbo.class);
                subQuery.select(criteriaBuilder.greatest(root2.get(BalanceDbo.Fields.valueDate)));
                subQuery.where(
                        criteriaBuilder.equal(
                                root2.get(BalanceDbo.Fields.accountNumber),
                                root.get(BalanceDbo.Fields.accountNumber)
                        )
                );
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BalanceDbo.Fields.valueDate),
                                subQuery
                        )
                );
            }

            // Status
            if (filter.getStatus() != null) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get(BalanceDbo.Fields.status),
                                filter.getStatus()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
