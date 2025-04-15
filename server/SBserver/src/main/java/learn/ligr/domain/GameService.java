package learn.ligr.domain;

import learn.ligr.data.GameRepository;
import learn.ligr.models.Game;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class GameService {
    private final GameRepository repository; // required data dependency

    GameService(GameRepository repository) { // constructor with dependency
        this.repository = repository;
    }

	List<Game> findAll() { // pass-through to repository
        return repository.findAll();
    }

	List<Game> findByGenre(String genre) { // pass-through to repository
        return repository.findByGenre(genre);
    }

	Game findById(int gameId) { // pass-through to repository
        return repository.findById(gameId);
    }

	Result<Game> add(Game game){ // pass-through to repository
        Result<Game> result = new Result<>();
        int currentYear = Year.now().getValue();
        if (game.getYearReleased() > currentYear) {
            result.addMessage("Year released cannot be in the future", ResultType.INVALID);
        }
        if (game.getGameId() != 0) {
            result.addMessage("gameId cannot be set for 'add' operation", ResultType.INVALID);
        }
        if (!result.isSuccess()) {
            return result;
        }

        game = repository.add(game);
        result.setPayload(game);
        return result;
    }

	Result<Game> update(Game game){ // pass-through to repository
        Result<Game> result = new Result<>();
        int currentYear = Year.now().getValue();
        if (game.getYearReleased() > currentYear) {
            result.addMessage("Year released cannot be in the future", ResultType.INVALID);
        }
        if (game.getGameId() <= 0) {
            result.addMessage("gameId must be set for `update` operation", ResultType.INVALID);
        }
        if (!result.isSuccess()) {
            return result;
        }

        if (!repository.update(game)) {
            String msg = String.format("gameId: %s, not found", game.getGameId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

	boolean deleteById(int gameId){ // pass-through to repository
        return repository.deleteById(gameId);
    }
}
