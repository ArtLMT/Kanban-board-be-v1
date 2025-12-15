package com.lmt.Kanban.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    @Schema(
            description = "Email address of the user",
            example = "lmt@kanban.com"
    )
    private String email;

    @NotBlank(message = "Username can't be empty")
    private String username;

    @NotBlank(message = "Password can't be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(
            description = "The password of the user",
            example = "StrongP@ss123!"
    )
    private String password;
}
