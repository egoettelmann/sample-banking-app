package com.github.egoettelmann.sample.auth.api.controllers;

import com.github.egoettelmann.sample.auth.api.core.UserInfoService;
import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import com.github.egoettelmann.sample.auth.api.core.dtos.requests.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * User Info controller
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    private final UserInfoService userInfoService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInfoController(
            UserInfoService userInfoService,
            PasswordEncoder passwordEncoder
    ) {
        this.userInfoService = userInfoService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets the current user info
     */
    @GetMapping
    public User getCurrentUserInfo() {
        return userInfoService.getUserInfo(AppUserDetails.current())
                .orElseThrow(() -> new UsernameNotFoundException("No info found for current user"));
    }

    /**
     * Updates the current user info
     */
    @PostMapping
    public User updateUserInfo(
            @Valid @RequestBody UserRequest userRequest
    ) {
        // Encoding password
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        return userInfoService.updateUserInfo(AppUserDetails.current(), userRequest);
    }

}
