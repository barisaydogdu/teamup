package com.filepackage.service.Impl;

import com.filepackage.dto.ProjectsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Projects;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IProjectsRepository;
import com.filepackage.service.IProjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectsService implements IProjectsService<ProjectsDto,Long> {
    @Autowired
    public AutoMapper autoMapper;
    private IProjectsRepository projectsRepository;
    private UsersService usersService;

    @Autowired
    public ProjectsService(IProjectsRepository projectsRepository) {
        this.projectsRepository = projectsRepository;
    }

    @Override
    public ProjectsDto getById(Long id) {
        Projects projects = projectsRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Project does not exist with given id"+ id));
        return autoMapper.convertToDto(projects,ProjectsDto.class);
    }

    @Override
    public List<ProjectsDto> getAll() {
        List<Projects> projects = projectsRepository.findAll();


        return projects.stream().map(projectsObj -> {
            ProjectsDto projectsDto = autoMapper.convertToDto(projectsObj, ProjectsDto.class);
            if (projectsObj.getUser() != null) {
                UsersDto usersDto = autoMapper.convertToDto(projectsObj.getUser(), UsersDto.class);
                projectsDto.setUser(usersDto);
            }
            return projectsDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        Projects project = projectsRepository.findById(id).
                orElseThrow(()-> new ResourceNotFoundException("Project does not exist with given id"));
        projectsRepository.deleteById(id);
    }

    @Override
    public ProjectsDto update(Long projectId, ProjectsDto updatedProject) {
        Projects project= projectsRepository.findById(projectId).orElseThrow(() ->
                new ResourceNotFoundException("Project is not exist with given id:)" + projectId));
        if (updatedProject.getTitle() != null)
            project.setTitle(updatedProject.getTitle());

        if (updatedProject.getShortDescription() != null)
            project.setShortDescription(updatedProject.getShortDescription());

        if (updatedProject.getDetailedDescription() != null)
            project.setDetailedDescription(updatedProject.getDetailedDescription());

        if (updatedProject.getStatus() != null)
            project.setStatus(updatedProject.getStatus());

        if (updatedProject.getVisibility() != null)
            project.setVisibility(updatedProject.getVisibility());

        if (updatedProject.getStartDate() != null)
            project.setStartDate(updatedProject.getStartDate());

        if (updatedProject.getDateestimatedEndDate() != null)
            project.setDateestimatedEndDate(updatedProject.getDateestimatedEndDate());

        if (updatedProject.getMaxTeam() != null)
            project.setMaxTeam(updatedProject.getMaxTeam());

        Projects updated = projectsRepository.save(project);
        return autoMapper.convertToDto(updated, ProjectsDto.class);
    }

    @Override
    public ProjectsDto create(ProjectsDto dto) {
        Projects projects = autoMapper.convertToEntity(dto, Projects.class);

        Projects savedProject = projectsRepository.save(projects);

        return autoMapper.convertToDto(savedProject, ProjectsDto.class);
    }
}
