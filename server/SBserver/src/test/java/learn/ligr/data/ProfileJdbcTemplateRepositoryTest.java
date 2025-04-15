package learn.ligr.data;

import learn.ligr.models.Profile;
import learn.ligr.models.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProfileJdbcTemplateRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    public static Profile makeProfile(){
        Profile profile = new Profile();
        profile.setProfileId(2);
        profile.setFavoriteGame(GameJdbcTemplateRepositoryTest.makeGame());
        profile.setDateJoined(Date.valueOf("2023-01-15"));
        profile.setRegion(Region.JP);
        profile.setProfileDescription("Long-time JRPG fan, loves turn-based combat.");
        profile.setPreferredGenre("RPG");
        return profile;
    }
}