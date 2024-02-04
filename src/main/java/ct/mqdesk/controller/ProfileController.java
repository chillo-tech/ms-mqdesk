package ct.mqdesk.controller;

import ct.mqdesk.entity.Profile;
import ct.mqdesk.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProfileController {

    private ProfileService profileService;

    @PostMapping(path = "sign-up")
    public void inscription(@RequestBody final Profile profile) {
        ProfileController.log.info("Inscription");
        this.profileService.inscription(profile);
    }

    @PostMapping(path = "password")
    public void sendPassword(@RequestBody final Map<String, String> params) {
        ProfileController.log.info("password");
        this.profileService.resetPassword(params);
    }

}
