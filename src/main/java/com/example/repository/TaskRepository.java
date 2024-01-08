package com.example.repository;

import com.example.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByNumberTask(String numberTask);

    @Query("""
            FROM Task t
            JOIN t.user u
            JOIN t.status ts
            WHERE t.numberTask = :numberTask""")
    Task searchByNumberTask(@Param("numberTask") String numberTask);

    @Query("""
            FROM Task t
            JOIN t.user u
            JOIN t.status ts""")
    List<Task> findByAllTask();

    @Query("""
            FROM Task t
            JOIN t.user u
            JOIN t.status ts
            WHERE ts.status = :status""")
    List<Task> findByTaskStatus(@Param("status") String status);

    @Query("""
            FROM Task t
            JOIN t.user u
            JOIN t.status ts
            WHERE u.username = :username""")
    List<Task> findByUsername(@Param("username") String username);
}
