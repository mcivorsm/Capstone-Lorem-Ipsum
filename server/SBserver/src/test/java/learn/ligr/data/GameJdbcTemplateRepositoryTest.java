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
        assertTrue(games.size() > 0);
    }

    @Test
    void shouldFindByGenre() {
        List<Game> games = repository.findByGenre("RPG");
        assertEquals(makeGame(), games.get(0));
    }

    @Test
    void shouldNotFindByGenre() {
        List<Game> games = repository.findByGenre("Bad Genre");
        assertEquals(0, games.size());
    }

    @Test
    void shouldFindById() {
        Game game = repository.findById(3);
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

        assertTrue(repository.findAll().size() >= 7 && repository.findAll().size() <= 8);
    }

    @Test
    void shouldUpdate() {
        Game update = makeGame();
        update.setTitle("Test Game");
        update.setGenre("Puzzle");

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

    public static Game makeGame(){
        Game game = new Game();
        game.setGameId(3);
        game.setTitle("Chrono Break");
        game.setDeveloper("Square Enix");
        game.setGenre("RPG");
        game.setYearReleased(2023);
        game.setPlatform("PC");
        game.setRegion(Region.JP);

        return game;
    }

}