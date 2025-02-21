package com.example.hotrovieclam.Model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import android.graphics.Color;


public class Job implements Serializable {
    private String id;
    private String employerId;
    private String avatar; // ref to employers.user_id
    // private Source source;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private int jobTypeId; // ref to job_types.id
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private String location;
    private int sourceId; // ref to source.id
    private float salaryMin = -1.0f;
    private float salaryMax = -1.0f;
    private String createdAt;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    private String jobURL;

    private String major;
    private String agreement;
    //private com.google.firebase.Timestamp createdAt;
    private Timestamp updatedAt;

//    public Source getSource() {
//        return source;
//    }
//
//    public void setSource(Source source) {
//        this.source = source;
//    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployerId() {
        return employerId;
    }

    public float getSalaryMin() {
        return salaryMin;
    }

    public void setSalaryMin(float salaryMin) {
        this.salaryMin = salaryMin;
    }

    public float getSalaryMax() {
        return salaryMax;
    }

    public void setSalaryMax(float salaryMax) {
        this.salaryMax = salaryMax;
    }

    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public int getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(int jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getJobURL() {
        return jobURL;
    }

    public void setJobURL(String jobURL) {
        this.jobURL = jobURL;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

//    public com.google.firebase.Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(com.google.firebase.Timestamp createdAt) {
//        this.createdAt = createdAt;
//    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", employerId='" + employerId + '\'' +
                ", jobTypeId=" + jobTypeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", location='" + location + '\'' +
                ", sourceId=" + sourceId +
                ", agreement='" + agreement + '\'' +
                ", major='" + major + '\'' +
                ", salaryMin='" + salaryMin + '\'' +
                ", salaryMax='" + salaryMax + '\'' +
                ", jobURL='" + jobURL + '\'' +
                //  ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}

