package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.UserInfoService;
import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import com.github.egoettelmann.sample.auth.api.core.dtos.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class DefaultUserDetailsService implements UserDetailsService, UserInfoService {

    private final SqlUserRepositoryService sqlUserRepositoryService;

    @Autowired
    public DefaultUserDetailsService(
            SqlUserRepositoryService sqlUserRepositoryService
    ) {
        this.sqlUserRepositoryService = sqlUserRepositoryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = sqlUserRepositoryService.getUser(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
        return user.map(u -> new AppUserDetails(
                u.getUsername(),
                u.getPassword(),
                u.getClaims()
        )).get();
    }

    @Override
    public Optional<User> getUserInfo(AppUserDetails appUser) {
        return sqlUserRepositoryService.getUser(appUser.getUsername());
    }

    @Override
    public User updateUserInfo(AppUserDetails appUser, UserRequest userRequest) {
        final Optional<User> existing = sqlUserRepositoryService.getUser(appUser.getUsername());
        if (!existing.isPresent()) {
            throw new UsernameNotFoundException("No user found with username: " + appUser.getUsername());
        }
        final User user = existing.get();
        user.setPassword(userRequest.getPassword()); // TODO: encode
        user.setAddress(userRequest.getAddress());
        return sqlUserRepositoryService.save(user);
    }

}
