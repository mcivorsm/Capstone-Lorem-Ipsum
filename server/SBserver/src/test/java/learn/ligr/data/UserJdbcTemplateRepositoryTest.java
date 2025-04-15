package learn.ligr.data;

import learn.ligr.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcTemplateRepositoryTest {

    @Autowired
    UserJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() { knownGoodState.set(); }

    @Test
    void findAll() {
        List<User> users = repository.findAll();
        assertTrue(users.size() > 0);
    }

    @Test
    void shouldFindById() {
        assertEquals(makeUser(), repository.findById(2));
    }

    @Test
    void shouldNotFindById() {
        assertNull(repository.findById(100));
    }

    @Test
    void shouldFindByUsername() {
        assertEquals(makeUser(), repository.findByUsername("gamer_girl91"));
    }

    @Test
    void shouldNotFindByUsername() {
        assertNull(repository.findByUsername("bad username"));
    }

    @Test
    void shouldAdd() {
        assertEquals(makeUser(), repository.add(makeUser()));
        assertTrue(repository.findAll().size() >= 3 && repository.findAll().size() <= 6);
    }

    @Test
    void shouldUpdate() {
        User user = makeUser();
        user.setId(3);
        user.setUsername("testuser");
        user.setEmail("test@test.test");
        assertTrue(repository.update(user));
    }

    @Test
    void shouldNotUpdate() {
        User user = makeUser();
        user.setId(100);
        assertFalse(repository.update(user));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteById() {
        assertFalse(repository.deleteById(100));
    }

    public static User makeUser(){
        User user = new User();

        user.setId(2);
        user.setProfile(ProfileJdbcTemplateRepositoryTest.makeProfile());
        user.setEmail("gg91@example.com");
        user.setUsername("gamer_girl91");
        user.setPasswordHash("p@ssword123");
        user.setAdmin(false);

        return user;
    }
}