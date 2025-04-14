package models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

public class Profile {
    @NotNull(message = "Need Profile ID.")
    int profileId;

    @NotNull(message = "Need Favorite Game ID.")
    int favoriteGameId;

    @NotNull(message = "Date Joined.")
    @PastOrPresent(message = "Date joined cannot be in the future.")
    Date dateJoined;

    @NotBlank(message = "Region is required")
    @Size(max = 5, message = "Max Length of 5 characters")
    String region;

    String profileDescription;

    @NotBlank(message = "Preferred Genre is required.")
    String preferredGenre;

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getFavoriteGameId() {
        return favoriteGameId;
    }

    public void setFavoriteGameId(int favoriteGameId) {
        this.favoriteGameId = favoriteGameId;
    }

    public Date getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
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
