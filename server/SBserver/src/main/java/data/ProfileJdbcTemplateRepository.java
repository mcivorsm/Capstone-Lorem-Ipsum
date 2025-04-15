package data;

import data.mappers.ProfileMapper;
import data.mappers.UserMapper;
import models.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ProfileJdbcTemplateRepository implements ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Profile> findAll() {
        final String sql = "select profile_id, fav_game_id,date_joined, region, profile_description, preferredGenre from profile";
        return jdbcTemplate.query(sql, new ProfileMapper());
    }

    @Override
    public Profile findById(int profileId) {
        final String sql = "select profile_id, fav_game_id,date_joined, region, profile_description, " +
                "preferredGenre from profile where profile_id = ?";
        return jdbcTemplate.queryForObject(sql,new ProfileMapper(),profileId);
    }

    @Transactional
    @Override
    public Profile add(Profile profile) {
        final String sql = "insert into profile (fav_game_id, date_joined, region, profile_description, preferred_genre) values (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setNull(1,java.sql.Types.INTEGER);
            preparedStatement.setDate(2,  new java.sql.Date(profile.getDateJoined().getTime()));
            preparedStatement.setString(3,null);
            preparedStatement.setString(4,null);
            preparedStatement.setString(5,null);

            return preparedStatement;
        }, keyHolder);

        if(rowsAffected <= 0) {
            return null;
        }

        profile.setProfileId(keyHolder.getKey().intValue());
        return profile;
    }

    @Override
    public boolean update(Profile profile) {
        final String sql = "update profile set "
                + "fav_game_id = ?, region = ?, profile_description = ?, preferred_genre = ? where profile_id = ? ";

        return jdbcTemplate.update(sql,
                profile.getFavoriteGame().getGameId(), profile.getRegion(), profile.getProfileDescription(), profile.getPreferredGenre(), profile.getProfileId()) > 0;
    }

    @Override
    public boolean deleteById(int profileId) {
        return jdbcTemplate.update(
                "delete from profile where profile_id = ?", profileId) > 0;
    }
}
