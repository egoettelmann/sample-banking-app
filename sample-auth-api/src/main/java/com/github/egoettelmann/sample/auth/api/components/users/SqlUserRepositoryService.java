package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class SqlUserRepositoryService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public SqlUserRepositoryService(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public Optional<User> getUser(final String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::to);
    }

    public User save(User user) {
        final Optional<UserDbo> existing = userRepository.findByUsername(user.getUsername());
        UserDbo dbo = existing.orElse(new UserDbo());
        dbo = userRepository.save(
                userMapper.from(user, dbo)
        );
        return userMapper.to(dbo);
    }

}
