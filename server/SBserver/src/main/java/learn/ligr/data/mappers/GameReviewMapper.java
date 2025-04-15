package learn.ligr.data.mappers;

import learn.ligr.models.GameReview;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameReviewMapper implements RowMapper<GameReview> {
    @Override
    public GameReview mapRow(ResultSet resultSet, int i) throws SQLException {
        GameReview gr = new GameReview();
        gr.setGameReviewId(resultSet.getInt("review_id"));

        GameMapper gameMapper = new GameMapper();
        gr.setGame(gameMapper.mapRow(resultSet, i));

        UserMapper userMapper = new UserMapper();
        gr.setUser(userMapper.mapRow(resultSet, i));

        gr.setReviewText(resultSet.getString("review"));
        gr.setRating(resultSet.getDouble("rating"));

        return gr;
    }
}
