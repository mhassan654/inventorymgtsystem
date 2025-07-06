package org.saavatech.inventorymgtsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.saavatech.inventorymgtsystem.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    private  final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        if (token == null) {
            String email = jwtUtils.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (StringUtils.hasText(email) && jwtUtils.isTokenValid(token, userDetails)) {
                log.info("Valid Token, {}", email);

                UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(
                        userDetails,null,
                        userDetails.getAuthorities()
                );
                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        try{
            filterChain.doFilter(request, response);
        }catch (Exception e){
            log.error("Exception occurred in Authfilter: " + e.getMessage());
        }

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
