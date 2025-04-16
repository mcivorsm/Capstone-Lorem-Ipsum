package learn.ligr.data;

import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;

import java.util.List;

public interface GameReviewRepository {
    List<GameReview> findAll(); // finds all reviews in the database, does not need to be public
	List<GameReview> findByGame(Game game); // finds all reviews of a game
	List<GameReview> findByUser(User user); // finds all reviews of a user
	GameReview findById(int gameReviewId); // finds a review by id
	GameReview add(GameReview gameReview); // create a review
	boolean update(GameReview gameReview); // update a review
	boolean deleteById(int gameReviewId); // delete a review by id
	double findGameReviewAverage(int gameId); //return the average rating of a game
}
