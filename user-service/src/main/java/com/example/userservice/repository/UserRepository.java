package com.example.userservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUserId(String userId);

    Optional<UserEntity> findByEmail(String username);
}
