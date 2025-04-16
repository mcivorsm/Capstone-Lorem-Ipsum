package learn.ligr.domain;

import learn.ligr.data.UserRepository;
import learn.ligr.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    System.out.println("ENTER LOAD BY USERNAME ****");
        try {

            User user = userRepository.findByUsername(username);
            System.out.println("USER NAME at load after find by : " + user.getUsername());
            System.out.println("Hashed password from DB: " + user.getPassword());
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            return user;  // Return a custom User object that implements UserDetails
        } catch (EmptyResultDataAccessException ex) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

}