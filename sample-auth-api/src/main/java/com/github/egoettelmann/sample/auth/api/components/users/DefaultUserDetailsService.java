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

    @Override
    public User getUserInfo(AppUserDetails appUser) {
        return sqlUserRepositoryService.getUserById(appUser.getUserId());
    }

    @Override
    public User updateUserInfo(AppUserDetails appUser, UserRequest userRequest) {
        User user = sqlUserRepositoryService.getUserById(appUser.getUserId());
        user.setPassword(userRequest.getPassword());
        user.setAddress(userRequest.getAddress());
        return sqlUserRepositoryService.updateUser(user);
    }

}
