package com.dmitry.muravev.chat.repository;

import com.dmitry.muravev.chat.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @EntityGraph(value = "user-entity-graph")
    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);

}

