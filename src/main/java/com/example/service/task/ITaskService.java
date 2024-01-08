package com.example.service.task;

import com.example.dto.TaskDTO;
import com.example.dto.request.TaskRequest;
import com.example.dto.request.UpdateTaskRequest;

import java.util.List;

public interface ITaskService {

    TaskDTO createTask(String username, TaskRequest request);

    TaskDTO updateTask(String numberTask, UpdateTaskRequest request);

    void deleteTask(String numberTask);

    TaskDTO getTaskByNumberTask(String numberTask);

    List<TaskDTO> getTaskByUser(String username);

    List<TaskDTO> getTaskByStatus(String status);

    List<TaskDTO> getAllTasks();
}
