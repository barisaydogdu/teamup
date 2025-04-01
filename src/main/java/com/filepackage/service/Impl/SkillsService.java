package com.filepackage.service.Impl;

import com.filepackage.dto.ProjectsDto;
import com.filepackage.dto.SkillsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Projects;
import com.filepackage.entity.Skills;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.ISkillsRepository;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.IProjectsService;
import com.filepackage.service.ISkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillsService implements ISkillsService<SkillsDto, Long> {

    @Autowired
    public AutoMapper autoMapper;
    @Autowired
    private ISkillsRepository skillsRepository;
    @Autowired
    public IUsersRepository usersRepository;

    @Autowired
    public SkillsService(ISkillsRepository skillsRepository) {
        this.skillsRepository = skillsRepository;
    }

    @Override
    public SkillsDto getById(Long skillID) {
        Skills skills = skillsRepository.findById(skillID).
        orElseThrow(()-> new ResourceNotFoundException("Skills does not exist with given id"+ skillID));
        return autoMapper.convertToDto(skills,SkillsDto.class);
    }

    @Override
    public List<SkillsDto> getAll() {
       List<Skills> skills = skillsRepository.findAll();

       return skills.stream().map(skillsObj ->{
           SkillsDto skillsDto = autoMapper.convertToDto(skillsObj,SkillsDto.class);
           if(skillsObj.getUsers() != null) {
               UsersDto usersDto = autoMapper.convertToDto(skillsObj.getUsers(), UsersDto.class);
               skillsDto.setUsers(usersDto);
           }
           return skillsDto;
       }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long aLong) {
        Skills project = skillsRepository.findById(aLong).
                orElseThrow(()-> new ResourceNotFoundException("Skill does not exist with given id"));
        skillsRepository.deleteById(aLong);
    }

    @Override
    public SkillsDto update(Long skillID, SkillsDto updatedSkills) {
        Skills skills= skillsRepository.findById(skillID).orElseThrow(() ->
                new ResourceNotFoundException("Project is not exist with given id:)" + skillID));
        if (updatedSkills.getSkillName() != null)
            skills.setSkillName(updatedSkills.getSkillName());

        Skills updated = skillsRepository.save(skills);
        return autoMapper.convertToDto(updated, SkillsDto.class);
    }

    @Override
    public SkillsDto create(SkillsDto dto) {
        Skills skills = autoMapper.convertToEntity(dto, Skills.class);
        Skills savedSkills = skillsRepository.save(skills);

        return  autoMapper.convertToDto(savedSkills, SkillsDto.class);
    }
}
