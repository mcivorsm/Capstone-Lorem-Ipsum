package domain;

import data.ProfileRepository;
import data.UserRepository;
import models.Profile;
import models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.profileRepository = profileRepository;
    }

    public Result<User> add(User user) {
        Result<User> result = new Result<>();
        if (DuplicateValidations.isDuplicate(user, userRepository.findAll())) {
            result.addMessage("User already exists.", ResultType.INVALID);
            return result;
        }

        //have get password but it is not being used, getpasswordhash here should return an unhashed password, i know thats confusing sorry
        String rawPassword = user.getPasswordHash();
        validate(user.getUsername());
        validatePassword(rawPassword);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        result.setPayload(userRepository.add(user));
        return result;
    }
    @Transactional
    public boolean deleteById(int userId, int profileId){
        try{
            profileRepository.deleteById(profileId)
        }
    }

    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("username must be less than 50 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("password must be at least 8 characters");
        }

        int digits = 0;
        int letters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c)) {
                letters++;
            } else {
                others++;
            }
        }

        if (digits == 0 || letters == 0 || others == 0) {
            throw new ValidationException("password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }
}
