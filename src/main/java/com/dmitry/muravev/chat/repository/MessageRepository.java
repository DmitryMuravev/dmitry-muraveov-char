package com.dmitry.muravev.chat.repository;

import com.dmitry.muravev.chat.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, UUID> {
    @Override
    @EntityGraph(value = "message-entity-graph")
    Page<MessageEntity> findAll(Pageable pageable);
}
