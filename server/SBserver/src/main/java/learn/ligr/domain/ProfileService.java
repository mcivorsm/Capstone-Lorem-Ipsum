package learn.ligr.domain;

import learn.ligr.data.ProfileRepository;
import learn.ligr.models.Profile;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
ProfileRepository profileRepository;

ProfileService(ProfileRepository profileRepository){
    this.profileRepository = profileRepository;
}

    public Result<Profile> update(Profile profile){
        Result<Profile> result = new Result<>();
        if(profileRepository.update(profile)){
            result.setPayload(profile);
            return result;
        }
        result.addMessage("Update failed.", ResultType.INVALID);
        return  result;
    }

    public Profile findById(int profileId){
        return profileRepository.findById(profileId);
    }

}
