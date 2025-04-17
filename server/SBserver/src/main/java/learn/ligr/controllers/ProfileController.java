package learn.ligr.controllers;

import learn.ligr.domain.ProfileService;
import learn.ligr.domain.Result;
import learn.ligr.models.Game;
import learn.ligr.models.Profile;
import learn.ligr.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    ProfileService profileService;



    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @PutMapping("/edit" )
    public ResponseEntity<?> edit(@RequestBody @Valid Profile profile, BindingResult bindingResult){

        // automatic validation
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Result<Profile> result = profileService.update(profile);
        if(result.isSuccess()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ErrorResponse.build(result);
    }

    @GetMapping("/{profileId}")
    public Profile viewProfile(@PathVariable int profileId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return profileService.findById(profileId);
    }

    @GetMapping("/")
    public Profile viewProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return profileService.findById(user.getProfile().getProfileId());
    }
}
