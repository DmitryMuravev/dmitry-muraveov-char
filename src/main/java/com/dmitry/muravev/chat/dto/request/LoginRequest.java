package com.dmitry.muravev.chat.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @Schema(example = "NewUserLogin", required = true)
    @NotEmpty
    private String login;

    @Schema(example = "NewUserPassword_32131", required = true)
    @NotEmpty
    private String password;
}
