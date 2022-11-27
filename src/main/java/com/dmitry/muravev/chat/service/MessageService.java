package com.dmitry.muravev.chat.service;

import com.dmitry.muravev.chat.dto.request.SendMessageRequest;
import com.dmitry.muravev.chat.dto.response.GetMessagesResponse;
import com.dmitry.muravev.chat.dto.response.SendMessageResponse;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    /**
     * Save provided message linked to user.
     *
     * @param request {@link SendMessageRequest} contains
     *                                                       message content to be saved.
     * @param username message sender login.
     * @return {@link SendMessageRequest} contains info about message saving.
     */
    SendMessageResponse saveMessage(SendMessageRequest request, String username);

    /**
     *  Get messages from repository.
     *
     * @param pageable {@link Pageable} object contains info to select messages.
     * @return {@link SendMessageRequest} contains info about selected messages.
     */
    GetMessagesResponse getMessages(Pageable pageable);
}
