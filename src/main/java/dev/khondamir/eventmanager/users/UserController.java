package dev.khondamir.eventmanager.users;

import dev.khondamir.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private AuthenticationService authenticationService;

    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("Get request for signup: login={}", signUpRequest.login());
        var user = userService.registerUser(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserDto(user.id(), user.login()));
    }
}
