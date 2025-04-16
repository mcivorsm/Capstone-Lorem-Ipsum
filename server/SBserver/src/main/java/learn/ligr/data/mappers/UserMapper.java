package learn.ligr.data.mappers;

import learn.ligr.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setId(resultSet.getInt("user_id"));

        ProfileMapper profileMapper = new ProfileMapper();
        user.setProfile(profileMapper.mapRow(resultSet, i));

        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password"));
        user.setAdmin(resultSet.getBoolean("isAdmin"));

        System.out.println("User Mapper...Username here: " + user.getUsername());
        return user;
    }
}
