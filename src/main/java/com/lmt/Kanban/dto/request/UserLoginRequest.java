package com.lmt.Kanban.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRequest implements Serializable {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(
            description = "Email address of the user",
            example = "lmt@kanban.com"
    )
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(
            description = "The password of the user",
            example = "StrongP@ss123!"
    )
    private String password;
}