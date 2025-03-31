package com.filepackage.controller;

import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Users;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.Impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "http;//127.0.0.1:5500",allowCredentials = "true")
public class UsersController {

    @Autowired
    private UsersService usersService;
    private final IUsersRepository usersRepository;

    public UsersController(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public ResponseEntity<List<UsersDto>> getAllUsers() {
        List<UsersDto> users = usersService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UsersDto> addUser(@RequestBody UsersDto userDto){
        UsersDto savedUser = usersService.addUser(userDto);
        return  new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UsersDto> getUserById(@PathVariable("id") Long userId) {
        UsersDto userDto = usersService.getById(userId);
        return ResponseEntity.ok(userDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
    @PutMapping("{id}")
    public ResponseEntity<UsersDto> updateUser (@PathVariable("id") Long userId, @RequestBody UsersDto updatedUser) {
        UsersDto userDto=usersService.updateUser(userId,updatedUser);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUserq() {
        // Mevcut tüm kullanıcıları getir
        List<Users> users = usersRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
