package learn.ligr.data.mappers;

import learn.ligr.models.Game;
import learn.ligr.models.Region;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet resultSet, int i) throws SQLException {
        Game game = new Game();

        System.out.println("id: " + resultSet.getInt("game_id"));
        game.setGameId(resultSet.getInt("game_id"));

        System.out.println("title: " + resultSet.getString("title"));
        game.setTitle(resultSet.getString("title"));

        game.setDeveloper(resultSet.getString("developer"));

        game.setGenre(resultSet.getString("genre"));

        game.setYearReleased(resultSet.getInt("year_released"));

        game.setPlatform(resultSet.getString("platform"));

        game.setRegion(Region.valueOf(resultSet.getString("region")));

        return game;
    }
}
