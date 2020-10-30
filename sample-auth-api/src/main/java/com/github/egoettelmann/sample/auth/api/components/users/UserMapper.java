package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    User to(UserDbo dbo);

    UserDbo from(User dto);

}
