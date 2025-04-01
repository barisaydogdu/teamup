package com.teamup.teamup;

import com.filepackage.TeamupApplication;
import com.filepackage.dto.SkillsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Role;
import com.filepackage.entity.Skills;
import com.filepackage.entity.Users;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IProjectsRepository;
import com.filepackage.repository.ISkillsRepository;
import com.filepackage.repository.IUsersRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TeamupApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration")
public class SkillsControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ISkillsRepository skillsRepository;

    @Autowired
    private IProjectsRepository projectsRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private AutoMapper autoMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/skills";
    }

    @BeforeEach
    void setUp() {
        projectsRepository.deleteAll();
        skillsRepository.deleteAll();
        usersRepository.deleteAll();
    }

    @Test
    @Order(1)
    void testAddSkill() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);

        SkillsDto skill = new SkillsDto();
        skill.setSkillName("Java");
        skill.setUsers(usersDto);

        ResponseEntity<SkillsDto> response = restTemplate.postForEntity(getBaseUrl(), skill, SkillsDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getSkillName()).isEqualTo("Java");
        assertThat(response.getBody().getUsers()).isNotNull();
        assertThat(response.getBody().getUsers().getEmail()).isEqualTo("barisaydogdu2@mail.com"); }

    @Test
    @Order(2)
    void testGetAllSkills() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);

        Skills skill = new Skills();
        skill.setSkillName("Spring Boot");
        skill.setUsers(user);
        skillsRepository.save(skill);

        ResponseEntity<SkillsDto[]> response = restTemplate.getForEntity(getBaseUrl(), SkillsDto[].class);

        SkillsDto[] body = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotEmpty();
        assertThat(body[0].getUsers()).isNotNull();
        assertThat(body[0].getUsers().getEmail()).isEqualTo("barisaydogdu2@mail.com");
    }

    @Test
    @Order(3)
    void testDeleteSkill() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);


        Skills skill = new Skills();
        skill.setSkillName("ToDelete");
        skill.setUsers(user);
        Skills saved = skillsRepository.save(skill);

        restTemplate.delete(getBaseUrl() + "/" + saved.getSkillID());

        assertThat(skillsRepository.findById(saved.getSkillID())).isEmpty();
    }

    @Test
    @Order(4)
    void testUpdateSkill() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);

        Skills skill = new Skills();
        skill.setSkillName("Old Skill");
        skill.setUsers(user);

        skill = skillsRepository.save(skill);

        SkillsDto updateDto = new SkillsDto();
        updateDto.setSkillName("Updated Skill");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SkillsDto> request = new HttpEntity<>(updateDto, headers);

        ResponseEntity<SkillsDto> response = restTemplate.exchange(
                getBaseUrl() + "/" + skill.getSkillID(),
                HttpMethod.PUT,
                request,
                SkillsDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSkillName()).isEqualTo("Updated Skill");
        assertThat(response.getBody().getUsers()).isNotNull();
        assertThat(response.getBody().getUsers().getEmail()).isEqualTo("barisaydogdu2@mail.com");
    }

    @Test
    @Order(5)
    void testGetSkillById() {
        Users user = new Users();
        user.setEmail("barisaydogdu2@mail.com");
        user.setPassword("1234567");
        user.setFirstName("Barış");
        user.setLastName("Aydoğdu");
        user.setRole(Role.ROLE_ADMIN);
        user = usersRepository.save(user);
        UsersDto usersDto = autoMapper.convertToDto(user,UsersDto.class);


        Skills skill = new Skills();
        skill.setSkillName("SkillX");
        skill.setUsers(user);
        skill = skillsRepository.save(skill);


        ResponseEntity<SkillsDto> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + skill.getSkillID(),
                SkillsDto.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getSkillName()).isEqualTo("SkillX");
        assertThat(response.getBody().getUsers()).isNotNull();
        assertThat(response.getBody().getUsers().getEmail()).isEqualTo("barisaydogdu2@mail.com");
    }
}
