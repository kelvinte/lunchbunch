package sg.okayfoods.lunchbunch.infrastracture.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sg.okayfoods.lunchbunch.common.constant.ErrorCode;
import sg.okayfoods.lunchbunch.domain.entity.AppRole;
import sg.okayfoods.lunchbunch.domain.repository.AppUserRepository;

import java.util.List;

@Component
public class CustomAuthenticationProvider  implements AuthenticationProvider {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        var user = appUserRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException(ErrorCode.INVALID_USERNAME_OR_PASSWORD.getMessage()));
        if(passwordEncoder.matches(pwd, user.getPassword())){
            return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(user.getAppRole()));
        }
        throw new BadCredentialsException(ErrorCode.INVALID_USERNAME_OR_PASSWORD.getMessage());
    }

    private List<GrantedAuthority> getGrantedAuthorities(AppRole role) {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
