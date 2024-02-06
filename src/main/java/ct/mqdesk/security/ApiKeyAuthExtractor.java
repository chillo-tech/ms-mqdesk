package ct.mqdesk.security;

import ct.mqdesk.entity.Token;
import ct.mqdesk.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ApiKeyAuthExtractor {

    TokenService tokenService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiKeyAuthExtractor(final TokenService tokenService, final BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.tokenService = tokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Optional<Authentication> extract(final HttpServletRequest request) {
        final String providedSecret = request.getHeader("X-API-SECRET");
        final String providedKey = request.getHeader("X-API-KEY");
        System.out.printf("Application key %s secret %s", providedKey, providedSecret);
        ApiKeyAuthExtractor.log.info("Application key {}", providedKey);
        ApiKeyAuthExtractor.log.info("Application secret {}", providedSecret);
        final Token token = this.tokenService.findByApplicationNameAndActive(providedKey, true);

        final boolean isValidKey = providedKey.equals(token.getApplication().getName());
        final boolean isValidSecret = this.bCryptPasswordEncoder.matches(providedSecret, token.getValue());
        if (isValidKey && isValidSecret) {
            return Optional.of(new ApiKeyAuth(providedKey, AuthorityUtils.NO_AUTHORITIES));
        }

        return Optional.empty();
    }
}
