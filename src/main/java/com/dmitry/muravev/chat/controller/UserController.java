package com.dmitry.muravev.chat.controller;

import com.dmitry.muravev.chat.dto.request.RegistrationRequest;
import com.dmitry.muravev.chat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Handle user registration requests.
     *
     * @param request {@link RegistrationRequest} contains required user registration info.
     */
    @PostMapping("/users")
    public void register (@RequestBody @Valid RegistrationRequest request) {
        userService.register(request);
    }

}
