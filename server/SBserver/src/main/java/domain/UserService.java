package domain;

import data.UserRepository;
import models.User;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result<User> add(User user) {
        Result<User> result = new Result<>();
        if (DuplicateValidations.isDuplicate(user, userRepository.findAll())) {
            result.addMessage("User already exists.", ResultType.INVALID);
            return result;
        }
        result.setPayload(userRepository.add(user));
        return result;
    }
}
