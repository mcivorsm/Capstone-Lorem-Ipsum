package data;

import models.Profile;

import java.util.List;

public class ProfileJdbcTemplateRepository implements ProfileRepository{
    @Override
    public List<Profile> findAll() {
        return List.of();
    }

    @Override
    public Profile findById(int profileId) {
        return null;
    }

    @Override
    public Profile add(Profile profile) {
        return null;
    }

    @Override
    public boolean update(Profile profile) {
        return false;
    }

    @Override
    public boolean deleteById(int profileId) {
        return false;
    }
}
