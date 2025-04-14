package data;

import models.Game;

import java.util.List;

public interface GameRepository {
    List<Game> findAll(); // finds all games in the database, does not need to be public
	List<Game> findByGenre(); // finds all games of a genre
	Game findById(int gameId); // finds a game by id
	Game add(Game game); // create a game
	boolean update(Game game); // update a game
	boolean deleteById(int gameId); // delete a game by id
}
