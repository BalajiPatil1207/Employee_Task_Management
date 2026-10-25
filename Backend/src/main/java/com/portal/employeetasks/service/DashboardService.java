package com.portal.employeetasks.service;

import com.portal.employeetasks.dto.DashboardStatsDto;
import com.portal.employeetasks.dto.TaskResponseDto;
import com.portal.employeetasks.model.Employee;
import com.portal.employeetasks.model.EmployeeStatus;
import com.portal.employeetasks.model.Task;
import com.portal.employeetasks.model.TaskPriority;
import com.portal.employeetasks.model.TaskStatus;
import com.portal.employeetasks.repository.EmployeeRepository;
import com.portal.employeetasks.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    public DashboardService(EmployeeRepository employeeRepository, TaskRepository taskRepository, TaskService taskService) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.taskService = taskService;
    }

    public DashboardStatsDto getDashboardStats() {
        DashboardStatsDto dto = new DashboardStatsDto();

        long totalEmployees = employeeRepository.count();
        long activeEmployees = employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        long onLeaveEmployees = employeeRepository.countByStatus(EmployeeStatus.ON_LEAVE);

        long totalTasks = taskRepository.count();
        long todoTasks = taskRepository.countByStatus(TaskStatus.TODO);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long inReviewTasks = taskRepository.countByStatus(TaskStatus.IN_REVIEW);
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);

        long urgentTasks = taskRepository.countByPriority(TaskPriority.URGENT);
        long highTasks = taskRepository.countByPriority(TaskPriority.HIGH);

        double completionRate = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100.0 : 0.0;

        // Department breakdown
        List<Employee> allEmployees = employeeRepository.findAll();
        Map<String, Long> deptMap = allEmployees.stream()
                .filter(e -> e.getDepartment() != null)
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()));

        // Tasks priority breakdown
        Map<String, Long> priorityMap = new HashMap<>();
        for (TaskPriority priority : TaskPriority.values()) {
            priorityMap.put(priority.name(), taskRepository.countByPriority(priority));
        }

        List<Task> recent = taskRepository.findTop10ByOrderByCreatedAtDesc();
        List<TaskResponseDto> recentDtos = recent.stream().map(taskService::mapToResponseDto).collect(Collectors.toList());

        dto.setTotalEmployees(totalEmployees);
        dto.setActiveEmployees(activeEmployees);
        dto.setOnLeaveEmployees(onLeaveEmployees);

        dto.setTotalTasks(totalTasks);
        dto.setTodoTasks(todoTasks);
        dto.setInProgressTasks(inProgressTasks);
        dto.setInReviewTasks(inReviewTasks);
        dto.setCompletedTasks(completedTasks);

        dto.setUrgentTasks(urgentTasks);
        dto.setHighTasks(highTasks);
        dto.setCompletionRate(Math.round(completionRate * 10.0) / 10.0);

        dto.setEmployeesByDepartment(deptMap);
        dto.setTasksByPriority(priorityMap);
        dto.setRecentTasks(recentDtos);

        return dto;
    }
}
