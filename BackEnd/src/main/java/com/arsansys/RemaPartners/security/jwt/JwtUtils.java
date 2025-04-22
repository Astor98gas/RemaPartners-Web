package com.arsansys.RemaPartners.security.jwt;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.arsansys.RemaPartners.models.entities.JwtEntity;
import com.arsansys.RemaPartners.services.JwtService;
import com.google.api.client.util.DateTime;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    @Autowired
    private JwtService jwtService;

    // Generar token acceso
    public String generateAccesToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey())
                .compact();
    }

    // Validar token acceso
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignatureKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            if (jwtService.findByToken(token) != null && !jwtService.findByToken(token).getIsValid()) {
                log.error("Token invalido1: ".concat(token));
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("Token invalido2: ".concat(e.getMessage()));
            return false;
        }
    }

    // Obtener username del token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Obtener un claim(informacion) de token
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Obtener todos los claims(informacion) token
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignatureKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Error extracting claims from token: " + e.getMessage());
            throw new RuntimeException("Invalid token", e);
        }
    }

    // Obtener firma token
    public SecretKey getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void invalidateToken(String jwtTokenString) {
        try {
            JwtEntity jwtToken = new JwtEntity(); // Create a new instance instead of null
            jwtToken.setToken(jwtTokenString);
            jwtToken.setIsValid(false);
            Date expirationDate = getExpirationDateFromToken(jwtTokenString);
            jwtToken.setExpirationDate(expirationDate);
            jwtToken.setUsername(getUsernameFromToken(jwtTokenString));

            jwtService.save(jwtToken);
        } catch (Exception e) {
            log.error("Error al invalidar el token: " + e.getMessage());
        }
    }

    private Date getExpirationDateFromToken(String jwtTokenString) {
        Claims claims = extractAllClaims(jwtTokenString);
        try {
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("Error al obtener la fecha de expiración del token: " + e.getMessage());
            return null;
        }
    }

}
