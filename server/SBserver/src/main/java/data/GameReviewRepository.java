package data;

import models.GameReview;

import java.util.List;

public interface GameReviewRepository {
    List<GameReview> findAll(); // finds all reviews in the database, does not need to be public
	List<GameReview> findByGame(); // finds all reviews of a game
	List<GameReview> findByUser(); // finds all reviews of a user
	GameReview findById(int gameReviewId); // finds a review by id
	GameReview add(GameReview gameReview); // create a review
	boolean update(GameReview gameReview); // update a review
	boolean deleteById(int gameReviewId); // delete a review by id
}
