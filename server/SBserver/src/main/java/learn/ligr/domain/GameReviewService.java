package learn.ligr.domain;

import learn.ligr.data.GameReviewRepository;
import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Year;
import java.util.List;

@Service
public class GameReviewService {
    private final GameReviewRepository repository;

    public GameReviewService(GameReviewRepository repository) {
        this.repository = repository;
    }

    public List<GameReview> findAll() { // pass-through to repository
        return repository.findAll();
    }

    public List<GameReview> findByGame(Game game) { // pass-through to repository
        return repository.findByGame(game);
    }

    public List<GameReview> findByUser(User user) { // pass-through to repository
        return repository.findByUser(user);
    }

    public GameReview findById(int gameReviewId) { // pass-through to repository
        return repository.findById(gameReviewId);
    }

    public Result<GameReview> add(GameReview gameReview) { // pass-through to repository
        Result<GameReview> result = new Result<>();
        if (gameReview.getGameReviewId() != 0) {
            result.addMessage("gameReviewId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        gameReview = repository.add(gameReview);
        result.setPayload(gameReview);
        return result;
    }

    public Result<GameReview> update(GameReview gameReview) { // pass-through to repository
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

    public boolean deleteById(int gameReviewId) { // pass-through to repository
        return repository.deleteById(gameReviewId);
    }
    public double findGameReviewAverage(int gameId){ return repository.findGameReviewAverage(gameId); }
}
