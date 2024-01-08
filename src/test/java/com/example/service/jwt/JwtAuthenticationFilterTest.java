package com.example.service.jwt;

import com.example.model.User;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {
    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void doFilterInternal_ValidToken() throws Exception {
        String token = "validToken";
        when(request.getHeader(any())).thenReturn("Bearer " + token);
        when(jwtService.getUserNameFromToken(token)).thenReturn("testUser");

        User user = User.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(user);

        when(jwtService.isTokenValid(token, user)).thenReturn(true);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext, times(1)).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_InvalidToken() throws Exception {
        String token = "invalidToken";
        when(request.getHeader(any())).thenReturn("Bearer " + token);
        when(jwtService.getUserNameFromToken(token)).thenReturn("testUser");

        User user = User.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .build();

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(user);

        when(jwtService.isTokenValid(token, user)).thenReturn(false);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_NoToken() throws Exception {
        when(request.getHeader(any())).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}
