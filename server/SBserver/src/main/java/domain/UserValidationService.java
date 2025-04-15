package domain;

import data.UserRepository;
import data.mappers.UserMapper;
import models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserValidationService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    public UserValidationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return user;  // Return a custom User object that implements UserDetails
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}