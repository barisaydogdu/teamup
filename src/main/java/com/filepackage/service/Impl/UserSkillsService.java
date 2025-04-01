package com.filepackage.service.Impl;

import com.filepackage.mapper.AutoMapper;
import com.filepackage.repository.ISkillsRepository;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.ISkillsService;
import com.filepackage.service.IUserSkillsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserSkillsService implements IUserSkillsService {
    @Autowired
    public AutoMapper autoMapper;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private ISkillsRepository skillsRepository;

    @Autowired
    private UserSkillsService(IUsersRepository usersRepository,ISkillsRepository skillsRepository) {
        this.usersRepository = usersRepository;
        this.skillsRepository = skillsRepository;
    }
    @Override
    public Object getById(Object o) {
        return null;
    }

    @Override
    public List getAll() {
        return List.of();
    }

    @Override
    public void delete(Object o) {

    }

    @Override
    public Object update(Object o, Object updatedEntity) {
        return null;
    }

    @Override
    public Object create(Object dto) {
        return null;
    }
}
