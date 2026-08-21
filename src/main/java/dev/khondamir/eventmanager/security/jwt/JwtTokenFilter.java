package dev.khondamir.eventmanager.security.jwt;

import dev.khondamir.eventmanager.users.User;
import dev.khondamir.eventmanager.users.UserService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger log= LoggerFactory.getLogger(JwtTokenFilter.class);

    private final JwtTokenManager jwtTokenManager;

    private final UserService userService;

    public JwtTokenFilter(
            JwtTokenManager jwtTokenManager,
            @Lazy UserService userService
    ){
        this.jwtTokenManager=jwtTokenManager;
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("Auth header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        log.info("Token: {}", token);
        try {
            String login = jwtTokenManager.getLoginFromToken(token);

            log.info("Login from token: {}", login);

            if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userService.findByLogin(login);

                if (jwtTokenManager.validateToken(token, user)) {

                    var auth = new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(new SimpleGrantedAuthority(user.role().toString()))
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);

                    log.info("AUTH SUCCESS for user: {}", login);
                }
            }



        } catch (Exception e) {
            log.error("JWT ERROR: ", e);
        }
        filterChain.doFilter(request, response);
    }

}
