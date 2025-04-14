package data;

import models.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfileJdbcTemplateRepository implements ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProfileJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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
