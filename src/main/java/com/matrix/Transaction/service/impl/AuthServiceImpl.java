package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.jwt.JwtService;
import com.matrix.Transaction.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private static final String ROLE_CLAIM = "roles";

    private static final String AUTH_HEADER = "Authorization";

    private static final String BEARER_PREFIX = "bearer";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    public Optional<Authentication> getAuthentication(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(AUTH_HEADER))
                .filter(this::isBearerAuth)
                .flatMap(this::getAuthenticationBearer);
    }

    private boolean isBearerAuth(String header){
        return header.toLowerCase().startsWith(BEARER_PREFIX);
    }


    private Optional<Authentication> getAuthenticationBearer (String header){

        String token = header.substring("bearer".length()).trim();
        Claims claims = jwtService.validateToken(token);

        log.info("Claims parsed {}",claims);
        if(claims.getExpiration().before(new Date())) {
            return Optional.empty();
        }

        return Optional.of(getAuthenticationBearer(claims));
    }


    private Authentication getAuthenticationBearer (Claims claims) {
        List<?> roles = claims.get(ROLE_CLAIM, List.class);

        List<GrantedAuthority> authorityList = roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(claims.getSubject()),"", authorityList);
    }
}