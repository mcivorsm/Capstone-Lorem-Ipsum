package models;

import java.util.Objects;

public class GameReview {
    int gameReviewId;
    int gameId;
    int userId;
    String reviewText;
    int rating;

    public int getGameReviewId() {
        return gameReviewId;
    }

    public void setGameReviewId(int gameReviewId) {
        this.gameReviewId = gameReviewId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameReview that = (GameReview) o;
        return gameId == that.gameId && userId == that.userId && Objects.equals(reviewText, that.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, userId, reviewText);
    }
}
