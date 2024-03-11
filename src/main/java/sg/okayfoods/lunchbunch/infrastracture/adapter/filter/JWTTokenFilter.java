package sg.okayfoods.lunchbunch.infrastracture.adapter.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sg.okayfoods.lunchbunch.application.AuthenticationService;
import sg.okayfoods.lunchbunch.application.JWTTokenService;
import sg.okayfoods.lunchbunch.common.constant.Constants;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.constant.UrlConstants;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.common.util.JsonUtils;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.domain.user.LoggedInUser;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.common.GenericResponse;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Component
public class JWTTokenFilter  extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String jwtSecretKey;

    private final AppUserRepository appUserRepository;
    private final AuthenticationService authenticationService;

    public JWTTokenFilter(AuthenticationService authenticationService,
                                   AppUserRepository appUserRepository) {
        this.authenticationService = authenticationService;
        this.appUserRepository = appUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        boolean isAuth = request.getServletPath().equals(UrlConstants.LOGIN) || request.getServletPath()
                .equals(UrlConstants.REGISTER);

        if(isAuth){
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = request.getHeader(Constants.JWT_HEADER);
        if (jwt == null ||!jwt.startsWith(Constants.BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = jwt.substring(Constants.BEARER.length()).trim();

        try {
            AppUser user = authenticationService.validateToken(jwt);

            Authentication auth = new LoggedInUser(user.getId(), user.getEmail(), null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAppRole().getName()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            filterChain.doFilter(request, response);
        }catch (AppException ex){
            invalidToken(response, ex.getErrorCode());
        }catch (Exception ex){
            invalidToken(response, ErrorCode.UNKNOWN_AUTH_ERROR_OCCURRED);
        }
    }

    private void invalidToken(HttpServletResponse response, ErrorCode errorCode) throws ServletException, IOException {
        GenericResponse genericResponse = errorCode.toGenericResponse();
        response.getWriter().write(JsonUtils.toJson(genericResponse));
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
    }

}
