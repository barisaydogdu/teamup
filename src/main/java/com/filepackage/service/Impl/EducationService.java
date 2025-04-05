package com.filepackage.service.Impl;

import com.filepackage.dto.EducationDto;
import com.filepackage.dto.SkillsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.entity.Education;
import com.filepackage.entity.Skills;
import com.filepackage.exception.ResourceNotFoundException;
import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.IEducationRepository;
import com.filepackage.repository.ISkillsRepository;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.IEducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EducationService implements IEducationService<EducationDto,Long> {

    @Autowired
    public AutoMapper autoMapper;
    private IEducationRepository educationRepository;
    public IUsersRepository usersRepository;

    @Autowired
    public EducationService(IEducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }


    @Override
    public EducationDto getById(Long educationID) {
        Education education = educationRepository.findById(educationID).
                orElseThrow(()-> new ResourceNotFoundException("Education does not exist with given id"+ educationID));
        return autoMapper.convertToDto(education,EducationDto.class);
    }

    @Override
    public List<EducationDto> getAll() {
        List<Education> education = educationRepository.findAll();

        return education.stream().map(educationObj ->{
            EducationDto educationDto = autoMapper.convertToDto(educationObj,EducationDto.class);
            if(educationObj.getUser() != null) {
                UsersDto usersDto = autoMapper.convertToDto(educationObj.getUser(), UsersDto.class);
                educationDto.setUser(usersDto);
            }
            return educationDto;
        }).collect(Collectors.toList());
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public EducationDto update(Long aLong, EducationDto updatedEntity) {
        return null;
    }

    @Override
    public EducationDto create(EducationDto dto) {
        return null;
    }
}
