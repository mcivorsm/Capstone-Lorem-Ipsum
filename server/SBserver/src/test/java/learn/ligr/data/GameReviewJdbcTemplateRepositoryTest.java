package learn.ligr.data;

import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GameReviewJdbcTemplateRepositoryTest {

    @Autowired
    GameReviewJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<GameReview> reviews = repository.findAll();
        assertTrue(reviews.size() > 0);
    }

    @Test
    void shouldFindByGame() {
        List<GameReview> reviews = repository.findByGame(GameJdbcTemplateRepositoryTest.makeGame());
        assertTrue(reviews.size() > 0);
    }

    @Test
    void shouldNotFindByGame() {

        Game game = GameJdbcTemplateRepositoryTest.makeGame();
        game.setGameId(100);
        List<GameReview> reviews = repository.findByGame(game);
        assertEquals(0, reviews.size());
    }

    @Test
    void shouldFindByUser() {
        List<GameReview> reviews = repository.findByUser(UserJdbcTemplateRepositoryTest.makeUser());
        assertTrue(reviews.size() > 0);
    }

    @Test
    void shouldNotFindByUser() {
        User user = UserJdbcTemplateRepositoryTest.makeUser();
        user.setId(100);
        List<GameReview> reviews = repository.findByUser(user);
        assertEquals(0, reviews.size());
    }

    @Test
    void shouldFindById() {
        GameReview expected = makeReview();
        GameReview actual = repository.findById(1);
        assertEquals(makeReview(), repository.findById(1));
    }

    @Test
    void shouldNotFindById() {
        assertNull(repository.findById(100));
    }

    @Test
    void shouldAdd() {
        assertEquals(makeReview(), repository.add(makeReview()));
        assertTrue(repository.findAll().size() >= 4 && repository.findAll().size() <= 5);
    }

    @Test
    void shouldUpdate() {
        GameReview gr = makeReview();
        gr.setRating(1.0);

        assertTrue(repository.update(gr));
    }

    @Test
    void shouldNotUpdate() {
        GameReview gr = makeReview();
        gr.setGameReviewId(100);

        assertFalse(repository.update(gr));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(1));
    }

    @Test
    void shouldNotDeleteById() {
        assertFalse(repository.deleteById(100));
    }

    @Test
    void shouldReturnRatingAverage(){
        assertTrue(repository.findGameReviewAverage(3) == 3.2 || repository.findGameReviewAverage(3) == 3.9 );
    }

    @Test
    void shouldNotReturnRatingAverage(){
        assertEquals(0, repository.findGameReviewAverage(100));
    }

    private GameReview makeReview(){
        GameReview gr = new GameReview();
        gr.setGameReviewId(1);
        gr.setGame(GameJdbcTemplateRepository.DEFAULT_GAME);
        gr.setUser(UserJdbcTemplateRepositoryTest.makeUser());
        gr.setReviewText("Surprisingly addictive! Great controls and fast-paced action.");
        gr.setRating(4.0);

        return gr;
    }
}