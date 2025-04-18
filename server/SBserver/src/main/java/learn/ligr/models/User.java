package learn.ligr.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class User implements UserDetails{
    @NotNull(message = "Need ID.")
    int userId;

    Profile profile;

    @NotBlank(message = "Username is required.")
    @Size(max = 25, message = "Username cannot be greater than 50 characters.")
    String username;
    @NotBlank(message = "Email is required.")
    @Email(message = "Email must be a valid email address.")
    String email;

    String passwordHash;
    boolean isAdmin;
    private static final String AUTHORITY_PREFIX = "ROLE_";
    private List<SimpleGrantedAuthority> roles = new ArrayList<>();

    public User( int userId, Profile profile, String username, String email, boolean isAdmin) {
        this.userId = userId;
        this.profile = profile;
        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    //used for first user parse on login from JWT -> to user. We need to decide where the profile user union is happening.
    //with our current setup have a Profile object as a dependency, i would have to either inject some profile service into a config file,
    //or we perform another query at controller or service level to assign a profile, or profile is just null until something needs to change
    public User( int userId, String username, String email, boolean isAdmin) {
        this.userId = userId;

        this.username = username;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    //Add user initally
    public User(String username, String passwordHash, String email){
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;

        this.profile = new Profile();
    }

    public User(){}

    public int getId() {
        return userId;
    }

    public void setId(int id) {
        this.userId = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles.isEmpty()) {
            if (this.isAdmin) {
                roles.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + "ADMIN"));
            }
            roles.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + "USER"));
        }
        return roles;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            this.roles.add(new SimpleGrantedAuthority(authority.getAuthority()));
        }
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(passwordHash, user.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}