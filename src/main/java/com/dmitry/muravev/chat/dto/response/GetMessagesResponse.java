package com.dmitry.muravev.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GetMessagesResponse {
    private List<MessageData> messages;
}
