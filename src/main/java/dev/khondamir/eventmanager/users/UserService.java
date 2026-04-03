package dev.khondamir.eventmanager.users;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByLogin(signUpRequest.login())){
            throw new IllegalArgumentException("Login already taken");
        }
        var hashedPssword = passwordEncoder.encode(signUpRequest.password());
        var userToSave = new UserEntity(
                null,
                signUpRequest.login(),
                signUpRequest.age(),
                hashedPssword,
                UserRole.USER.name()
        );
        var savedUser = userRepository.save(userToSave);

        return mapToDomain(savedUser);
    }

    public User findByLogin(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(()-> new EntityNotFoundException("User not found"));

        return mapToDomain(user);
    }

    public static User mapToDomain(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getLogin(),
                userEntity.getAge(),
                UserRole.valueOf(userEntity.getRole())
        );
    }
}
