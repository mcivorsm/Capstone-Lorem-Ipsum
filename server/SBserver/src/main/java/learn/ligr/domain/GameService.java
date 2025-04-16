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

	public List<Game> findAll() { // pass-through to repository
        return repository.findAll();
    }

    public List<Game> findByGenre(String genre) { // pass-through to repository
        return repository.findByGenre(genre);
    }

    public Game findById(int gameId) { // pass-through to repository
        return repository.findById(gameId);
    }

    public Result<Game> add(Game game){ // pass-through to repository
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

    public Result<Game> update(Game game){ // pass-through to repository
        Result<Game> result = new Result<>();
        int currentYear = Year.now().getValue();
        if (game.getYearReleased() > currentYear) {
            result.addMessage("Year released cannot be in the future", ResultType.INVALID);
        }
        if (game.getGameId() <= 0) {
            result.addMessage("gameId must be set for `update` operation", ResultType.INVALID);
        }

        if(game.getGameId() == 1 || game.getGameId() == 2){
            String msg = String.format("gameId: %s is a reserved game, and cannot be edited.", game.getGameId());
            result.addMessage(msg, ResultType.INVALID);
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

    public Result<Game> deleteById(int gameId){
        Result<Game> result = new Result<>();
        if(gameId == 1 || gameId == 2){
            String msg = String.format("gameId: %s is a reserved game, and cannot be deleted.", gameId);
            result.addMessage(msg, ResultType.INVALID);
        }

        if(result.isSuccess()){
            result.setPayload(repository.findById(gameId));
            repository.deleteById(gameId);
        }
        return result;
    }
}
