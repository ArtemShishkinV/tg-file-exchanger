package com.shishkin.repository;

import com.shishkin.entity.RawData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawDataRepo extends CrudRepository<RawData, Long> {
}
