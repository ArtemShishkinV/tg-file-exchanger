package com.shishkin.common.jpa.repository;

import com.shishkin.common.jpa.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByTelegramId(Long id);
}
