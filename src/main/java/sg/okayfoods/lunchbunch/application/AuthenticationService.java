package sg.okayfoods.lunchbunch.application;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.constant.Constants;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.common.constant.RoleConstants;
import sg.okayfoods.lunchbunch.common.constant.UserStatus;
import sg.okayfoods.lunchbunch.domain.entity.AppRole;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;
import sg.okayfoods.lunchbunch.common.exception.AppException;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.LoginResponse;
import sg.okayfoods.lunchbunch.infrastracture.adapter.dto.auth.RegistrationRequest;
import sg.okayfoods.lunchbunch.infrastracture.adapter.mapper.UserMapper;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Slf4j
@Service
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenService jwtTokenService;
    private final UserMapper userMapper;

    @Value("${jwt.secret}")
    private String jwtSecretKey;

    public AuthenticationService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager, JWTTokenService jwtTokenService, UserMapper userMapper) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userMapper = userMapper;
    }


    public void register(RegistrationRequest registrationRequest){
        log.info("Registering {}", registrationRequest.getName());

        var user = appUserRepository.findByEmail(registrationRequest.getEmail());

        if(user.isPresent()){
            throw new AppException(ErrorCode.EMAIL_ALREADY_IN_USE);
        }

        AppRole appRole = AppRole.builder().id(RoleConstants.USER).build();

        AppUser appUser = AppUser.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .name(registrationRequest.getName())
                .status(UserStatus.ACTIVE)
                .appRole(appRole)
                .build();

        appUserRepository.save(appUser);
    }

    public LoginResponse login(LoginRequest loginRequest){
        var user = appUserRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new BadCredentialsException(
                ErrorCode.INVALID_USERNAME_OR_PASSWORD.getMessage()));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        Long expiryInMillis = jwtTokenService.calculateExpiry();
        var jwtToken = jwtTokenService.generateToken(user, expiryInMillis);
        return userMapper.map(user, jwtToken, expiryInMillis);
    }


    public AppUser validateToken(String jwt){
        SecretKey key = Keys.hmacShaKeyFor(
                jwtSecretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        Long iat = claims.get(Constants.EXPIRY_CLAIM, Long.class);
        if(Instant.now().isAfter(Instant.ofEpochMilli(iat))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        String email = claims.get(Constants.SUB_CLAIM, String.class);
        return appUserRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException(
                ErrorCode.INVALID_TOKEN.getMessage()));

    }

}