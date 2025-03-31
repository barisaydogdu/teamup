package com.filepackage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "admin_actions",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class AdminActions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_id")
    private Long action_id;

    @Column(name = "admin_id")
    private Integer admin_id;

    @Column(name = "action_type")
    private String action_type;

    @Column(name = "target_table")
    private String target_table;

    @Column(name = "target_id")
    private Integer target_id;

    @Column(name = "action_description")
    private String action_description;

    @Column(name= "action_timestamp")
    private LocalDateTime action_timestamp;

    @Column(name = "user_id")
    private Integer user_id;

    @PrePersist
    protected void onCreate(){this.action_timestamp = LocalDateTime.now();}

}
