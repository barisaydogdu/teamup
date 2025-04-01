package com.teamup.teamup;

import com.filepackage.dto.SkillsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Skills;
import com.filepackage.entity.Users;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.ISkillsRepository;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.Impl.SkillsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SkillsServiceTest {

    @InjectMocks
    private SkillsService skillsService;

    @Mock
    private ISkillsRepository skillsRepository;

    @Mock
    private IUsersRepository usersRepository;

    @Mock
    private AutoMapper autoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skillsService = new SkillsService(skillsRepository);
        skillsService.autoMapper = autoMapper;
        skillsService.usersRepository = usersRepository;
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Skills skill = new Skills();
        SkillsDto dto = new SkillsDto();

        when(skillsRepository.findById(id)).thenReturn(Optional.of(skill));
        when(autoMapper.convertToDto(skill, SkillsDto.class)).thenReturn(dto);

        SkillsDto result = skillsService.getById(id);

        assertNotNull(result);
        verify(skillsRepository).findById(id);
    }

    @Test
    void testGetById_NotFound() {
        when(skillsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> skillsService.getById(1L));
    }

    @Test
    void testGetAll() {
        Skills skill = new Skills();
        skill.setSkillName("Java");
        Users user = new Users();
        user.setEmail("user@mail.com");
        skill.setUsers(user);

        SkillsDto skillDto = new SkillsDto();
        UsersDto usersDto = new UsersDto();
        usersDto.setEmail("user@mail.com");
        skillDto.setUsers(usersDto);

        when(skillsRepository.findAll()).thenReturn(List.of(skill));
        when(autoMapper.convertToDto(skill, SkillsDto.class)).thenReturn(skillDto);
        when(autoMapper.convertToDto(user, UsersDto.class)).thenReturn(usersDto);

        List<SkillsDto> result = skillsService.getAll();

        assertEquals(1, result.size());
        assertEquals("user@mail.com", result.get(0).getUsers().getEmail());
    }

    @Test
    void testDelete_Success() {
        Long id = 1L;
        Skills skill = new Skills();

        when(skillsRepository.findById(id)).thenReturn(Optional.of(skill));

        skillsService.delete(id);

        verify(skillsRepository).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        when(skillsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> skillsService.delete(1L));
    }

    @Test
    void testUpdate_Success() {
        Long id = 1L;
        Skills skill = new Skills();
        skill.setSkillName("Java");

        SkillsDto updateDto = new SkillsDto();
        updateDto.setSkillName("Spring Boot");

        Skills updatedEntity = new Skills();
        updatedEntity.setSkillName("Spring Boot");

        SkillsDto resultDto = new SkillsDto();
        resultDto.setSkillName("Spring Boot");

        when(skillsRepository.findById(id)).thenReturn(Optional.of(skill));
        when(skillsRepository.save(any(Skills.class))).thenReturn(updatedEntity);
        when(autoMapper.convertToDto(updatedEntity, SkillsDto.class)).thenReturn(resultDto);

        SkillsDto result = skillsService.update(id, updateDto);

        assertNotNull(result);
        assertEquals("Spring Boot", result.getSkillName());
    }

    @Test
    void testCreate() {
        SkillsDto dto = new SkillsDto();
        dto.setSkillName("Docker");

        Skills entity = new Skills();
        entity.setSkillName("Docker");

        when(autoMapper.convertToEntity(dto, Skills.class)).thenReturn(entity);
        when(skillsRepository.save(entity)).thenReturn(entity);
        when(autoMapper.convertToDto(entity, SkillsDto.class)).thenReturn(dto);

        SkillsDto result = skillsService.create(dto);

        assertNotNull(result);
        assertEquals("Docker", result.getSkillName());
    }
}
