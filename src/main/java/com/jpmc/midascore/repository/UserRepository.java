package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserRecord, Long> {
    Optional<UserRecord> findById(Long id);
    UserRecord findByName(String name); // Keep if you want, but probably not needed for Kafka task
}

