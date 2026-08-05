
package com.example.main.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.time.LocalDateTime;

@Entity
@Table
public class Student {

    @Id
    @Column(nullable = false)
    private Integer id;

    @Column
    @NotBlank(message = "Name is required")
    private String name;

    @Column
    @Min(value = 1, message = "Roll Number should be greater than 0")
    private int rollno;

    @Column(unique = true)
    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;

    @Column
    @NotBlank(message = "Password is required")
    @Size(min = 6,message = "Password must be at least 6 character")
    private String password;

    @Column
    private String mobileNo;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column
    @NotBlank(message = "department is required")
    private String department;

    @Column
    @NotBlank(message = "course is required")
    private String course;

    @Column
    @NotBlank(message = "city is required")
    private String city;

    @NotBlank(message = "gender is required")
    @Column
    private String gender;

    @Column
    private String status = "Active";

    @Column
    private LocalDateTime createdDate;

    @Column(length = 6)
    private String otp;

    @Column
    private LocalDateTime otpExpiry;

    @Column
    private String profilePhoto;

    @Column(name = "is_deleted",nullable = false)
    private boolean isDeleted = false;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRollno() {
        return rollno;
    }

    public void setRollno(int rollno) {
        this.rollno = rollno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }


}

