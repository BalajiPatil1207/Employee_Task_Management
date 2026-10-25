package com.portal.employeetasks.repository;

import com.portal.employeetasks.model.Task;
import com.portal.employeetasks.model.TaskPriority;
import com.portal.employeetasks.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAssignedEmployeeId(Long employeeId);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    long countByStatus(TaskStatus status);

    long countByPriority(TaskPriority priority);

    @Query("SELECT t FROM Task t WHERE " +
           "(:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:employeeId IS NULL OR (t.assignedEmployee IS NOT NULL AND t.assignedEmployee.id = :employeeId)) " +
           "ORDER BY t.dueDate ASC NULLS LAST, t.id DESC")
    List<Task> searchTasks(
            @Param("search") String search,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("employeeId") Long employeeId
    );

    List<Task> findTop10ByOrderByCreatedAtDesc();
}
