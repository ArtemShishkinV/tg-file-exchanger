package com.shishkin.common.jpa.repository;

import com.shishkin.common.jpa.entity.DocumentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DocumentRepository extends CrudRepository<DocumentEntity, Long> {
}
