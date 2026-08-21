package dev.khondamir.eventmanager.users;

import dev.khondamir.eventmanager.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .body(new UserDto(user.id(), user.login(), user.age(), user.role()));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticateUser(
            @RequestBody @Valid SignInRequest signInRequest
    ){
        log.info("Get request for signin: login={}", signInRequest.login());
        var token = authenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }
    @GetMapping("/{userId}")
    public UserDto getUserById(
            @PathVariable("userId") Long userId
    ){
        log.info("Get request for user: userId={}", userId);
        var user = userService.findById(userId);
        return new UserDto(user.id(), user.login(), user.age(), user.role());
    }
}
