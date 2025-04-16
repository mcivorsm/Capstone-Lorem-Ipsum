package learn.ligr.domain;

import learn.ligr.data.GameRepository;
import learn.ligr.models.Game;
import learn.ligr.models.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GameServiceTest {

    @Autowired
    GameService service;

    @MockBean
    GameRepository repository;

    @Test
    void shouldNotAddWhenYearInFuture() {
        Game game = makeGame();
        game.setGameId(0);
        game.setYearReleased(Year.now().getValue() + 1);

        Result<Game> result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("future"));
    }

    @Test
    void shouldNotAddWhenIdSet() {
        Game game = makeGame();
        game.setGameId(99);

        Result<Game> result = service.add(game);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldAddWhenValid() {
        Game game = makeGame();
        game.setGameId(0);

        when(repository.add(game)).thenReturn(game);

        Result<Game> result = service.add(game);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(game, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenYearInFuture() {
        Game game = makeGame();
        game.setGameId(1);
        game.setYearReleased(Year.now().getValue() + 1);

        Result<Game> result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenIdNotSet() {
        Game game = makeGame();
        game.setGameId(0);

        Result<Game> result = service.update(game);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldReturnNotFoundWhenUpdateFails() {
        Game game = makeGame();
        game.setGameId(100);

        when(repository.update(game)).thenReturn(false);

        Result<Game> result = service.update(game);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldNotUpdateReservedGameId(){
        Game update = makeGame();
        update.setGameId(2);
        assertFalse(repository.update(update));
    }

    @Test
    void shouldUpdateWhenValid() {
        Game game = makeGame();
        game.setGameId(3);

        when(repository.update(game)).thenReturn(true);

        Result<Game> result = service.update(game);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteReservedGames(){
        assertFalse(service.deleteById(1).isSuccess());
        assertFalse(service.deleteById(2).isSuccess());
    }

    Game makeGame(){
        Game game = new Game();
        game.setGameId(2);
        game.setTitle("Chrono Break");
        game.setDeveloper("Square Enix");
        game.setGenre("RPG");
        game.setYearReleased(2023);
        game.setPlatform("PC");
        game.setRegion(Region.JP);

        return game;
    }
}