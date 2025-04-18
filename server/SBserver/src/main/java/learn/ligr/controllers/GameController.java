package learn.ligr.controllers;

import learn.ligr.domain.GameService;
import learn.ligr.domain.Result;
import learn.ligr.models.Game;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    public List<Game> findAll() { // handles get route
        return service.findAll();
    }

	@GetMapping("/{gameId}")
    public Game findById(@PathVariable int gameId) { // handles get route find by id
        return service.findById(gameId);
    }

    @GetMapping("genre/{genre}")
    public List<Game> findByGenre(@PathVariable String genre) { // handles get route find by genre
        return service.findByGenre(genre);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid Game game, BindingResult bindingResult) { // handles post route
        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<Game> result = service.add(game);
        if(result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{gameId}")
    public ResponseEntity<Object> update(@PathVariable int gameId, @RequestBody @Valid Game game, BindingResult bindingResult) { // handles put route
        // if url gameId doesn't match request body game's gameId
        if (gameId != game.getGameId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<Game> result = service.update(game);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteById(@PathVariable int gameId) { // handles delete route
        if (service.deleteById(gameId).isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
