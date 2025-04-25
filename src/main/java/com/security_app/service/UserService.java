package com.security_app.service;

import com.security_app.model_entity.user_dtos.UserDto;
import com.security_app.model_entity.user_dtos.UserRegistrationDto;
import com.security_app.model_entity.user_dtos.UserUpdateDto;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto registerUser(UserRegistrationDto registrationDto);

    List<UserDto> getAllUsers();

    Optional<UserDto> getUserById(Long id);

    Optional<UserDto> getUserByUsername(String username);

    UserDto updateUser(Long id, UserUpdateDto userUpdateDto);

    void deleteUser(Long id);

    void changeUserRole(Long id, String role, boolean add);

    void toggleUserStatus(Long id);

    Optional<UserDto> getUser(String username, String password);
}