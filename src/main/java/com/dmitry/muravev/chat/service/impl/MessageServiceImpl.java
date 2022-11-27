package com.dmitry.muravev.chat.service.impl;

import com.dmitry.muravev.chat.dto.request.SendMessageRequest;
import com.dmitry.muravev.chat.dto.response.GetMessagesResponse;
import com.dmitry.muravev.chat.dto.response.MessageData;
import com.dmitry.muravev.chat.dto.response.SendMessageResponse;
import com.dmitry.muravev.chat.entity.MessageEntity;
import com.dmitry.muravev.chat.entity.UserEntity;
import com.dmitry.muravev.chat.repository.MessageRepository;
import com.dmitry.muravev.chat.service.MessageService;
import com.dmitry.muravev.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final String SUCCESS_MESSAGE_SAVING_MESSAGE = "Message send successfully";

    private final MessageRepository messageRepository;
    private final UserService userService;

    /**
     * Save provided message linked to user.
     *
     * @param request {@link SendMessageRequest} contains message content to be saved.
     * @param username message sender login.
     * @return {@link SendMessageResponse} contains {@link MessageServiceImpl#SUCCESS_MESSAGE_SAVING_MESSAGE}.
     */
    public SendMessageResponse saveMessage(SendMessageRequest request, String username) {
        UserEntity user = userService.findUser(username);

        MessageEntity message = MessageEntity.builder()
                .content(request.getMessage())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        messageRepository.save(message);
        return new SendMessageResponse(SUCCESS_MESSAGE_SAVING_MESSAGE);
    }

    /**
     * Get messages from repository.
     *
     * @param pageable {@link Pageable} object contains info to select messages.
     * @return {@link GetMessagesResponse} contains list of selected messages.
     */
    public GetMessagesResponse getMessages(Pageable pageable) {
        Page<MessageEntity> messages = messageRepository.findAll(pageable);

        List<MessageData> messagesData = messages.stream()
                .map(me -> MessageData.builder()
                        .message(me.getContent())
                        .user(me.getUser().getLogin())
                        .createdAt(me.getCreatedAt())
                        .build()).collect(Collectors.toList());

        return new GetMessagesResponse(messagesData);
    }
}
