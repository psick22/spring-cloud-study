package com.example.userservice.security;

import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Override
    public Authentication attemptAuthentication
        (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            RequestLogin creds = new ObjectMapper()
                .readValue(request.getInputStream(), RequestLogin.class);

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    creds.getEmail(),
                    creds.getPassword(),
                    new ArrayList<>()
                );

            return getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication
        (HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
