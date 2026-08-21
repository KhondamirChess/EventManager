package dev.khondamir.eventmanager.users;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotNull
        @Size(min = 5)
        String login,
        @NotNull
        @Size(min = 5)
        String password,
        @NotNull
        Integer age
) {
}
