package com.vsii.enamecard.jwt;

import com.vsii.enamecard.jwt.model.CustomUserDetails;
import com.vsii.enamecard.jwt.service.UserJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserJwtService userJwtService;

    @Autowired
    private LoginSession loginSession;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response
            , FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFormRequest(request);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                String username = jwtTokenProvider.getUsernameFromJWT(jwt);
                Integer userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                UserDetails customUserDetails = userJwtService.loadUserByUsername(username);
                String jwtSession = loginSession.getToken(userId + "");

                if (customUserDetails != null && jwtSession.equals(jwt)) {

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFormRequest(HttpServletRequest request) {
        String bearerString = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerString) && bearerString.startsWith("Bearer ")) {
            return bearerString.substring(7);
        }
        return null;
    }
}
