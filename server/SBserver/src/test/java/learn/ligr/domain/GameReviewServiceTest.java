package learn.ligr.domain;

import learn.ligr.data.GameReviewRepository;
import learn.ligr.models.GameReview;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GameReviewServiceTest {

    @Autowired
    GameReviewService service;

    @MockBean
    GameReviewRepository repository;

    @Test
    void shouldNotAddWhenIdSet() {
        GameReview review = makeReview();
        review.setGameReviewId(42);

        Result<GameReview> result = service.add(review);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldAddWhenValid() {
        GameReview review = makeReview();
        review.setGameReviewId(0);

        when(repository.add(review)).thenReturn(review);

        Result<GameReview> result = service.add(review);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(review, result.getPayload());
    }

    @Test
    void shouldNotUpdateWhenIdNotSet() {
        GameReview review = makeReview();
        review.setGameReviewId(0);

        Result<GameReview> result = service.update(review);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdateFails() {
        GameReview review = makeReview();
        review.setGameReviewId(1);

        when(repository.update(review)).thenReturn(false);

        Result<GameReview> result = service.update(review);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldUpdateWhenValid() {
        GameReview review = makeReview();
        review.setGameReviewId(1);

        when(repository.update(review)).thenReturn(true);

        Result<GameReview> result = service.update(review);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    GameReview makeReview(){
        GameReview gr = new GameReview();
        gr.setGameReviewId(1);
        gr.setGame(null);
        gr.setUser(null);
        gr.setReviewText("Surprisingly addictive! Great controls and fast-paced action.");
        gr.setRating(4.5);

        return gr;
    }
}