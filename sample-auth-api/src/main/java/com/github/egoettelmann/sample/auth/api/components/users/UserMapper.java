package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
interface UserMapper {

    User to(UserDbo dbo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "claims", ignore = true)
    UserDbo from(User dto, @MappingTarget UserDbo dbo);

    default String claimToString(ClaimDbo dbo) {
        return dbo == null ? null : dbo.getValue();
    }

}
