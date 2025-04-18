package learn.ligr.domain;


import learn.ligr.data.UserRepository;
import learn.ligr.data.ProfileRepository;
import learn.ligr.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.profileRepository = profileRepository;
    }

    public User findById(int userId) {
        return userRepository.findById(userId);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Result<User> update(User user) {
        Result<User> result = new Result<>();
        List<User> allUsers = userRepository.findAll();
        if(DuplicateValidations.isDuplicate(user,allUsers)){
                result.addMessage("No change has been made", ResultType.INVALID);
                return result;
        }

        if (DuplicateValidations.isUsernameDuplicate(user,allUsers)) {
            result.addMessage("Username already in use", ResultType.INVALID);
            return result;
        }

        if (DuplicateValidations.isEmailDuplicate(user,allUsers)) {
            result.addMessage("Email already in use", ResultType.INVALID);
            return result;
        }

        if(user.getId() == 1){
            String msg = String.format("UserID: %s is a reserved ID, and cannot be deleted.", user.getId());
            result.addMessage(msg, ResultType.INVALID);
        }

        String rawPassword = user.getPasswordHash();
        System.out.println(rawPassword);
        validate(user.getUsername());
        validatePassword(rawPassword);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        System.out.println(passwordEncoder.encode(rawPassword));

        if (!userRepository.update(user)) {
            String msg = String.format("userId: %s, not found", user.getId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<User> add(User user) {
        Result<User> result = new Result<>();

        if (user.getId() != 0) {
            result.addMessage("userId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }
        if (DuplicateValidations.isDuplicate(user, userRepository.findAll())) {
            result.addMessage("Username already in use.", ResultType.INVALID);
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
    public Result<User> deleteById(int userId, int profileId) {
        Result<User> result = new Result<>();

        if(userId == 1){
            String msg = String.format("UserID: %s is a reserved ID, and cannot be deleted.", userId);
            result.addMessage(msg, ResultType.INVALID);
        }
        if(profileId == 1){
            String msg = String.format("ProfileID: %s is a reserved ID, and cannot be deleted.", profileId);
            result.addMessage(msg, ResultType.INVALID);
        }

        if(!result.isSuccess()){
            return result;
        }

        try {
            // Delete profile first if there are foreign key constraints
            result.setPayload(userRepository.findById(userId));
            profileRepository.deleteById(profileId);
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            // Log the exception if needed
            String msg = String.format("Error deleting user/profile: " + ex.getMessage());
            result.addMessage(msg, ResultType.INVALID);
        }
        return result;
    }

    private void validate(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        if (username.length() > 50) {
            throw new ValidationException("Username must be less than 50 characters");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
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
            throw new ValidationException("Password must contain a digit, a letter, and a non-digit/non-letter");
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
