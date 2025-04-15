package domain;

import data.UserRepository;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    public Result<User> add(User user) {
        Result<User> result = new Result<>();
        if (DuplicateValidations.isDuplicate(user, userRepository.findAll())) {
            result.addMessage("User already exists.", ResultType.INVALID);
            return result;
        }

        //have get password but it is not being used, getpasswordhash here should return an unhashed password, i know thats confusing sorry
        String rawPassword = user.getPasswordHash();
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        result.setPayload(userRepository.add(user));
        return result;
    }
}
