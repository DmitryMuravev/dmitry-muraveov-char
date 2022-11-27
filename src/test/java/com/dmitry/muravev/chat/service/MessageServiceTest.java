package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.SendMessageRequest;
import com.dmitry.muravev.chat.entity.MessageEntity;
import com.dmitry.muravev.chat.entity.UserEntity;
import com.dmitry.muravev.chat.repository.MessageRepository;
import com.dmitry.muravev.chat.repository.RoleRepository;
import com.dmitry.muravev.chat.repository.UserRepository;
import com.dmitry.muravev.chat.service.impl.MessageServiceImpl;
import com.dmitry.muravev.chat.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageServiceTest {

    private static final String TEST_MESSAGE_CONTENT = "usermessage";
    private static final String TEST_USER_LOGIN = "userlogin";
    private static final UUID TEST_USER_ID = UUID.randomUUID();

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private MessageRepository messageRepository;
    private Map<UUID, MessageEntity> savedMessages;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        roleRepository = Mockito.mock(RoleRepository.class);
        messageRepository = Mockito.mock(MessageRepository.class);
        savedMessages = new HashMap<>();
    }


    @Test
    public void whenSaveMessageAndUserExistsThenMessageSaved() {
        Mockito.when(userRepository.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.of(new UserEntity()));

        UserService userService = new UserServiceImpl(userRepository,
                roleRepository,
                new BCryptPasswordEncoder());

        Mockito.when(messageRepository.save(Mockito.any(MessageEntity.class)))
                .thenAnswer(i -> savedMessages.put(TEST_USER_ID,
                        i.getArgument(0, MessageEntity.class)));

        MessageService messageService = new MessageServiceImpl(messageRepository,
                userService);

        messageService.saveMessage(new SendMessageRequest(TEST_MESSAGE_CONTENT),
                TEST_USER_LOGIN);

        assertTrue(savedMessages.values().size() > 0);
    }

    @Test
    public void whenSaveMessageAndUserNotExistsThenThrowException() {
        Mockito.when(userRepository.findByLogin(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserService userService = new UserServiceImpl(userRepository,
                roleRepository, new BCryptPasswordEncoder());

        MessageService messageService = new MessageServiceImpl(messageRepository,
                userService);

        assertThrows(ResponseStatusException.class,
                () -> messageService.saveMessage(
                        new SendMessageRequest(TEST_MESSAGE_CONTENT),
                        TEST_MESSAGE_CONTENT));

    }
}
