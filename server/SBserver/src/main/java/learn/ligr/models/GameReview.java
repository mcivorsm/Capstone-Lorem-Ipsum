package learn.ligr.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class GameReview {
    @NotNull(message = "Need Game Review ID.")
    int gameReviewId;

    @NotNull(message = "Need Game ID.")
    Game game;

    @NotNull(message = "Need User ID.")
    User user;

    @NotBlank(message = "Review cannot be blank.")
    String reviewText;

    @NotNull(message = "Need Rating.")
    double rating;

    public int getGameReviewId() {
        return gameReviewId;
    }

    public void setGameReviewId(int gameReviewId) {
        this.gameReviewId = gameReviewId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameReview that = (GameReview) o;
        return Objects.equals(game,that.game)  && Objects.equals(user, that.user)  && Objects.equals(reviewText, that.reviewText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(game, user, reviewText);
    }
}
