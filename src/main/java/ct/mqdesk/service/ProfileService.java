package ct.mqdesk.service;

import ct.mqdesk.entity.MQDeskAccount;
import ct.mqdesk.entity.Profile;
import ct.mqdesk.enums.Role;
import ct.mqdesk.repository.ProfileRepository;
import ct.mqdesk.service.brevo.BrevoService;
import ct.mqdesk.service.chcontact.ChContactClient;
import ct.mqdesk.service.rabbitmq.RabbitMQService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ProfileService implements UserDetailsService {
    private MQDeskAccountService mqDeskAccountService;
    private ProfileRepository profileRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private BrevoService brevoService;
    private RabbitMQService rabbitMQService;
    private ChContactClient chContactClient;

    public void inscription(Profile profile) {
        if (!profile.getEmail().contains("@")) {
            throw new IllegalArgumentException("Votre mail invalide");
        }
        if (!profile.getEmail().contains(".")) {
            throw new IllegalArgumentException("Votre mail invalide");
        }

        final Optional<Profile> profileOptional = this.profileRepository.findByEmail(profile.getEmail());
        if (profileOptional.isPresent()) {
            throw new IllegalArgumentException("Votre mail est déjà utilisé");
        }
        final String password = RandomStringUtils.randomAlphanumeric(10);
        final String encodedPassword = this.passwordEncoder.encode(password);

        profile.setRole(Role.CUSTOMER);
        profile.setActive(true);
        profile = this.profileRepository.save(profile);

        MQDeskAccount mqDeskAccount = new MQDeskAccount();
        mqDeskAccount.setProfile(profile);
        String username = profile.getEmail().split("@")[0];
        username = username.replaceAll("[^A-Za-z0-9]", "-");
        username = String.format("%s-%s", username, RandomStringUtils.random(4, false, true));
        mqDeskAccount.setUsername(username);
        mqDeskAccount.setPassword(encodedPassword);
        mqDeskAccount = this.mqDeskAccountService.save(mqDeskAccount);

        this.rabbitMQService.createCurstomerVhost(mqDeskAccount, password);
        this.brevoService.sendIncriptionEmails(mqDeskAccount, password);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return null;
    }

    public void newPassword(final Map<String, String> params) {
        final String email = params.get("email");
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Votre mail invalide");
        }
        if (!email.contains(".")) {
            throw new IllegalArgumentException("Votre mail invalide");
        }
        MQDeskAccount mqDeskAccount = this.mqDeskAccountService.readUserAccount(email);
        final String password = RandomStringUtils.randomAlphanumeric(10);
        final String encodedPassword = this.passwordEncoder.encode(password);
        mqDeskAccount.setPassword(encodedPassword);
        mqDeskAccount = this.mqDeskAccountService.save(mqDeskAccount);

        this.rabbitMQService.createUser(mqDeskAccount, password);
        this.brevoService.sendPassword(mqDeskAccount, password);
    }

    public void sendContactMessage(final Map<String, String> params) {
        this.brevoService.sendMessage(params);
        this.chContactClient.sendMessage(params);
    }
}
