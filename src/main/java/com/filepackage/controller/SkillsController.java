package com.filepackage.controller;

import com.filepackage.dto.SkillsDto;
import com.filepackage.dto.UsersDto;
import com.filepackage.repository.IUsersRepository;
import com.filepackage.service.Impl.SkillsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
public class SkillsController {
    @Autowired
    private SkillsService skillsService;
    private final IUsersRepository usersRepository;

    public SkillsController(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public ResponseEntity<List<SkillsDto>> getAllSkills() {
        List<SkillsDto> skills = skillsService.getAll();
        return ResponseEntity.ok(skills);
    }

    @PostMapping
    public ResponseEntity<SkillsDto> addSkill(@RequestBody SkillsDto skillsDto) {
        SkillsDto savedSkill = skillsService.create(skillsDto);
        return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<SkillsDto> getSkillById(@PathVariable("id") Long skillID) {
        SkillsDto skillsDto = skillsService.getById(skillID);
        return ResponseEntity.ok(skillsDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable("id") Long skillID) {
        skillsService.delete(skillID);
        return ResponseEntity.ok("Skill deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<SkillsDto> updateUser (@PathVariable("id") Long skillID, @RequestBody SkillsDto updatedSkill) {
        SkillsDto skillsDto=skillsService.update(skillID,updatedSkill);
        return ResponseEntity.ok(skillsDto);
    }


}
