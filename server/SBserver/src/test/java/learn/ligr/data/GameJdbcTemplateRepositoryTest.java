package learn.ligr.data;

import learn.ligr.models.Game;
import learn.ligr.models.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GameJdbcTemplateRepositoryTest {

    @Autowired
    GameJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void shouldFindAll() {
        List<Game> games = repository.findAll();
        assertNotNull(games);
    }

    @Test
    void shouldFindByGenre() {
        List<Game> games = repository.findByGenre("RPG");
        assertEquals(makeGame(), games.get(0));
    }

    @Test
    void shouldNotFindByGenre() {
        List<Game> games = repository.findByGenre("Puzzle");
        assertEquals(0, games.size());
    }

    @Test
    void shouldFindById() {
        Game game = repository.findById(2);
        assertEquals(makeGame(), game);
    }

    @Test
    void shouldNotFindById() {
        Game game = repository.findById(100);
        assertNull(game);
    }

    @Test
    void shouldAdd() {
        assertEquals(makeGame(), repository.add(makeGame()));

        assertTrue(repository.findAll().size() >= 6 && repository.findAll().size() <= 7);
    }

    @Test
    void shouldUpdate() {
        Game update = makeGame();
        update.setTitle("Test Game");

        assertTrue(repository.update(update));
    }

    @Test
    void shouldNotpdate() {
        Game update = makeGame();
        update.setTitle("Test Game");
        update.setGameId(100);

        assertFalse(repository.update(update));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(2));
    }

    @Test
    void shouldNotDeleteById() {
        assertFalse(repository.deleteById(100));
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