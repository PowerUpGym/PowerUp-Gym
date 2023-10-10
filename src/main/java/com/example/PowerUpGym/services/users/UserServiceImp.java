package com.example.PowerUpGym.services.users;

import com.example.PowerUpGym.bo.auth.users.PlayerRegistrationRequest;
import com.example.PowerUpGym.bo.auth.users.RegistrationRequests;
import com.example.PowerUpGym.bo.auth.users.UserRegistrationRequest;
import com.example.PowerUpGym.entity.users.UserEntity;
import com.example.PowerUpGym.repositories.UserEntityRepositories;
import com.example.PowerUpGym.util.exception.BodyGuardException;
import com.example.PowerUpGym.util.validators.CompositeValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.PowerUpGym.util.validators.CompositeValidator.hasValue;
import static java.lang.String.join;
import static java.util.Objects.nonNull;


@Service
public class UserServiceImp implements UserService {

    private final UserEntityRepositories userEntityRepositories;

    public UserServiceImp(UserEntityRepositories userEntityRepositories) {
        this.userEntityRepositories = userEntityRepositories;
    }


    protected void validate(List<String> violations) {
        if (!violations.isEmpty()) {
            throw new BodyGuardException(join(",", violations));
        }
    }

    @Override
    public void createPreValidation(UserRegistrationRequest createDto) {
        List<String> violation = new CompositeValidator<UserRegistrationRequest, String>()
                .addValidator(r -> hasValue(r.getUsername()), "User Name Cannot Be Empty")
                .addValidator(r -> hasValue(r.getEmail()), "Email Cannot Be Empty")
                .addValidator(r -> nonNull(r.getPassword()), "Password Cannot Be Empty")
                .addValidator(r -> r.getEmail() == null || Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(r.getEmail()).matches(), "Enter a valid email")
                .addValidator(r -> r.getEmail() == null || !userEntityRepositories.findByEmail(r.getEmail().toLowerCase()).isPresent(), "Email already exists")
                .addValidator(r -> Pattern.compile("^(?=.*[A-Z]).{8,20}$").matcher(r.getPassword()).matches(), "Password should be minimum 8 characters with 1 upper case letter")
                .validate(createDto);
        validate(violation);
    }

    @Override
    public void createPreValidation(RegistrationRequests createDto) {
        List<String> violation = new CompositeValidator<RegistrationRequests, String>()
                .addValidator(r -> hasValue(r.getAddress()), "Address Cannot Be Empty")
                .addValidator(r -> hasValue(r.getFullName()), "FullName Cannot Be Empty")
                .addValidator(r -> r.getEmail() == null || !userEntityRepositories.findByEmail(r.getEmail().toLowerCase()).isPresent(), "Email already exists")
                .addValidator(r -> r.getEmail() == null || Pattern.compile("^(.+)@(.+)\\.(.+)$").matcher(r.getEmail()).matches(), "Enter a valid email")
                .addValidator(r -> r.getPhoneNumber().length() > 10, "Phone Cannot Be less than 10")
                .addValidator(r -> r.getHeight() > 0, "Height must be > 0")
                .addValidator(r -> r.getWeight() > 0, "Height must be > 0")
                .addValidator(r -> r.getAge() > 0, "Height must be > 0")
                .addValidator(r -> nonNull(r.getPassword()), "Password Cannot Be Empty")
                .addValidator(r -> hasValue(r.getAddress()), "Address Cannot Be Empty")
                .addValidator(r -> hasValue(r.getAddress()), "Address Cannot Be Empty")
                .addValidator(r -> Pattern.compile("^(?=.*[A-Z]).{8,20}$").matcher(r.getPassword()).matches(), "Password should be minimum 8 characters with 1 upper case letter")
                .validate(createDto);
        validate(violation);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userEntityRepositories.findAll();
    }

    @Override
    public UserEntity signupUser(UserEntity user) {
        userEntityRepositories.save(user);
        return user;
    }

    @Override
    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userEntityRepositories.findById(userId);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Override
    public UserEntity findUserById(Long userId) {
        return userEntityRepositories.findById(userId).orElse(null);
    }

    @Override
    public void saveUser(UserEntity user) {
        userEntityRepositories.save(user);
    }

    @Override
    public UserEntity findUserByUsername(String senderUsername) {
        return userEntityRepositories.findByUsername(senderUsername);
    }

}
