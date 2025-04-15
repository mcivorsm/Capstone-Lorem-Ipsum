package learn.ligr.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

public class Profile {
    @NotNull(message = "Need Profile ID.")
    int profileId;


    Game favoriteGame;

    @NotNull(message = "Date Joined.")
    @PastOrPresent(message = "Date joined cannot be in the future.")
    Date dateJoined;

    Region region;

    String profileDescription;


    String preferredGenre;

    public Profile(){
        this.dateJoined = new Date();
    }
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public Game getFavoriteGame() {
        return favoriteGame;
    }

    public void setFavoriteGame(Game favoriteGame) {
        this.favoriteGame = favoriteGame;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getPreferredGenre() {
        return preferredGenre;
    }

    public void setPreferredGenre(String preferredGenre) {
        this.preferredGenre = preferredGenre;
    }


}
