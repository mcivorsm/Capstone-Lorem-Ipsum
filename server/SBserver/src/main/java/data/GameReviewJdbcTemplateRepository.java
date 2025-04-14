package data;

import models.GameReview;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GameReviewJdbcTemplateRepository implements GameReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameReviewJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GameReview> findAll() {
        return List.of();
    }

    @Override
    public List<GameReview> findByGame() {
        return List.of();
    }

    @Override
    public List<GameReview> findByUser() {
        return List.of();
    }

    @Override
    public GameReview findById(int gameReviewId) {
        return null;
    }

    @Override
    public GameReview add(GameReview gameReview) {
        return null;
    }

    @Override
    public boolean update(GameReview gameReview) {
        return false;
    }

    @Override
    public boolean deleteById(int gameReviewId) {
        return false;
    }
}
