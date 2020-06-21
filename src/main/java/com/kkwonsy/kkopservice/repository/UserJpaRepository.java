package com.kkwonsy.kkopservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkwonsy.kkopservice.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
