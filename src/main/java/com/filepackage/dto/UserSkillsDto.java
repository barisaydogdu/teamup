package com.filepackage.dto;

import com.filepackage.entity.Skills;
import com.filepackage.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSkillsDto {

    private Long userSkillID;

    private Integer user_id;

    private Users user;

    private Integer skill_id;

    private Skills skills;

    private String proficiency_level;

}
