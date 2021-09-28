package ch.zli.m223.punchclock.service;

import java.time.Duration;

import javax.enterprise.context.RequestScoped;

import io.smallrye.jwt.build.Jwt;

import org.eclipse.microprofile.jwt.Claims;

@RequestScoped
public class AuthenticationService {
    
    public String generateValidJwtToken(String username, String role){
        String token =
            Jwt.issuer("https://zli.ch/issuer") 
            .upn(username) 
            .groups(role)
            .claim(Claims.birthdate.name(), "2001-07-13")
            .expiresIn(Duration.ofHours(1)) 
            .sign();
        return token;
    }
}
