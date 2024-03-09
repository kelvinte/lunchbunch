package sg.okayfoods.lunchbunch.application;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sg.okayfoods.lunchbunch.common.constant.Constants;
import sg.okayfoods.lunchbunch.domain.entity.AppUser;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service

public class JWTTokenService {
    @Value("${jwt.secret}")
    private String jwtSecretKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(AppUser user, Long epochExpiry) {

        return Jwts
                .builder()
                .setClaims(
                        Map.of(Constants.AUTHORITY_CLAIM, user.getAppRole().getName(),
                                Constants.USER_ID_CLAIM, user.getId().toString())
                )
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.ofEpochSecond(epochExpiry)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long calculateExpiry(){
        return System.currentTimeMillis()  + jwtExpiration;
    }


    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }


}
