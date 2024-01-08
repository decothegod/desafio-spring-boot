package com.example.service.user;

import com.example.dto.UserDTO;
import com.example.dto.request.UserRequest;

import java.util.List;

public interface IUserService {
    UserDTO register(UserRequest request);

    UserDTO login(UserRequest request);

    List<UserDTO> getAllUsers();
}
