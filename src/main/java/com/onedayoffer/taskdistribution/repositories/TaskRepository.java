package com.onedayoffer.taskdistribution.repositories;

import com.onedayoffer.taskdistribution.repositories.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t " +
            "JOIN FETCH t.employee e " +
            "WHERE e.id = :employeeId")
    List<Task> findAllByEmployeeId(Integer employeeId);
}
