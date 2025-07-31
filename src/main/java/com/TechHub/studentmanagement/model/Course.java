package com.TechHub.studentmanagement.model;

import jakarta.persistence.*;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseName;
    private String description;

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore //  This avoids infinite loop during serialization
    private List<Student> students = new ArrayList<>();

    // Constructors
    public Course() {}

    public Course(String courseName, String description) {
        this.courseName = courseName;
        this.description = description;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
    
 //  Add these to help with duplicate check
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    
}
