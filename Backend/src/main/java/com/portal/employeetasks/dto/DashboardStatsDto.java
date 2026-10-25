package com.portal.employeetasks.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsDto {

    private long totalEmployees;
    private long activeEmployees;
    private long onLeaveEmployees;
    private long totalTasks;
    private long todoTasks;
    private long inProgressTasks;
    private long inReviewTasks;
    private long completedTasks;
    private long urgentTasks;
    private long highTasks;
    private double completionRate;
    private Map<String, Long> employeesByDepartment;
    private Map<String, Long> tasksByPriority;
    private List<TaskResponseDto> recentTasks;

    public DashboardStatsDto() {
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(long activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public long getOnLeaveEmployees() {
        return onLeaveEmployees;
    }

    public void setOnLeaveEmployees(long onLeaveEmployees) {
        this.onLeaveEmployees = onLeaveEmployees;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public long getTodoTasks() {
        return todoTasks;
    }

    public void setTodoTasks(long todoTasks) {
        this.todoTasks = todoTasks;
    }

    public long getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(long inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public long getInReviewTasks() {
        return inReviewTasks;
    }

    public void setInReviewTasks(long inReviewTasks) {
        this.inReviewTasks = inReviewTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public long getUrgentTasks() {
        return urgentTasks;
    }

    public void setUrgentTasks(long urgentTasks) {
        this.urgentTasks = urgentTasks;
    }

    public long getHighTasks() {
        return highTasks;
    }

    public void setHighTasks(long highTasks) {
        this.highTasks = highTasks;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public Map<String, Long> getEmployeesByDepartment() {
        return employeesByDepartment;
    }

    public void setEmployeesByDepartment(Map<String, Long> employeesByDepartment) {
        this.employeesByDepartment = employeesByDepartment;
    }

    public Map<String, Long> getTasksByPriority() {
        return tasksByPriority;
    }

    public void setTasksByPriority(Map<String, Long> tasksByPriority) {
        this.tasksByPriority = tasksByPriority;
    }

    public List<TaskResponseDto> getRecentTasks() {
        return recentTasks;
    }

    public void setRecentTasks(List<TaskResponseDto> recentTasks) {
        this.recentTasks = recentTasks;
    }
}
