package com.teamup.teamup;

import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Role;
import com.filepackage.entity.Users;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.Impl.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @InjectMocks
    private UsersService usersService;

    @Mock
    private IUsersRepository usersRepository;

    @Mock
    private AutoMapper autoMapper;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usersService = new UsersService(usersRepository);
        usersService.autoMapper = autoMapper;
        usersRepository.deleteAll();
    }

    @Test
    void testGetAllUsers() {
        Users user = new Users();
        List<Users> usersList = List.of(user);
        UsersDto usersDto = new UsersDto();

        when(usersRepository.findAll()).thenReturn(usersList);
        when(autoMapper.convertToDto(any(Users.class), eq(UsersDto.class))).thenReturn(usersDto);

        List<UsersDto> result = usersService.getAllUsers();

        assertEquals(1, result.size());
        verify(usersRepository, times(1)).findAll();
    }

    @Test
    void testAddUser() {
        UsersDto usersDto = new UsersDto();
        Users userEntity = new Users();

        when(autoMapper.convertToEntity(usersDto, Users.class)).thenReturn(userEntity);
        when(usersRepository.save(userEntity)).thenReturn(userEntity);
        when(autoMapper.convertToDto(userEntity, UsersDto.class)).thenReturn(usersDto);

        UsersDto result = usersService.addUser(usersDto);

        assertNotNull(result);
        verify(usersRepository, times(1)).save(userEntity);
    }

    @Test
    void testUpdateUser_Success() {
        Long id = 1L;
        Users user = new Users();
        user.setId(id);

        UsersDto updatedDto = new UsersDto();
        updatedDto.setFirstName("Test");
        updatedDto.setLastName("User");
        updatedDto.setEmail("test@example.com");
        updatedDto.setPassword("password");
        updatedDto.setProfilePicture("pic.jpg");
        updatedDto.setBio("bio");
        updatedDto.setPhoneNumber("12345");
        updatedDto.setRole("USER");
        updatedDto.setPhoneVisibility(true);

        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        when(usersRepository.save(user)).thenReturn(user);
        when(autoMapper.convertToDto(updatedDto, UsersDto.class)).thenReturn(updatedDto);

        UsersDto result = usersService.updateUser(id, updatedDto);

        assertNotNull(result);
        verify(usersRepository).findById(id);
        verify(usersRepository).save(user);
    }

    @Test
    void testUpdateUser_NotFound() {
        Long id = 1L;
        UsersDto updatedDto = new UsersDto();

        when(usersRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usersService.updateUser(id, updatedDto));
    }

    @Test
    void testDeleteUser_Success() {
        Long id = 1L;
        Users user = new Users();
        when(usersRepository.findById(id)).thenReturn(Optional.of(user));

        usersService.deleteUser(id);

        verify(usersRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(usersRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> usersService.deleteUser(1L));
    }

    @Test
    void testGetById_Success() {
        Long id = 1L;
        Users user = new Users();
        UsersDto usersDto = new UsersDto();

        when(usersRepository.findById(id)).thenReturn(Optional.of(user));
        when(autoMapper.convertToDto(user, UsersDto.class)).thenReturn(usersDto);

        UsersDto result = usersService.getById(id);

        assertNotNull(result);
        verify(usersRepository).findById(id);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        Users user = new Users();

        when(usersRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Users foundUser = usersService.findByEmail(email);

        assertNotNull(foundUser);
        verify(usersRepository).findByEmail(email);
    }

}
