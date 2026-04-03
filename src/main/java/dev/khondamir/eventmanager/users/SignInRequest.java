package dev.khondamir.eventmanager.users;

import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @NotNull
        String login,
        @NotNull
        String password
) {
}
