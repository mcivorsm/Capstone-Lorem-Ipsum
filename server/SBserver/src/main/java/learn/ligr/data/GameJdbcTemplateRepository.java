package learn.ligr.data;

import learn.ligr.data.mappers.GameMapper;
import learn.ligr.models.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GameJdbcTemplateRepository implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Game> findAll() {
        final String sql = "select game_id, title, developer, genre, year_released, platform, region "
                + "from game limit 1000;";
        return jdbcTemplate.query(sql, new GameMapper());
    }

    @Override
    public List<Game> findByGenre(String genre) {
        final String sql = "select game_id, title, developer, genre, year_released, platform, region "
                + "from game where genre = ?;";
        return jdbcTemplate.query(sql, new GameMapper(), genre);
    }

    @Override
    public Game findById(int gameId) {
        final String sql = "select game_id, title, developer, genre, " +
                "year_released, platform, region from game where genre = ?;";

        return jdbcTemplate.query(sql, new GameMapper(), gameId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public Game add(Game game) {

        final String sql = "insert into game (title, developer, genre, year_released, platform, region) "
                + " values (?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getTitle());
            ps.setString(2, game.getDeveloper());
            ps.setString(3, game.getGenre());
            ps.setInt(4, game.getYearReleased());
            ps.setString(5, game.getPlatform());
            ps.setString(6, game.getRegion().getDisplayName());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        game.setGameId(keyHolder.getKey().intValue());
        return game;
    }

    @Override
    public boolean update(Game game) {

        final String sql = "update game set " +
                "title = ?, " +
                "developer = ?, " +
                "genre = ?, " +
                "year_released = ?, " +
                "platform = ?, " +
                "region = ?"
                + "where game_id = ?;";

        return jdbcTemplate.update(sql,
                game.getTitle(),
                game.getDeveloper(),
                game.getGenre(),
                game.getYearReleased(),
                game.getPlatform(),
                game.getRegion()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int gameId) {

        return jdbcTemplate.update("delete from game where game_id = ?;", gameId) > 0;
    }
}
