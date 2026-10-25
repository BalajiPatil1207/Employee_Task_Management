package com.portal.employeetasks.config;

import com.portal.employeetasks.model.*;
import com.portal.employeetasks.repository.EmployeeRepository;
import com.portal.employeetasks.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    public DataInitializer(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        if (employeeRepository.count() > 0) {
            return;
        }

        // Initialize realistic employees
        Employee emp1 = new Employee("Alex", "Morgan", "alex.morgan@workpulse.io", "+1 (555) 234-5678",
                "Engineering", "Lead Full Stack Architect", 125000.0, EmployeeStatus.ACTIVE,
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80");

        Employee emp2 = new Employee("Marcus", "Chen", "marcus.chen@workpulse.io", "+1 (555) 345-6789",
                "Engineering", "Senior DevOps & Cloud Engineer", 118000.0, EmployeeStatus.ACTIVE,
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80");

        Employee emp3 = new Employee("Sophia", "Rodriguez", "sophia.rodriguez@workpulse.io", "+1 (555) 456-7890",
                "Design", "Principal UI/UX Designer", 105000.0, EmployeeStatus.ACTIVE,
                "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80");

        Employee emp4 = new Employee("David", "Kim", "david.kim@workpulse.io", "+1 (555) 567-8901",
                "Product", "Senior Technical Product Manager", 115000.0, EmployeeStatus.ACTIVE,
                "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&auto=format&fit=crop&q=80");

        Employee emp5 = new Employee("Elena", "Rostova", "elena.rostova@workpulse.io", "+1 (555) 678-9012",
                "Marketing", "Growth Marketing Director", 98000.0, EmployeeStatus.ON_LEAVE,
                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=150&auto=format&fit=crop&q=80");

        Employee emp6 = new Employee("Jordan", "Taylor", "jordan.taylor@workpulse.io", "+1 (555) 789-0123",
                "Human Resources", "People Operations Manager", 88000.0, EmployeeStatus.ACTIVE,
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&auto=format&fit=crop&q=80");

        List<Employee> savedEmployees = employeeRepository.saveAll(Arrays.asList(emp1, emp2, emp3, emp4, emp5, emp6));

        // Initialize realistic tasks
        LocalDate today = LocalDate.now();

        Task t1 = new Task("Architect Real-Time Notification Pipeline",
                "Design and implement WebSocket / SSE gateway for push updates across distributed client sessions.",
                TaskPriority.HIGH, TaskStatus.IN_PROGRESS, today.plusDays(4), 16, emp1);

        Task t2 = new Task("Audit Spring Security 6 RBAC Endpoints",
                "Perform penetration testing and audit token refresh mechanics with CSRF protection headers.",
                TaskPriority.URGENT, TaskStatus.IN_PROGRESS, today.plusDays(2), 8, emp1);

        Task t3 = new Task("Setup Kubernetes Helm Charts for Staging Cluster",
                "Containerize microservices, write Helm values configurations, and configure auto-scaling triggers.",
                TaskPriority.HIGH, TaskStatus.TODO, today.plusDays(7), 24, emp2);

        Task t4 = new Task("Optimize CI/CD GitHub Actions Pipeline",
                "Cache Gradle & Maven build dependencies to reduce pipeline execution times from 12m to under 3m.",
                TaskPriority.MEDIUM, TaskStatus.COMPLETED, today.minusDays(2), 10, emp2);

        Task t5 = new Task("Design Glassmorphism Dashboard Component Library",
                "Create reusable design system tokens, color palettes, and Figma component specs for the portal.",
                TaskPriority.HIGH, TaskStatus.COMPLETED, today.minusDays(1), 20, emp3);

        Task t6 = new Task("Conduct Usability Testing for Task Board Kanban",
                "Run 5 user interviews to validate drag-and-drop feedback and quick status filter affordances.",
                TaskPriority.MEDIUM, TaskStatus.IN_REVIEW, today.plusDays(3), 12, emp3);

        Task t7 = new Task("Q3 Product Roadmap Milestone Definition",
                "Synthesize customer feedback and align engineering sprint targets with quarterly OKRs.",
                TaskPriority.URGENT, TaskStatus.IN_PROGRESS, today.plusDays(1), 14, emp4);

        Task t8 = new Task("Competitor Feature Matrix & Benchmarking",
                "Analyze competing enterprise project management and HR portals to identify differentiator features.",
                TaskPriority.LOW, TaskStatus.TODO, today.plusDays(10), 18, emp4);

        Task t9 = new Task("Launch Product Hunt & TechCrunch Campaign",
                "Coordinate outreach assets, press releases, demo videos, and landing page conversion trackers.",
                TaskPriority.HIGH, TaskStatus.TODO, today.plusDays(14), 30, emp5);

        Task t10 = new Task("Revamp Engineering Onboarding Handbook",
                "Update development setup guides, Git branching strategy, and team communication guidelines.",
                TaskPriority.MEDIUM, TaskStatus.COMPLETED, today.minusDays(4), 8, emp6);

        Task t11 = new Task("Automate Leave Request & Performance Review Flows",
                "Implement automated approval notifications and policy compliance checks in HR portal.",
                TaskPriority.LOW, TaskStatus.TODO, today.plusDays(12), 15, emp6);

        taskRepository.saveAll(Arrays.asList(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));

        System.out.println(">>> DataInitializer: Successfully populated demo employees and tasks database!");
    }
}
