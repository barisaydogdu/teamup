package com.teamup.teamup;

import com.filepackage.TeamupApplication;
import com.filepackage.dto.ProjectsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Projects;
import com.filepackage.entity.Role;
import com.filepackage.entity.Users;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IProjectsRepository;
import com.filepackage.repository.IUsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest(
        classes = TeamupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class ProjectControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IProjectsRepository projectsRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private AutoMapper autoMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/projects";
    }

    @BeforeEach
    void setUp() {
        projectsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @Order(1)
    void  testAddProject() {
        Users user = new Users();
        user.setEmail("barisaydogdu1@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);

        ProjectsDto project = new ProjectsDto();
        project.setTitle("Test Proje");
        project.setShortDescription("Short description");
        project.setDetailedDescription("Detailed description");
        project.setStatus("OPENN");
        project.setVisibility("PUBLIC");
        project.setUser(usersDto);
        System.out.println("USERID: "+usersDto.getId());
//        project.setCreatorID(user.getId());
//        project.setCreatorID(1L);
        project.setStartDate(new Date());

        ResponseEntity<ProjectsDto> response = restTemplate.postForEntity(
                getBaseUrl(),
                project,
                ProjectsDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo("Test Proje");

    }

    @Test
    @Order(2)
    void testGetAllProjects() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);


        Projects project = new Projects();
        project.setTitle("Projex");
        project.setShortDescription("desc");
        project.setStatus("OPEN");
        project.setUser(user);
        projectsRepository.save(project);

        ResponseEntity<ProjectsDto[]> response = restTemplate.getForEntity(
                getBaseUrl(),
                ProjectsDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(3)
    void testDeleteProject() {
        Users user = new Users();
        user.setEmail("barisaydogdu3@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
//        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);


        Projects project = new Projects();
        project.setUser(user);
        project.setTitle("to be deleted");
        project.setShortDescription("desc");
        project.setStatus("OPEN");
        project.setUser(user);
        Projects saved = projectsRepository.save(project);

        restTemplate.delete(getBaseUrl() + "/" + saved.getProjectID());

        assertThat(projectsRepository.findById(saved.getProjectID())).isEmpty();
    }

    @Test
    @Order(4)
    void testUpdateProject() {
        Users user = new Users();
        user.setEmail("barisaydogdu6@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);

        Projects project = new Projects();
        project.setTitle("old project");
        project.setShortDescription("old project desc");
        project.setStatus("Closed");
        project.setUser(user);
        Projects saved = projectsRepository.save(project);

        ProjectsDto updateDto = new ProjectsDto();
        updateDto.setTitle("Updated Project");
        updateDto.setStatus("UPDATED");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProjectsDto> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<ProjectsDto> response = restTemplate.exchange(
                getBaseUrl() + "/" + saved.getProjectID(),
                HttpMethod.PUT,
                request,
                ProjectsDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Project");
    }
}
