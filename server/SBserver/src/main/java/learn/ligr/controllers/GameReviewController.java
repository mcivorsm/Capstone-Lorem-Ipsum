package learn.ligr.controllers;

import learn.ligr.domain.GameReviewService;
import learn.ligr.domain.GameService;
import learn.ligr.domain.Result;
import learn.ligr.domain.UserService;
import learn.ligr.models.Game;
import learn.ligr.models.GameReview;
import learn.ligr.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/gameReview")
public class GameReviewController {
    private final GameReviewService gameReviewService;
    private final GameService gameService;
    private final UserService userService;

    public GameReviewController(GameReviewService gameReviewService, GameService gameService, UserService userService) {
        this.gameReviewService = gameReviewService;
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("/{gameReviewId}")
    public GameReview findById(@PathVariable int gameReviewId) { // handles get route find by id
        return gameReviewService.findById(gameReviewId);
    }

    @GetMapping("/game/{gameId}")
    public List<GameReview> findByGame(@PathVariable int gameId) { // handles get route find by game
        Game game = gameService.findById(gameId);
        return gameReviewService.findByGame(game);
    }

    @GetMapping("/user/{userId}")
    public List<GameReview> findByUser(@PathVariable int userId) { // handles get route find by user
        User user = userService.findById(userId);
        return gameReviewService.findByUser(user);
    }

    @GetMapping("/game/{gameId}/avg")
    public double findGameReviewAverage(@PathVariable int gameId){
        return gameReviewService.findGameReviewAverage(gameId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid GameReview gameReview, BindingResult bindingResult) { // handles post route
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        gameReview.setUser(user);
        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<GameReview> result = gameReviewService.add(gameReview);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{gameReviewId}")
    public ResponseEntity<Object> update(@PathVariable int gameReviewId, @RequestBody @Valid GameReview gameReview, BindingResult bindingResult) { // handles put route
        // if url gameReviewId doesn't match request body gameReview's gameReviewId
        if (gameReviewId != gameReview.getGameReviewId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<GameReview> result = gameReviewService.update(gameReview);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{gameReviewId}")
    public ResponseEntity<Void> deleteById(@PathVariable int gameReviewId) { // handles delete route
        if (gameReviewService.deleteById(gameReviewId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/testerino")
    public List<GameReview> test(){
        User user = userService.findById(2);

        List<GameReview> result = gameReviewService.findByUser(user);
        return result;
    }
}
