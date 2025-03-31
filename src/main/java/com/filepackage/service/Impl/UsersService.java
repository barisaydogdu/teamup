package com.filepackage.service.Impl;

import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Role;
import com.filepackage.entity.Users;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService implements IUsersService {
    @Autowired
   public AutoMapper autoMapper;

    private IUsersRepository usersRepository;
//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
//        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UsersDto> getAllUsers() {
        List<Users> users=usersRepository.findAll();

        return users.stream().map(user -> autoMapper.convertToDto(user,UsersDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsersDto addUser(UsersDto usersDto) {
        Users user = autoMapper.convertToEntity(usersDto,Users.class);
        Users savedUser = usersRepository.save(user);
        return  autoMapper.convertToDto(savedUser,UsersDto.class);
    }

    @Override
    public UsersDto updateUser(Long id, UsersDto updatedUser) {
        Users user= usersRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User is not exist with given id:)" + id));
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        user.setProfilePicture(updatedUser.getProfilePicture());
        user.setBio(updatedUser.getBio());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setRole(Role.valueOf(updatedUser.getRole()));
        user.setPhoneVisibility(updatedUser.getPhoneVisibility());
        user.setRole(Role.valueOf(updatedUser.getRole()));
        //user.(updatedUser.getName());
        Users updatedUserObj = usersRepository.save(user);
        return autoMapper.convertToDto(updatedUser,UsersDto.class);   }

    @Override
    public void deleteUser(Long id) {
        Users user = usersRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("User does not exist with given id"));
        usersRepository.deleteById(id);
    }

    @Override
    public UsersDto getById(Long userId) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User does not exist with given id)"+userId));
        return autoMapper.convertToDto(user, UsersDto.class);
    }

//    public Users saveUser(Users user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        return usersRepository.save(user);
//    }

    public Users findByEmail(String email) {
        return usersRepository.findByEmail(email).orElse(null);
    }

}
