package learn.ligr.domain;

import learn.ligr.data.UserRepository;
import learn.ligr.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import javax.validation.ValidationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository repository;

    @Test
    void shouldNotAddWhenIdSet() {
        User user = makeUser();
        user.setId(99);

        Result<User> result = service.add(user);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("id"));
    }

    @Test
    void shouldNotAddWhenDuplicate() {
        User user = makeUser();
        user.setId(0);
        user.setPasswordHash("abc123!@#");

        when(repository.findAll()).thenReturn(List.of(user)); // simulate duplicate

        Result<User> result = service.add(user);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("exists"));
    }

    @Test
    void shouldNotAddWhenUsernameInvalid() {
        User user = makeUser();
        user.setId(0);
        user.setUsername(""); // invalid
        user.setPasswordHash("abc123!@#"); // valid password
        assertThrows(ValidationException.class, () -> service.add(user));
    }

    @Test
    void shouldNotAddWhenPasswordMissingDigit() {
        User user = makeUser();
        user.setId(0);
        user.setPasswordHash("abcdefg!");

        assertThrows(ValidationException.class, () -> service.add(user));
    }

    @Test
    void shouldNotAddWhenPasswordMissingLetter() {
        User user = makeUser();
        user.setId(0);
        user.setPasswordHash("12345678!");

        assertThrows(ValidationException.class, () -> service.add(user));
    }

    @Test
    void shouldNotAddWhenPasswordMissingSymbol() {
        User user = makeUser();
        user.setId(0);
        user.setPasswordHash("abc12345");

        assertThrows(ValidationException.class, () -> service.add(user));
    }

    @Test
    void shouldAddWhenValid() {
        User expected = makeUser();
        User arg = makeUser();
        arg.setId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<User> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    User makeUser(){
        User user = new User();

        user.setId(2);
        user.setProfile(null);
        user.setUsername("gamer_girl91");
        user.setEmail("gg91@example.com");
        user.setPasswordHash("p@ssword123");
        user.setAdmin(false);

        return user;
    }
}