package learn.ligr.data;

import learn.ligr.models.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll(); // finds all users in the database, useful for admin control over user accounts
	User findById(int userId);// finds a user by id
	User findByUsername(String username);
	User add(User user); // create a user
	boolean update(User user); // update a user
	boolean deleteById(int userId); // delete a user by id
}
