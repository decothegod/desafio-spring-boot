package com.example.service.user;

import com.example.dto.UserDTO;
import com.example.dto.request.UserRequest;
import com.example.exception.ServiceExceptionNotFound;
import com.example.exception.ServiceExceptionUnauthorized;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.jwt.IJwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String TEST_USERNAME = "userTest";
    private static final String TEST_PASSWORD = "password";
    private static final String ENCODED_PASSWORD = "passwordEncoder";
    private static final String TOKEN = "token";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IJwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ModelMapper mapper;

    private User createUser() {
        return User.builder()
                .id(1L)
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
    }

    @Test
    public void login_Successful() {
        UserRequest request = UserRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        User user = createUser();

        UserDTO userDTO = UserDTO.builder()
                .username(TEST_USERNAME)
                .password(ENCODED_PASSWORD)
                .token(TOKEN)
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(jwtService.getToken(user)).thenReturn(TOKEN);
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO response = userService.login(request);
        assertNotNull(response);
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(ENCODED_PASSWORD, response.getPassword());
        assertEquals(TOKEN, response.getToken());
    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void login_UserNotFound() {
        UserRequest request = UserRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        userService.login(request);
    }

    @Test(expected = ServiceExceptionUnauthorized.class)
    public void login_PasswordIncorrect() {
        UserRequest request = UserRequest.builder()
                .username(TEST_USERNAME)
                .password("passwordWrong")
                .build();

        User user = createUser();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenThrow(BadCredentialsException.class);

        userService.login(request);
    }

    @Test
    public void register_Successful() {
        UserRequest request = UserRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);

        UserDTO response = userService.register(request);
        assertNotNull(response);
        assertEquals(TEST_USERNAME, response.getUsername());
        assertEquals(ENCODED_PASSWORD, response.getPassword());
    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void register_UserExists() {
        UserRequest request = UserRequest.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();

        User user = createUser();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        userService.register(request);
    }

    @Test
    public void getAllUsers() {
        User user1 = createUser();
        User user2 = createUser();

        List<User> users = List.of(user1, user2);

        UserDTO userDTO = UserDTO.builder()
                .username(TEST_USERNAME)
                .password(ENCODED_PASSWORD)
                .token(TOKEN)
                .build();

        when(userRepository.findAll()).thenReturn(users);
        when(mapper.map(user1, UserDTO.class)).thenReturn(userDTO);

        List<UserDTO> response = userService.getAllUsers();
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals(TEST_USERNAME, response.get(0).getUsername());
        assertEquals(ENCODED_PASSWORD, response.get(0).getPassword());
        assertEquals(TOKEN, response.get(0).getToken());
    }
}
