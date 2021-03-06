package com.damar.spring.service.security;

import com.damar.spring.exception.JwtNotValidException;
import com.damar.spring.exception.UserNotAuthorizedException;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationService {

    @Autowired
    public AuthorizationService(JwtService jwtService){
        this.jwtService = jwtService;
    }

    private JwtService jwtService;

    @Value("${jwt.issuer}")
    private String ISSUER;

    public void checkAccessAuthorization(String jwt) {
        log.info("[CHECK] Api access: checking authorization...");

        Claims claims;

        try {
            claims = jwtService.decodeJWT(jwt);
        } catch (Exception e){
            throw new JwtNotValidException("Error decoding jwt");
        }

        String issuer = claims.getIssuer();
        String subject = claims.getSubject();

        if (!StringUtils.equals(ISSUER, issuer))
            throw new UserNotAuthorizedException("Error matching jwt, calling service not authorized");

        log.info("[CHECK] Api access: User {} AUTHORIZED", subject);
    }
}
