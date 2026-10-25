package com.portal.employeetasks.service;

import com.portal.employeetasks.dto.EmployeeRequestDto;
import com.portal.employeetasks.dto.EmployeeResponseDto;
import com.portal.employeetasks.dto.TaskResponseDto;
import com.portal.employeetasks.exception.BadRequestException;
import com.portal.employeetasks.exception.ResourceNotFoundException;
import com.portal.employeetasks.model.Employee;
import com.portal.employeetasks.model.EmployeeStatus;
import com.portal.employeetasks.model.Task;
import com.portal.employeetasks.model.TaskStatus;
import com.portal.employeetasks.repository.EmployeeRepository;
import com.portal.employeetasks.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final TaskRepository taskRepository;

    public EmployeeService(EmployeeRepository employeeRepository, TaskRepository taskRepository) {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponseDto> getAllEmployees(String search, String department, EmployeeStatus status) {
        String cleanSearch = (search != null && !search.trim().isEmpty()) ? search.trim() : null;
        String cleanDepartment = (department != null && !department.trim().isEmpty() && !department.equalsIgnoreCase("ALL")) ? department.trim() : null;

        List<Employee> employees = employeeRepository.searchEmployees(cleanSearch, cleanDepartment, status);
        return employees.stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeResponseDto getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponseDto(employee);
    }

    public EmployeeResponseDto createEmployee(EmployeeRequestDto dto) {
        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("An employee with email '" + dto.getEmail() + "' already exists.");
        }

        Employee employee = new Employee();
        mapDtoToEntity(dto, employee);
        Employee saved = employeeRepository.save(employee);
        return mapToResponseDto(saved);
    }

    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (employeeRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new BadRequestException("Another employee already uses the email '" + dto.getEmail() + "'.");
        }

        mapDtoToEntity(dto, employee);
        Employee updated = employeeRepository.save(employee);
        return mapToResponseDto(updated);
    }

    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        // Unassign tasks or keep tasks detached
        List<Task> assignedTasks = taskRepository.findByAssignedEmployeeId(id);
        for (Task task : assignedTasks) {
            task.setAssignedEmployee(null);
            taskRepository.save(task);
        }

        employeeRepository.delete(employee);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getEmployeeTasks(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with id: " + employeeId);
        }
        List<Task> tasks = taskRepository.findByAssignedEmployeeId(employeeId);
        return tasks.stream().map(this::mapTaskToDto).collect(Collectors.toList());
    }

    private void mapDtoToEntity(EmployeeRequestDto dto, Employee employee) {
        employee.setFirstName(dto.getFirstName().trim());
        employee.setLastName(dto.getLastName().trim());
        employee.setEmail(dto.getEmail().trim().toLowerCase());
        employee.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : null);
        employee.setDepartment(dto.getDepartment().trim());
        employee.setPosition(dto.getPosition().trim());
        employee.setSalary(dto.getSalary());
        employee.setStatus(dto.getStatus() != null ? dto.getStatus() : EmployeeStatus.ACTIVE);
        employee.setAvatarUrl(dto.getAvatarUrl());
    }

    public EmployeeResponseDto mapToResponseDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setDepartment(employee.getDepartment());
        dto.setPosition(employee.getPosition());
        dto.setSalary(employee.getSalary());
        dto.setStatus(employee.getStatus());
        dto.setAvatarUrl(employee.getAvatarUrl());
        dto.setCreatedAt(employee.getCreatedAt());
        dto.setUpdatedAt(employee.getUpdatedAt());

        List<Task> tasks = taskRepository.findByAssignedEmployeeId(employee.getId());
        dto.setTotalTasks(tasks.size());
        dto.setCompletedTasks((int) tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count());
        dto.setPendingTasks(dto.getTotalTasks() - dto.getCompletedTasks());

        return dto;
    }

    private TaskResponseDto mapTaskToDto(Task task) {
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
