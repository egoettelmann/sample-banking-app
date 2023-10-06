package com.github.egoettelmann.sample.auth.api.core;

import com.github.egoettelmann.sample.auth.api.core.dtos.AppUserDetails;
import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import com.github.egoettelmann.sample.auth.api.core.dtos.requests.UserRequest;

import java.util.Optional;

public interface UserInfoService {

    Optional<User> getUserInfo(AppUserDetails appUser);

    User updateUserInfo(AppUserDetails appUser, UserRequest userRequest);

}
