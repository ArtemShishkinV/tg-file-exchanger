package com.shishkin.common.jpa.service;

import com.shishkin.common.jpa.entity.UserEntity;

public interface UserService {
    UserEntity findByTelegramId(Long id);

    UserEntity save(UserEntity user);
}
