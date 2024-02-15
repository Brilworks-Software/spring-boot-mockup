package com.brilworks.mockup.security;

import com.brilworks.mockup.modules.user.model.AuthUser;
import com.brilworks.mockup.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class TokenUtils {

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    String getUserNameFromToken(String token) {
        String userName = null;
        final Claims claims = getClaimsFromToken(token);
        if (!Objects.isNull(claims))
            userName = claims.getSubject();

        return userName;

    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(tokenSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public String generateToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail());
        claims.put("created", DateUtils.generateCurrentDate());
        return this.generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(this.generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    boolean validateToken(String token, UserDetails userDetails) {
        final String userName = this.getUserNameFromToken(token);
        return (userName.equals(userDetails.getUsername()) && !(this.isTokenExpired(token)));
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(DateUtils.generateCurrentDate());
    }

    Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        final Claims claims = getClaimsFromToken(token);
        if (!Objects.isNull(claims))
            expiration = claims.getExpiration();
        return expiration;
    }

    private Date generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONDAY, 2);
        return calendar.getTime();
    }

}
