package com.TechHub.studentmanagement.controller;

import com.TechHub.studentmanagement.model.Course;
import com.TechHub.studentmanagement.model.Student;
import com.TechHub.studentmanagement.model.User;
import com.TechHub.studentmanagement.repository.CourseRepository;
import com.TechHub.studentmanagement.repository.StudentRepository;
import com.TechHub.studentmanagement.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    // ✅ Add a new course (open to all for now)
    @PostMapping("/add")
    public Course addCourse(@RequestBody Course course) {
        return courseRepository.save(course);
    }

    // ✅ Assign course to student — Only TEACHER allowed
    @PostMapping("/students/{studentId}/courses/{courseId}")
    public ResponseEntity<String> assignCourseToStudent(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {

        // 🔐 Get logged-in user's username
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // 🔍 Fetch user and check role
        Optional<User> currentUser = userService.getUserByUsername(username);
        if (currentUser.isEmpty() || !"TEACHER".equalsIgnoreCase(currentUser.get().getRole())) {
            return ResponseEntity.status(403).body("Only TEACHERS can assign courses.");
        }

        // ✅ Proceed with assignment if valid
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Course> courseOpt = courseRepository.findById(courseId);

        if (studentOpt.isPresent() && courseOpt.isPresent()) {
            Student student = studentOpt.get();
            Course course = courseOpt.get();

            student.getCourses().add(course);
            course.getStudents().add(student);

            studentRepository.save(student);
            courseRepository.save(course);

            return ResponseEntity.ok("Course assigned successfully!");
        } else {
            return ResponseEntity.status(404).body("Student or Course not found!");
        }
    }

    // ✅ Get all available courses
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
 // ✅ Delete course by ID
    @DeleteMapping("/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
            return ResponseEntity.ok("Course deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Course not found.");
        }
    }

}
