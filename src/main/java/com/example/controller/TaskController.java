package com.example.controller;

import com.example.dto.TaskDTO;
import com.example.dto.request.TaskRequest;
import com.example.dto.request.UpdateTaskRequest;
import com.example.service.task.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/Task")
public class TaskController {

    @Autowired
    private ITaskService taskService;

    @Operation(summary = "Get all tasks", description = "Retrieves the complete list of tasks. If there are no users, it returns an empty list. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, return the task list."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @GetMapping("/allTasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Get task by task number", description = "Retrieves a task by its task number.Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, return the task."),
            @ApiResponse(responseCode = "404", description = "Not found, task not found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @GetMapping("/byNumberTask/{numberTask}")
    public ResponseEntity<TaskDTO> getTaskByNumberTask(
            @Parameter(description = "The task identifier code", required = true, example = "TASK-1")
            @PathVariable String numberTask) {
        return ResponseEntity.ok(taskService.getTaskByNumberTask(numberTask));
    }

    @Operation(summary = "Get tasks by username", description = "Retrieves the list of tasks associated with a user. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, return the task list."),
            @ApiResponse(responseCode = "404", description = "Not found, user not found or no associated tasks."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @GetMapping("/byUser/{username}")
    public ResponseEntity<List<TaskDTO>> getTaskByUser(
            @Parameter(description = "The username for search", required = true, example = "user")
            @PathVariable String username) {
        return ResponseEntity.ok(taskService.getTaskByUser(username));
    }

    @Operation(summary = "Get tasks by status", description = "Retrieves the list of tasks associated with a state. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, return the task list."),
            @ApiResponse(responseCode = "404", description = "Not found, status not found or no associated tasks."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @GetMapping("/byStatus/{status}")
    public ResponseEntity<List<TaskDTO>> getTaskByStatus(
            @Parameter(description = "The status name for search", required = true, example = "on hold")
            @PathVariable String status) {
        return ResponseEntity.ok(taskService.getTaskByStatus(status));
    }

    @Operation(summary = "Create a new task", description = "Create a new task associated with a user. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, returns the created task."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "404", description = "Not found, user not found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @PostMapping("/create/{username}")
    public ResponseEntity<TaskDTO> createTask(
            @Parameter(description = "The user for whom a task is to be created", required = true, example = "user")
            @PathVariable String username, @RequestBody TaskRequest request) {
        return ResponseEntity.ok(taskService.createTask(username, request));
    }

    @Operation(summary = "Update a task", description = "Updates an existing task by its task number. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, returns the updated task."),
            @ApiResponse(responseCode = "400", description = "Bad Request."),
            @ApiResponse(responseCode = "404", description = "Not found, user not found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @PutMapping("/update/{numberTask}")
    public ResponseEntity<TaskDTO> updateTask(
            @Parameter(description = "The task identifier code to update", required = true, example = "TASK-1")
            @PathVariable String numberTask, @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(numberTask, request));
    }

    @Operation(summary = "Delete a task", description = "Delete an existing task by its task number. Need a token generated upon login or registration.")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success, the task has been deleted."),
            @ApiResponse(responseCode = "404", description = "Not found, task not found."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.")
    })
    @DeleteMapping("/delete/{numberTask}")
    public ResponseEntity<String> deleteTask(
            @Parameter(description = "The task identifier code to delete", required = true, example = "TASK-1")
            @PathVariable String numberTask) {
        taskService.deleteTask(numberTask);
        return ResponseEntity.ok("Task: " + numberTask + " has been deleted");
    }
}
