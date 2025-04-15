package data;

import data.mappers.UserMapper;
import models.Profile;
import models.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserJdbcTemplateRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProfileRepository profileRepository;

    public UserJdbcTemplateRepository(JdbcTemplate jdbcTemplate, ProfileRepository profileRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.profileRepository = profileRepository;
    }

    @Override
    public List<User> findAll() {
        final String sql = "select user_id, profile_id, username, email, password, isAdmin from user";
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User findById(int userId) {
        final String sql = "select user_id, profile_id, username, email, password, isAdmin from user where user_id = ?";
        return jdbcTemplate.queryForObject(sql, new UserMapper(), userId);
    }

    @Transactional
    @Override
    public User add(User user) {
        try {
            // Create and attempt to add a new profile
            Profile profile = new Profile();
            Profile savedProfile = profileRepository.add(profile);
            // Throw exception on profile fail exit method
            if (savedProfile == null) {
                throw new RuntimeException("Failed to add profile.");
            }
            user.setProfile(savedProfile);
            // Otherwise Add the user
            final String sql = "insert into user (profile_id, username, email, password, isAdmin) values (?, ?, ?, ?, ?);";
            KeyHolder keyHolder = new GeneratedKeyHolder();

            int rowsAffected = jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1,savedProfile.getProfileId());
                ps.setString(2, user.getUsername());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getPasswordHash());
                ps.setBoolean(5, user.isAdmin());
                return ps;
            }, keyHolder);

            if (rowsAffected <= 0) {
                throw new RuntimeException("Failed to add user.");
            }

            user.setId(keyHolder.getKey().intValue());
            return user;

        } catch (Exception ex) {

            System.err.println("Error during user/profile add: " + ex.getMessage());

        }

        return user;
    }


    @Override
    public boolean update(User user) {
        final String sql = "update user set "
                + "username = ?, email = ?, password = ? where user_id = ? ";

        return jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.getPasswordHash(), user.getId()) > 0;
    }


        @Override
        public boolean deleteById(int userId) throws DataIntegrityViolationException {
            return jdbcTemplate.update(
                    "delete from user where user_id = ?", userId) > 0;
        }

}
