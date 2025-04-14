package data;

import models.Game;

import java.util.List;

public class GameJdbcTemplateRepository implements GameRepository {
    @Override
    public List<Game> findAll() {
        return List.of();
    }

    @Override
    public List<Game> findByGenre() {
        return List.of();
    }

    @Override
    public Game findById(int gameId) {
        return null;
    }

    @Override
    public Game add(Game game) {
        return null;
    }

    @Override
    public boolean update(Game game) {
        return false;
    }

    @Override
    public boolean deleteById(int gameId) {
        return false;
    }
}
