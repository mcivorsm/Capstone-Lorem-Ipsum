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
    void shouldNotAddWhenUsernameTooLong() {
        User user = makeUser();
        user.setId(0);
        user.setUsername("a".repeat(51));
        user.setPasswordHash("abc123!@#");

        assertThrows(ValidationException.class, () -> service.add(user));
    }


    @Test
    void shouldNotAddWhenUsernameInvalid() {
        User user = makeUser();
        user.setId(0);
        user.setUsername(""); // invalid username
        user.setPasswordHash("abc123!@#"); // valid password
        assertThrows(ValidationException.class, () -> service.add(user));
    }

    @Test
    void shouldNotAddWhenPasswordTooShort() {
        User user = makeUser();
        user.setId(0);
        user.setPasswordHash("a1!");

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

    @Test
    void shouldNotUpdateWhenDuplicateUser() {
        User user = makeUser();

        when(repository.findAll()).thenReturn(List.of(user));

        Result<User> result = service.update(user);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("no change"));
    }

    @Test
    void shouldNotUpdateWhenUsernameTaken() {
        User user = makeUser();
        user.setUsername("taken_user");

        User other = makeUser();
        other.setId(99); // simulate different user with same username
        other.setUsername("taken_user");
        other.setEmail("different@email.com");

        when(repository.findAll()).thenReturn(List.of(other));

        Result<User> result = service.update(user);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("username"));
    }

    @Test
    void shouldNotUpdateWhenEmailTaken() {
        User user = makeUser();
        user.setEmail("used@email.com");

        User other = makeUser();
        other.setId(99);
        other.setEmail("used@email.com");
        other.setUsername("different_username");

        when(repository.findAll()).thenReturn(List.of(other));

        Result<User> result = service.update(user);
        assertEquals(ResultType.INVALID, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("email"));
    }

    @Test
    void shouldNotUpdateWhenUserNotFound() {
        User user = makeUser();

        when(repository.findAll()).thenReturn(List.of());
        when(repository.update(user)).thenReturn(false);

        Result<User> result = service.update(user);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        assertTrue(result.getMessages().get(0).toLowerCase().contains("not found"));
    }

    @Test
    void shouldNotUpdateReservedUserId(){
        User user = makeUser();
        user.setId(1);

        assertFalse(service.update(user).isSuccess());
    }

    @Test
    void shouldUpdateWhenValid() {
        User user = makeUser();

        when(repository.findAll()).thenReturn(List.of());
        when(repository.update(user)).thenReturn(true);

        Result<User> result = service.update(user);
        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteReservedUserId(){
        assertFalse(service.deleteById(1, 1).isSuccess());
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