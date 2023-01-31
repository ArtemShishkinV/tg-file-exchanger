package com.shishkin.common.jpa.repository;

import com.shishkin.common.jpa.entity.PhotoEntity;
import org.springframework.data.repository.CrudRepository;

public interface PhotoRepository extends CrudRepository<PhotoEntity, Long> {
}
