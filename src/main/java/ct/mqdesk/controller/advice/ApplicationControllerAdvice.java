package ct.mqdesk.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final AccessDeniedException exception) {
        ApplicationControllerAdvice.log.error(exception.getMessage(), exception);
        return ProblemDetail.forStatusAndDetail(
                FORBIDDEN,
                "Vos droits ne vous permettent pas d'effectuer cette action"
        );
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = BadCredentialsException.class)
    public @ResponseBody
    ProblemDetail badCredentialsException(final BadCredentialsException exception) {
        ApplicationControllerAdvice.log.error(exception.getMessage(), exception);
        final ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                UNAUTHORIZED,
                "identifiants invalides"
        );
        problemDetail.setProperty("erreur", "nous n'avons pas pu vous identifier");
        return problemDetail;
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = RuntimeException.class)
    public Map<String, String> runtimeException(final RuntimeException exception) {
        ApplicationControllerAdvice.log.error("", exception);
        return Map.of("erreur", exception.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = Exception.class)
    public Map<String, String> exceptionsHandler(final Exception exception) {
        ApplicationControllerAdvice.log.error("", exception);
        return Map.of("erreur", "description");
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Map<String, String> IllegalArgumentException(final IllegalArgumentException exception) {
        ApplicationControllerAdvice.log.error("", exception);
        return Map.of("erreur", exception.getMessage());
    }
}
