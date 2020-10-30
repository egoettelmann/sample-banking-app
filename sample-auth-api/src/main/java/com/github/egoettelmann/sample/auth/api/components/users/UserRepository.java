package com.github.egoettelmann.sample.auth.api.components.users;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository extends PagingAndSortingRepository<UserDbo, Long> {

    UserDbo findByUsername(String username);

}
