package com.vsii.enamecard.jwt;

import com.vsii.enamecard.jwt.model.CustomUserDetails;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final String JWT_SECRET;

    private final long JWT_EXPIRATION;

    private final LoginSession loginSession;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String jwt_secret,
            @Value(("${jwt.time.expiration}")) long jwt_expiration, LoginSession loginSession) {
        JWT_SECRET = jwt_secret;
        JWT_EXPIRATION = jwt_expiration;
        this.loginSession = loginSession;
    }

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + JWT_EXPIRATION);
        Map<String,Object> claims = new HashMap<>();
        claims.put("username",userDetails.getUsername());
        claims.put("id",userDetails.getAccountEntity().getId());
        String jwtToken =  Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
        loginSession.saveLoginSession(String.valueOf(userDetails.getAccountEntity().getId()),jwtToken,expireDate.toInstant());
        return jwtToken;
    }

    public Integer getUserIdFromJWT(String token) {
        Claims claims = this.getTokenRaw(token);
        return Integer.parseInt(String.valueOf(claims.get("id")));
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = this.getTokenRaw(token);
        return String.valueOf(claims.get("username"));
    }

    public Claims getTokenRaw(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}" +  ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}" + ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}" + ex.getMessage());
        }
        return false;
    }
}
