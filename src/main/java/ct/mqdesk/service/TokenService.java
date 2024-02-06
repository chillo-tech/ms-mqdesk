package ct.mqdesk.service;

import ct.mqdesk.entity.ClientApplication;
import ct.mqdesk.entity.Token;
import ct.mqdesk.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@Service
public class TokenService {
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final String clientApplicationToken;

    public TokenService(
            final TokenRepository tokenRepository,
            final BCryptPasswordEncoder passwordEncoder,
            @Value("${application.client.token: ''}") final String clientApplicationToken) {
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.clientApplicationToken = clientApplicationToken;
    }

    public void generate(final ClientApplication clientApplication) {
        final String prefix = RandomStringUtils.random(8, true, true);
        final String suffix = RandomStringUtils.random(8, true, true);
        final String content = RandomStringUtils.random(50, true, true);
        String clientToken = String.format("%s_%s_%s", prefix, content, suffix);
        if (!Strings.isEmpty(this.clientApplicationToken)) {
            clientToken = this.clientApplicationToken;
        }
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
