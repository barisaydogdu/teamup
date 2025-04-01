package com.filepackage.controller;

import com.filepackage.dto.ProjectsDto;
import com.filepackage.entity.Users;
import com.filepackage.service.Impl.ProjectsService;
import com.filepackage.service.Impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
    @Autowired
    private ProjectsService projectService;
    private UsersService usersService;

    public ProjectsController(ProjectsService projectService, UsersService usersService) {
        this.projectService = projectService;
        this.usersService = usersService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectsDto>> getAllProjects() {
        List<ProjectsDto> projects = projectService.getAll();
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<ProjectsDto> addProject(@RequestBody ProjectsDto projectDto){
        //Users entrepreneur = usersService.getUsersAuthenticatedUser();
        //projectDto.setEntrepreneur_id(entrepreneur.getEntrepreneurId());

        Users user = new Users();
//        projectDto.setCreatorID(user.getId());
        ProjectsDto savedProject = projectService.create(projectDto);

//        savedProject.setCreatorID(user.getId());
        return  new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProjectsDto> getProjectById(@PathVariable("id") Long userId) {
        ProjectsDto projectDto = projectService.getById(userId);
        return ResponseEntity.ok(projectDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject (@PathVariable("id") Long userId) {
        projectService.delete(userId);
        return ResponseEntity.ok("Project deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<ProjectsDto> updateProject (@PathVariable("id") Long userId, @RequestBody ProjectsDto updatedProject) {
        ProjectsDto projectDto=projectService.update(userId,updatedProject);
        return ResponseEntity.ok(projectDto);
    }
}
