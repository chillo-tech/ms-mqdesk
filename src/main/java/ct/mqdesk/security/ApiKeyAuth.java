package ct.mqdesk.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

public class ApiKeyAuth extends AbstractAuthenticationToken {

    @Serial
    private static final long serialVersionUID = 8077563358447837022L;
    private final String apiKey;

    public ApiKeyAuth(final String apiKey, final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.apiKey;
    }

}
