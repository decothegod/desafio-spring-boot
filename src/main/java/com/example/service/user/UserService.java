package com.example.service.user;

import com.example.dto.UserDTO;
import com.example.dto.request.UserRequest;
import com.example.exception.ServiceExceptionNotFound;
import com.example.exception.ServiceExceptionUnauthorized;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.jwt.IJwtService;
import io.jsonwebtoken.ExpiredJwtException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.util.Constants.*;

@Service
public class UserService implements IUserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IJwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO register(UserRequest request) {
        checkExistUser(request.getUsername());

        UserDTO userDTO = UserDTO.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User user = convertToEntity(userDTO);
        userDTO.setToken(jwtService.getToken(user));
        userRepository.save(user);

        return userDTO;
    }

    @Override
    public UserDTO login(UserRequest request) {
        try {
            User user = getUser(request.getUsername());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            UserDTO userDTO = convertToDto(user);
            userDTO.setToken(jwtService.getToken(user));

            return userDTO;

        } catch (ExpiredJwtException e) {
            LOG.error(e.getMessage());
            throw new ServiceExceptionUnauthorized(e.getMessage());
        } catch (AuthenticationException e) {
            LOG.error(BAD_CREDENTIALS_MSG);
            throw new ServiceExceptionUnauthorized(BAD_CREDENTIALS_MSG);
        }
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserDTO.class)).toList();
    }

    private void checkExistUser(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            LOG.error(USER_EXIST_MSG);
            throw new ServiceExceptionNotFound(USER_EXIST_MSG);
        }
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceExceptionNotFound(USER_NOT_FOUND_MSG));
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
