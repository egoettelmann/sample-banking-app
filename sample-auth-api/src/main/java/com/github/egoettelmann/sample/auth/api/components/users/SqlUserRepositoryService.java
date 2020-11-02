package com.github.egoettelmann.sample.auth.api.components.users;

import com.github.egoettelmann.sample.auth.api.core.dtos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public User getUserById(final Long userId) {
        return userMapper.to(
                userRepository.findById(userId).orElse(null)
        );
    }

    public User getUserByUsername(final String username) {
        return userMapper.to(
                userRepository.findByUsername(username)
        );
    }

    public User updateUser(User user) {
        UserDbo dbo = userMapper.from(user);
        return userMapper.to(
                userRepository.save(dbo)
        );
    }
}
