package learn.ligr.domain;

import learn.ligr.data.GameReviewRepository;
import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;

import java.time.Year;
import java.util.List;

public class GameReviewService {
    private final GameReviewRepository repository;

    public GameReviewService(GameReviewRepository repository) {
        this.repository = repository;
    }

	List<GameReview> findAll() { // pass-through to repository
        return repository.findAll();
    }

    List<GameReview> findByGame(Game game) { // pass-through to repository
        return repository.findByGame(game);
    }

    List<GameReview> findByUser(User user) { // pass-through to repository
        return repository.findByUser(user);
    }

    GameReview findById(int gameReviewId) { // pass-through to repository
        return repository.findById(gameReviewId);
    }

    Result<GameReview> add(GameReview gameReview) { // pass-through to repository
        Result<GameReview> result = new Result<>();
        if (gameReview.getGameReviewId() != 0) {
            result.addMessage("gameReviewId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        gameReview = repository.add(gameReview);
        result.setPayload(gameReview);
        return result;
    }

    Result<GameReview> update(GameReview gameReview) { // pass-through to repository
        Result<GameReview> result = new Result<>();
        if (gameReview.getGameReviewId() <= 0) {
            result.addMessage("gameReviewId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(gameReview)) {
            String msg = String.format("gameReviewId: %s, not found", gameReview.getGameReviewId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    boolean deleteById(int gameReviewId) { // pass-through to repository
        return repository.deleteById(gameReviewId);
    }

}
