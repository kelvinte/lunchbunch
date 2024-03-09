package sg.okayfoods.lunchbunch.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import java.time.Instant;

@Slf4j
@Service
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTTokenService jwtTokenService;
    private final UserMapper userMapper;

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


}