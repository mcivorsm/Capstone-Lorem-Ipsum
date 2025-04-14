package data;

import models.Profile;

import java.util.List;

public interface ProfileRepository {
    List<Profile> findAll(); // finds all profiles in the database, does not need to be public
	Profile findById(int profileId); // finds a profile by id
	Profile add(Profile profile); // create a profile
	boolean update(Profile profile); // update a profile
	boolean deleteById(int profileId); // delete a profile by id
}
