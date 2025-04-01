package com.teamup.teamup;

import com.filepackage.TeamupApplication;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Role;
import com.filepackage.entity.Users;
import com.filepackage.repository.IProjectsRepository;
import com.filepackage.repository.IUsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test") // application-test.yml kullanır
@SpringBootTest(
        classes = TeamupApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")

public class UsersControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private IProjectsRepository projectsRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/users";
    }

    @BeforeEach
    void setUp() {
        projectsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testAddUser() {
        UsersDto user = new UsersDto();
        user.setFirstName("Baris");
        user.setLastName("Aydogdu");
        user.setEmail("baris@mail.com");
        user.setPassword("12345");
        user.setRole("ADMIN");

        ResponseEntity<UsersDto> response = restTemplate.postForEntity(
                getBaseUrl(),
                user,
                UsersDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getFirstName()).isEqualTo("Baris");
    }

    @Test
    @Order(2)
    void testGetAllUsers() {
        Users user = new Users();
        user.setFirstName("Gizem");
        user.setLastName("Koc");
        user.setEmail("gizem@mail.com");
        user.setPassword("pass");
        user.setRole(Role.USER);
        usersRepository.save(user);

        ResponseEntity<UsersDto[]> response = restTemplate.getForEntity(
                getBaseUrl(),
                UsersDto[].class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(3)
    void testDeleteUser() {
        Users user = new Users();
        user.setFirstName("Zeynep");
        user.setLastName("Karakan");
        user.setEmail("zeynep@example.com");
        user.setPassword("test");
        user.setRole(Role.USER);
        Users saved = usersRepository.save(user);

        restTemplate.delete(getBaseUrl() + "/" + saved.getId());

        assertThat(usersRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        Users user = new Users();
        user.setFirstName("emircan");
        user.setLastName("eren");
        user.setEmail("emican@example.com");
        user.setPassword("pass");
        user.setRole(Role.USER);
        user.setPhoneVisibility(true);
        Users saved = usersRepository.save(user);

        UsersDto updated = new UsersDto();
        updated.setFirstName("Eren Updated");
        updated.setLastName("Emircan updated");
        updated.setEmail("eren@example.com");
        updated.setPassword("pass");
        updated.setRole("ADMIN");
        updated.setPhoneVisibility(false);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsersDto> request = new HttpEntity<>(updated, headers);

        ResponseEntity<UsersDto> response = restTemplate.exchange(
                getBaseUrl() + "/" + saved.getId(),
                HttpMethod.PUT,
                request,
                UsersDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getFirstName()).isEqualTo("Eren Updated");
    }
}
