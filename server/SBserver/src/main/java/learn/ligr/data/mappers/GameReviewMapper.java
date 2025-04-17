package learn.ligr.data.mappers;

import learn.ligr.models.*;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameReviewMapper implements RowMapper<GameReview> {
    @Override
    public GameReview mapRow(ResultSet resultSet, int i) throws SQLException {
        GameReview gameReview = new GameReview();

        // Map game review fields
        gameReview.setGameReviewId(resultSet.getInt("review_id"));
        gameReview.setRating(resultSet.getInt("rating"));
        gameReview.setReviewText(resultSet.getString("review"));

        // Map the User object
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password"));
        user.setAdmin(resultSet.getBoolean("isAdmin"));

        // Map the Profile object
        Profile profile = new Profile();
        profile.setProfileId(resultSet.getInt("user_profile_id"));

        //profile.setFavoriteGame(resultSet.getInt("fav_game_id"));
        Game favGame = new Game();
        favGame.setGameId(resultSet.getInt("fav_game_id"));
        favGame.setTitle(resultSet.getString("fav_game_title"));
        favGame.setDeveloper(resultSet.getString("fav_game_developer"));
        favGame.setGenre(resultSet.getString("fav_game_genre"));
        favGame.setYearReleased(resultSet.getInt("fav_game_year_released"));
        favGame.setPlatform(resultSet.getString("fav_game_platform"));
        favGame.setRegion(Region.valueOf(resultSet.getString("fav_game_release_region")));
        profile.setFavoriteGame(favGame);

        profile.setDateJoined(resultSet.getDate("date_joined"));
        profile.setRegion(Region.valueOf(resultSet.getString("profile_region")));
        profile.setProfileDescription(resultSet.getString("profile_description"));
        profile.setPreferredGenre(resultSet.getString("preferred_genre"));


        user.setProfile(profile);

        // Map the Game object
        Game reviewedGame = new Game();
        reviewedGame.setGameId(resultSet.getInt("reviewed_game_id"));
        reviewedGame.setTitle(resultSet.getString("reviewed_game_title"));
        reviewedGame.setDeveloper(resultSet.getString("reviewed_game_developer"));
        reviewedGame.setGenre(resultSet.getString("reviewed_game_genre"));
        reviewedGame.setYearReleased(resultSet.getInt("reviewed_game_year_released"));
        reviewedGame.setPlatform(resultSet.getString("reviewed_game_platform"));
        reviewedGame.setRegion(Region.valueOf(resultSet.getString("reviewed_game_release_region")));

        // Set the Game and User objects into the GameReview object
        gameReview.setUser(user);
        gameReview.setGame(reviewedGame);

        return gameReview;
    }
}
