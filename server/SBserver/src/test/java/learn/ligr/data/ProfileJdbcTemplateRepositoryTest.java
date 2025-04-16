package learn.ligr.data;

import learn.ligr.models.Profile;
import learn.ligr.models.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ProfileJdbcTemplateRepositoryTest {

    @Autowired
    ProfileJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<Profile> profiles = repository.findAll();
        assertTrue(profiles.size() > 0);
    }

    @Test
    void shouldFindById() {
        Profile profile = makeProfile();
        profile.setProfileId(2);
        assertEquals(profile, repository.findById(2));
    }

    @Test
    void shouldNotFindById() {
        assertNull(repository.findById(100));
    }

    @Test
    void shouldAdd() {
        assertEquals(makeProfile(), repository.add(makeProfile()));

        assertTrue(repository.findAll().size() >= 4 && repository.findAll().size() <= 6);
    }

    @Test
    void shouldUpdate() {
        Profile profile = makeProfile();
        profile.setProfileId(2);
        profile.setPreferredGenre("Rhythm Games");

        assertTrue(repository.update(profile));
    }

    @Test
    void shouldNotUpdate() {
        Profile profile = makeProfile();
        profile.setProfileId(100);

        assertFalse(repository.update(profile));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteById() {
        assertFalse(repository.deleteById(100));
    }

    public static Profile makeProfile(){
        Profile profile = new Profile();
        profile.setProfileId(7);
        profile.setFavoriteGame(GameJdbcTemplateRepositoryTest.makeGame());
        profile.setDateJoined(Date.valueOf("2023-01-15"));
        profile.setRegion(Region.JP);
        profile.setProfileDescription("Long-time JRPG fan, loves turn-based combat.");
        profile.setPreferredGenre("RPG");
        return profile;
    }
}