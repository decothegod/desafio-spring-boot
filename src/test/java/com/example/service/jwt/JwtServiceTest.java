package com.example.service.jwt;

import com.example.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtServiceTest {
    @InjectMocks
    private JwtService jwtService;
    @Mock
    private User userDetails;
    @Mock
    private Claims claims;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUserNameFromToken_Successful() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.getToken(userDetails);
        String username = jwtService.getUserNameFromToken(token);

        assertNotNull(token);
        assertNotNull(username);
        assertEquals("testUser", username);
    }

    @Test
    public void isTokenValid_ValidToken() {
        when(userDetails.getUsername()).thenReturn("testUser");
        String token = jwtService.getToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertNotNull(token);
        assertTrue(isValid);
    }

    @Test(expected = ExpiredJwtException.class)
    public void isTokenValid_ExpiredToken() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBaWx5biBGZXJyYWRhIiwiaWF0IjoxNzAwMTk5MzE3LCJleHAiOjE3MDAyMDA3NTd9.q_kjnBnIm5Mdx55ImEnR7l05RsQGrtgm1Ihr_O_PXRI";
        jwtService.isTokenValid(invalidToken, userDetails);
    }

    @Test
    public void getExpiration_Successful() {
        String token = jwtService.getToken(userDetails);

        Date expiration = jwtService.getExpiration(token);

        assertNotNull(expiration);
    }

}
