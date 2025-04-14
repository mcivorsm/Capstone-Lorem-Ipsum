package models;

import java.util.Date;

public class Profile {
    int profileId;
    int favoriteGameId;
    Date dateJoined;
    String region;
    String profileDescription;
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
