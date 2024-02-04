package ct.mqdesk.service;

import ct.mqdesk.entity.ClientApplication;
import ct.mqdesk.entity.Token;
import ct.mqdesk.repository.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class TokenService {
    private TokenRepository tokenRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public void generate(final ClientApplication clientApplication) {
        final String prefix = "mqdesk";
        final String suffix = RandomStringUtils.random(6, false, true);
        final String content = RandomStringUtils.random(50, true, true);
        final String clientToken = String.format("%s_%s_%s", prefix, content, suffix);
        TokenService.log.info(clientToken);
        final Token token = new Token();
        token.setValue(this.passwordEncoder.encode(clientToken));
        token.setActive(true);
        token.setCreation(LocalTime.now());
        token.setApplication(clientApplication);
        this.tokenRepository.save(token);

    }

    public Token findByValue(final String value) {
        final Optional<Token> optionalToken = this.tokenRepository.findByValue(value);
        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }
        return optionalToken.get();
    }

    public Token findByValueAndActive(final String value, final boolean active) {
        final Optional<Token> optionalToken = this.tokenRepository.findByValueAndActive(value, active);
        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Votre mail est déjà utilisé");
        }
        return optionalToken.get();
    }

    public Token findByApplicationNameAndActive(final String value, final boolean active) {
        final Optional<Token> optionalToken = this.tokenRepository.findByApplicationNameAndActive(value, active);
        if (optionalToken.isEmpty()) {
            throw new RuntimeException("Application Inconnue");
        }
        return optionalToken.get();
    }

}
