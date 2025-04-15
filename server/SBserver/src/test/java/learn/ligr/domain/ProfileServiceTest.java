package learn.ligr.domain;

import learn.ligr.data.ProfileRepository;
import learn.ligr.models.Game;
import learn.ligr.models.Profile;
import learn.ligr.models.Region;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProfileServiceTest {
    @Autowired
    ProfileService service;

    @MockBean
    ProfileRepository repository;

    @Test
    void shouldFindById() {
        Profile profile = makeProfile();

        when(repository.findById(1)).thenReturn(profile);

        Profile actual = service.findById(1);
        assertEquals(profile, actual);
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Profile profile = makeProfile();

        when(repository.update(profile)).thenReturn(false);

        Result<Profile> result = service.update(profile);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("update"));
    }

    @Test
    void shouldUpdateWhenValid() {
        Profile profile = makeProfile();

        when(repository.update(profile)).thenReturn(true);

        Result<Profile> result = service.update(profile);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(profile, result.getPayload());
    }

    Profile makeProfile(){
        Profile profile = new Profile();
        profile.setProfileId(2);
        profile.setFavoriteGame(null);
        profile.setDateJoined(Date.valueOf("2023-01-15"));
        profile.setRegion(Region.JP);
        profile.setProfileDescription("Long-time JRPG fan, loves turn-based combat.");
        profile.setPreferredGenre("RPG");
        return profile;
    }
}