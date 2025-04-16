package learn.ligr.data;

import learn.ligr.data.mappers.ProfileMapper;
import learn.ligr.models.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        final String sql = "select p.profile_id, p.fav_game_id,date_joined, p.region, p.profile_description, p.preferred_genre, " +
                "g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region from profile p " +
                "join game g on p.fav_game_id = g.game_id ;";
        return jdbcTemplate.query(sql, new ProfileMapper());
    }

    @Override
    public Profile findById(int profileId) {
        final String sql = "select p.profile_id, p.fav_game_id,date_joined, p.region, p.profile_description, p.preferred_genre, " +
                "g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region from profile p " +
                "join game g on p.fav_game_id = g.game_id where profile_id = ?;";

        return jdbcTemplate.query(sql,new ProfileMapper(),profileId).stream()
                .findFirst().orElse(null);
    }

    @Transactional
    @Override
    public Profile add(Profile profile) {
        final String sql = "insert into profile (fav_game_id, date_joined, region, profile_description, preferred_genre) values (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,profile.getFavoriteGame().getGameId());
            preparedStatement.setDate(2, new java.sql.Date(profile.getDateJoined().getTime()));
            preparedStatement.setString(3, profile.getRegion().getDisplayName());
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
                profile.getFavoriteGame().getGameId(), profile.getRegion().getDisplayName(), profile.getProfileDescription(), profile.getPreferredGenre(), profile.getProfileId()) > 0;
    }

    @Override
    public boolean deleteById(int profileId) {
        jdbcTemplate.update("delete from user where profile_id = ?;", profileId);

        return jdbcTemplate.update(
                "delete from profile where profile_id = ?", profileId) > 0;
    }
}
