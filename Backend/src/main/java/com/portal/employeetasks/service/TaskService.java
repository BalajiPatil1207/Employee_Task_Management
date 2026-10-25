package com.portal.employeetasks.service;

import com.portal.employeetasks.dto.TaskRequestDto;
import com.portal.employeetasks.dto.TaskResponseDto;
import com.portal.employeetasks.exception.ResourceNotFoundException;
import com.portal.employeetasks.model.Employee;
import com.portal.employeetasks.model.Task;
import com.portal.employeetasks.model.TaskPriority;
import com.portal.employeetasks.model.TaskStatus;
import com.portal.employeetasks.repository.EmployeeRepository;
import com.portal.employeetasks.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllTasks(TaskStatus status, TaskPriority priority, Long employeeId, String search) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        List<Task> tasks = taskRepository.searchTasks(cleanSearch, status, priority, employeeId);
        return tasks.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return mapToResponseDto(task);
    }

    public TaskResponseDto createTask(TaskRequestDto dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle().trim());
        task.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIUM);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO);
        task.setDueDate(dto.getDueDate());
        task.setEstimatedHours(dto.getEstimatedHours());

        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned employee not found with id: " + dto.getEmployeeId()));
            task.setAssignedEmployee(employee);
        }

        Task saved = taskRepository.save(task);
        return mapToResponseDto(saved);
    }

    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        task.setTitle(dto.getTitle().trim());
        task.setDescription(dto.getDescription() != null ? dto.getDescription().trim() : null);
        task.setPriority(dto.getPriority() != null ? dto.getPriority() : TaskPriority.MEDIUM);
        task.setStatus(dto.getStatus() != null ? dto.getStatus() : TaskStatus.TODO);
        task.setDueDate(dto.getDueDate());
        task.setEstimatedHours(dto.getEstimatedHours());

        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned employee not found with id: " + dto.getEmployeeId()));
            task.setAssignedEmployee(employee);
        } else {
            task.setAssignedEmployee(null);
        }

        Task updated = taskRepository.save(task);
        return mapToResponseDto(updated);
    }

    public TaskResponseDto updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return mapToResponseDto(updated);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskResponseDto mapToResponseDto(Task task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setEstimatedHours(task.getEstimatedHours());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        if (task.getAssignedEmployee() != null) {
            dto.setEmployeeId(task.getAssignedEmployee().getId());
            dto.setEmployeeName(task.getAssignedEmployee().getFullName());
            dto.setEmployeeEmail(task.getAssignedEmployee().getEmail());
            dto.setEmployeeDepartment(task.getAssignedEmployee().getDepartment());
            dto.setEmployeeAvatarUrl(task.getAssignedEmployee().getAvatarUrl());
        }

        return dto;
    }
}
