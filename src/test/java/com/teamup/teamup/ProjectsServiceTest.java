package com.teamup.teamup;

import com.filepackage.dto.ProjectsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Projects;
import com.filepackage.entity.Users;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IProjectsRepository;
import com.filepackage.service.Impl.ProjectsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectsServiceTest {

    @InjectMocks
    private ProjectsService projectsService;

    @Mock
    private IProjectsRepository projectsRepository;

    @Mock
    private AutoMapper autoMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        projectsService = new ProjectsService(projectsRepository);
        projectsService.autoMapper = autoMapper;
    }

    @Test
    void testGetById() {
        Long id = 1L;
        Projects project = new Projects();
        ProjectsDto dto = new ProjectsDto();

        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));
        when(autoMapper.convertToDto(project, ProjectsDto.class)).thenReturn(dto);

        ProjectsDto result = projectsService.getById(id);

        assertNotNull(result);
        verify(projectsRepository).findById(id);
    }

    @Test
    void testGetById_NotFound() {
        when(projectsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectsService.getById(1L));
    }

    @Test
    void testGetAll() {
        Projects project = new Projects();
        ProjectsDto dto = new ProjectsDto();

        when(projectsRepository.findAll()).thenReturn(List.of(project));
        when(autoMapper.convertToDto(any(), eq(ProjectsDto.class))).thenReturn(dto);

        List<ProjectsDto> result = projectsService.getAll();

        assertEquals(1, result.size());
        verify(projectsRepository).findAll();
    }

    @Test
    void testDelete_Success() {
        Long id = 1L;
        Projects project = new Projects();

        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));

        projectsService.delete(id);

        verify(projectsRepository).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        when(projectsRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectsService.delete(1L));
    }

    @Test
    void testUpdate_Success() {
        Long id = 1L;

        Projects project = new Projects();
        project.setProjectID(id);
        project.setTitle("Old Title");

        ProjectsDto updatedDto = new ProjectsDto();
        updatedDto.setTitle("New AI Project");
        updatedDto.setShortDescription("Updated short desc");
        updatedDto.setDetailedDescription("Updated detailed desc");
        updatedDto.setStatus("COMPLETED");
        updatedDto.setVisibility("PRIVATE");

        Projects savedProject = new Projects();
        savedProject.setProjectID(id);
        savedProject.setTitle("New AI Project");
        savedProject.setShortDescription("Updated short desc");
        savedProject.setDetailedDescription("Updated detailed desc");
        savedProject.setStatus("COMPLETED");
        savedProject.setVisibility("PRIVATE");

        ProjectsDto resultDto = new ProjectsDto();
        resultDto.setTitle("New AI Project");

        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectsRepository.save(any(Projects.class))).thenReturn(savedProject);
        when(autoMapper.convertToDto(savedProject, ProjectsDto.class)).thenReturn(resultDto);

        ProjectsDto result = projectsService.update(id, updatedDto);

        assertNotNull(result);
        assertEquals("New AI Project", result.getTitle());
        verify(projectsRepository).findById(id);
        verify(projectsRepository).save(any(Projects.class));
        verify(autoMapper).convertToDto(savedProject, ProjectsDto.class);
    }

    @Test
    void testGetAll_WithUserMapping() {
        Projects project = new Projects();
        Users user = new Users();
        user.setId(1L);
        user.setEmail("user@example.com");
        project.setUser(user);

        ProjectsDto projectsDto = new ProjectsDto();
        UsersDto usersDto = new UsersDto();
        usersDto.setId(1L);
        usersDto.setEmail("user@example.com");

        when(projectsRepository.findAll()).thenReturn(List.of(project));
        when(autoMapper.convertToDto(project, ProjectsDto.class)).thenReturn(projectsDto);
        when(autoMapper.convertToDto(user, UsersDto.class)).thenReturn(usersDto);

        // Act
        List<ProjectsDto> result = projectsService.getAll();

        // Assert
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getUser());
        assertEquals("user@example.com", result.get(0).getUser().getEmail());

        verify(projectsRepository).findAll();
        verify(autoMapper).convertToDto(project, ProjectsDto.class);
        verify(autoMapper).convertToDto(user, UsersDto.class);
    }


    @Test
    void testCreate() {
        ProjectsDto dto = new ProjectsDto();
        dto.setTitle("AI Project");
        dto.setShortDescription("Short desc");
        dto.setDetailedDescription("Detailed desc");
        dto.setStatus("ACTIVE");
        dto.setVisibility("PUBLIC");

        Projects project = new Projects();
        project.setTitle(dto.getTitle());
        project.setShortDescription(dto.getShortDescription());
        project.setDetailedDescription(dto.getDetailedDescription());
        project.setStatus(dto.getStatus());
        project.setVisibility(dto.getVisibility());

        when(autoMapper.convertToEntity(dto, Projects.class)).thenReturn(project);
        when(projectsRepository.save(project)).thenReturn(project);
        when(autoMapper.convertToDto(project, ProjectsDto.class)).thenReturn(dto);

        ProjectsDto result = projectsService.create(dto);

        assertNotNull(result);
        assertEquals("AI Project", result.getTitle());
        verify(projectsRepository).save(project);
    }
}
