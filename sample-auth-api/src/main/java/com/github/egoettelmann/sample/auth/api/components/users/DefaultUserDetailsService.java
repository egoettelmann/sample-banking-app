package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
class DefaultUserDetailsService implements UserDetailsService {

    private final SqlUserRepositoryService sqlUserRepositoryService;

    @Autowired
    public DefaultUserDetailsService(SqlUserRepositoryService sqlUserRepositoryService) {
        this.sqlUserRepositoryService = sqlUserRepositoryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = sqlUserRepositoryService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        return new AppUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword()
        );
    }

}
