package learn.ligr.data.mappers;

import learn.ligr.models.Profile;
import learn.ligr.models.Region;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileMapper implements RowMapper<Profile> {

    @Override
    public Profile mapRow(ResultSet resultSet, int i) throws SQLException {
        Profile profile = new Profile();

        profile.setProfileId(resultSet.getInt("profile_id"));

        GameMapper gameMapper = new GameMapper();
        profile.setFavoriteGame(gameMapper.mapRow(resultSet,i));

        profile.setDateJoined(resultSet.getDate("date_joined"));
        profile.setRegion(Region.valueOf(resultSet.getString("region")));
        profile.setProfileDescription(resultSet.getString("profile_description"));
        profile.setPreferredGenre(resultSet.getString("preferred_genre"));

        return profile;
    }
}
