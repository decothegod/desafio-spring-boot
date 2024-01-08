package com.example.controller;

import com.example.dto.UserDTO;
import com.example.dto.request.UserRequest;
import com.example.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/auth")
public class UserController {
    @Autowired
    private IUserService userService;

    @Operation(summary = "Get all users", description = "Retrieves the complete list of users.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, returns the list of users. If there are no users, it returns an empty list"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Register a new user", description = "Register a new user with the information provided.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, returns the registered user and the token of the logged in user."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> register(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @Operation(summary = "Log in", description = "Sign in with the provided credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, returns the token of the logged in user."),
            @ApiResponse(responseCode = "401", description = "Unauthorized, incorrect credentials."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
