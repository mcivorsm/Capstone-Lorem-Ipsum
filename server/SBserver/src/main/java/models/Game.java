package models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class Game {
    @NotNull(message = "Need Game ID.")
    int gameId;
    @NotBlank(message = "Title is required.")
    String title;

    @NotBlank(message = "Developer is required.")
    String developer;

    @NotBlank(message = "Genre is required.")
    String genre;
    int yearReleased;

    @NotBlank(message = "Platform is required.")
    String platform;

    @NotBlank(message = "Region is required.")
    Region region;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(title, game.title) && Objects.equals(developer, game.developer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, developer);
    }
}
