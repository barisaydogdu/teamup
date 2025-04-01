package com.filepackage.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "projects", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Projects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectID;

//    @Column(name = "creator_id")
//    private Long creatorID;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @Column(name = "title")
    private String title;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "detailed_description")
    private String detailedDescription;

    @Column(name = "category_id")
    private Integer categoryID;

    @Column(name = "status")
    private String status;

    @Column(name = "visibility")
    private String visibility;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "estimated_end_date")
    private Date dateestimatedEndDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "max_team")
    private LocalDateTime maxTeam;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public Long getProjectID() {
        return projectID;
    }

    public void setProjectID(Long projectID) {
        this.projectID = projectID;
    }

//    public Long getCreatorID() {
//        return creatorID;
//    }

//    public void setCreatorID(Long creatorID) {
//        this.creatorID = creatorID;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDateestimatedEndDate() {
        return dateestimatedEndDate;
    }

    public void setDateestimatedEndDate(Date dateestimatedEndDate) {
        this.dateestimatedEndDate = dateestimatedEndDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getMaxTeam() {
        return maxTeam;
    }

    public void setMaxTeam(LocalDateTime maxTeam) {
        this.maxTeam = maxTeam;
    }


}
