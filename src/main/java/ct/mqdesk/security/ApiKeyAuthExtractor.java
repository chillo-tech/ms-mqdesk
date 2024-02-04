package ct.mqdesk.security;

import ct.mqdesk.entity.Token;
import ct.mqdesk.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
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
        final Token token = this.tokenService.findByApplicationNameAndActive(providedKey, true);

        final boolean isValidKey = providedKey.equals(token.getApplication().getName());
        final boolean isValidSecret = this.bCryptPasswordEncoder.matches(providedSecret, token.getValue());
        if (isValidKey && isValidSecret) {
            return Optional.of(new ApiKeyAuth(providedKey, AuthorityUtils.NO_AUTHORITIES));
        }

        return Optional.empty();
    }
}