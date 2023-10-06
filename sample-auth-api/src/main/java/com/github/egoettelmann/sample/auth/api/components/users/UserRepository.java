package com.github.egoettelmann.sample.auth.api.components.users;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends PagingAndSortingRepository<UserDbo, Long> {

    Optional<UserDbo> findByUsername(String username);

}
