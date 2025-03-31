package com.filepackage.service;

import com.filepackage.dto.UsersDto;

import java.util.List;

public interface IUsersService {
    List<UsersDto> getAllUsers();
    UsersDto addUser(UsersDto usersDto);
    UsersDto updateUser(Long id, UsersDto usersDto);
    void deleteUser(Long id);
    UsersDto getById(Long userId);
}
