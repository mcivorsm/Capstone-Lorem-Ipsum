package models;

import java.util.Objects;

import java.time.LocalDate;


public class User{
    int id;
    int profileId;

    @NotBlank(message = "Customer name is required.")
    @Size(max = 50, message = "Customer name cannot be greater than 50 characters.")
    String username;
    @NotBlank(message = "Customer name is required.")
    @Size(max = 50, message = "Customer name cannot be greater than 50 characters.")
    String email;
    String passwordHash;
    boolean isAdmin;


    public User(int profileId, int id, String username, String email, String passwordHash, boolean isAdmin) {
        this.profileId = profileId;
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isAdmin = isAdmin;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
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
        return Objects.equals(username, user.username) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email);
    }
}