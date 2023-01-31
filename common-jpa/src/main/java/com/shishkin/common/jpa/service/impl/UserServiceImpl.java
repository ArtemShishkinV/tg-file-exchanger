package com.shishkin.common.jpa.service.impl;

import com.shishkin.common.jpa.entity.UserEntity;
import com.shishkin.common.jpa.repository.UserRepository;
import com.shishkin.common.jpa.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity findByTelegramId(Long id) {
        return userRepository.findByTelegramId(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        if (user != null) {
            return userRepository.save(user);
        }
        return null;
    }
}
