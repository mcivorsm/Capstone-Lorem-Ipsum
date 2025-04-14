package data;

import models.User;

import java.util.List;

public class UserJdbcTemplateRepository implements UserRepository {
    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User findById(int userId) {
        return null;
    }

    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean deleteById(int userId) {
        return false;
    }
}
