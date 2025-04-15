package learn.ligr.data;

import learn.ligr.data.mappers.GameReviewMapper;
import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameReviewJdbcTemplateRepository implements GameReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameReviewJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GameReview> findAll() {
        final String sql = "select gr.review_id, gr.game_id, gr.user_id, gr.review, gr.rating, "
                +"u.user_id, u.profile_id, u.username, u.email, u.password, u.isAdmin, "
                +"p.profile_id, p.fav_game_id, p.date_joined, p.region, p.profile_description, p.preferred_genre, "
                +"g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region "
                +"from game_review gr "
                +"join user u on gr.user_id = u.user_id "
                +"join profile p on u.profile_id = p.profile_id "
                +"join game g on p.fav_game_id = g.game_id "
                +"limit 1000;";

        return jdbcTemplate.query(sql, new GameReviewMapper());
    }

    @Override
    public List<GameReview> findByGame(Game game) {

        final String sql = "select gr.review_id, gr.game_id, gr.user_id, gr.review, gr.rating, "
                +"u.user_id, u.profile_id, u.username, u.email, u.password, u.isAdmin, "
                +"p.profile_id, p.fav_game_id, p.date_joined, p.region, p.profile_description, p.preferred_genre, "
                +"g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region "
                +"from game_review gr "
                +"join user u on gr.user_id = u.user_id "
                +"join profile p on u.profile_id = p.profile_id "
                +"join game g on p.fav_game_id = g.game_id "
                +"where gr.game_id = ? "
                +"limit 1000;";

        List<GameReview> games = jdbcTemplate.query(sql, new GameReviewMapper(), game.getGameId());

        return games;
    }

    @Override
    public List<GameReview> findByUser(User user) {
        final String sql = "select gr.review_id, gr.game_id, gr.user_id, gr.review, gr.rating, "
                +"u.user_id, u.profile_id, u.username, u.email, u.password, u.isAdmin, "
                +"p.profile_id, p.fav_game_id, p.date_joined, p.region, p.profile_description, p.preferred_genre, "
                +"g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region "
                +"from game_review gr "
                +"join user u on gr.user_id = u.user_id "
                +"join profile p on u.profile_id = p.profile_id "
                +"join game g on p.fav_game_id = g.game_id "
                +"where gr.user_id = ? "
                +"limit 1000;";

        List<GameReview> games = jdbcTemplate.query(sql, new GameReviewMapper(), user.getId());

        return games;
    }

    @Override
    public GameReview findById(int gameReviewId) {

        final String sql = "select gr.review_id, gr.game_id, gr.user_id, gr.review, gr.rating, "
                +"u.user_id, u.profile_id, u.username, u.email, u.password, u.isAdmin, "
                +"p.profile_id, p.fav_game_id, p.date_joined, p.region, p.profile_description, p.preferred_genre, "
                +"g.game_id, g.title, g.developer, g.genre, g.year_released, g.platform, g.region "
                +"from game_review gr "
                +"join user u on gr.user_id = u.user_id "
                +"join profile p on u.profile_id = p.profile_id "
                +"join game g on p.fav_game_id = g.game_id "
                +"where gr.review_id = ? "
                +"limit 1000;";

        return jdbcTemplate.query(sql, new GameReviewMapper(), gameReviewId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public GameReview add(GameReview gameReview) {

        final String sql = "insert into game_review (game_id, user_id, review, rating) "
                + " values (?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, gameReview.getGame().getGameId());
            ps.setInt(2, gameReview.getUser().getId());
            ps.setString(3, gameReview.getReviewText());
            ps.setDouble(4, gameReview.getRating());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        gameReview.setGameReviewId(keyHolder.getKey().intValue());
        return gameReview;
    }

    @Override
    public boolean update(GameReview gameReview) {

        final String sql = "update game_review set "
                + "game_id = ?, "
                + "user_id = ?, "
                + "review = ?, "
                + "rating = ? "
                + "where review_id = ?;";

        return jdbcTemplate.update(sql,
                gameReview.getGame().getGameId(),
                gameReview.getUser().getId(),
                gameReview.getReviewText(),
                gameReview.getRating(),
                gameReview.getGameReviewId()) > 0;
    }

    @Override
    public boolean deleteById(int gameReviewId) {
        return jdbcTemplate.update("delete from game_review where review_id = ?;", gameReviewId) > 0;
    }
}
