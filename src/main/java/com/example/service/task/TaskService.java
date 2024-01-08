package com.example.service.task;

import com.example.dto.TaskDTO;
import com.example.dto.request.TaskRequest;
import com.example.dto.request.UpdateTaskRequest;
import com.example.exception.ServiceExceptionNotFound;
import com.example.model.Task;
import com.example.model.TaskStatus;
import com.example.model.User;
import com.example.repository.TaskRepository;
import com.example.repository.TaskStatusRepository;
import com.example.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.util.Constants.*;

@Service
public class TaskService implements ITaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);
    private static final String ON_HOLD = "on hold";
    private static final String TASK = "TASK";
    private static int nextSequence = 1;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TaskDTO createTask(String username, TaskRequest request) {
        checkExistUser(username);
        User user = getUser(username);
        TaskStatus status = getStatus(ON_HOLD);

        Task task = Task.builder()
                .numberTask(generateUniqueCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .status(status)
                .build();

        taskRepository.save(task);

        return convertToDto(task);
    }

    @Override
    public TaskDTO updateTask(String numberTask, UpdateTaskRequest request) {
        checkExistTask(numberTask);
        Task task = getTask(numberTask);

        Task updateTask = Task.builder()
                .id(task.getId())
                .numberTask(task.getNumberTask())
                .title(task.getTitle())
                .description(task.getDescription())
                .user(task.getUser())
                .status(task.getStatus())
                .build();

        if (!StringUtils.isEmpty(request.getTitle())) {
            updateTask.setTitle(request.getTitle());
        }
        if (!StringUtils.isEmpty(request.getDescription())) {
            updateTask.setDescription(request.getDescription());
        }
        if (!StringUtils.isEmpty(request.getStatus())) {
            task.setStatus(getStatus(task.getStatus().getStatus()));
        }
        if (!StringUtils.isEmpty(request.getUsername())) {
            task.setUser(getUser(task.getUser().getUsername()));
        }

        taskRepository.save(updateTask);

        return convertToDto(task);
    }

    @Override
    public void deleteTask(String numberTask) {
        checkExistTask(numberTask);
        Task task = getTask(numberTask);

        taskRepository.delete(task);

    }

    @Override
    public TaskDTO getTaskByNumberTask(String numberTask) {
        checkExistTask(numberTask);
        return convertToDto(taskRepository.searchByNumberTask(numberTask));
    }

    @Override
    public List<TaskDTO> getTaskByUser(String username) {
        checkExistUser(username);
        return taskRepository.findByUsername(username).stream().map(this::convertToDto).toList();
    }

    @Override
    public List<TaskDTO> getTaskByStatus(String status) {
        checkExistTaskStatus(status);
        return taskRepository.findByTaskStatus(status).stream().map(this::convertToDto).toList();
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findByAllTask().stream().map(this::convertToDto).toList();
    }

    private TaskDTO convertToDto(Task task) {
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        taskDTO.setUsername(task.getUser().getUsername());
        taskDTO.setStatus(task.getStatus().getStatus());
        return taskDTO;
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        return modelMapper.map(taskDTO, Task.class);
    }

    private String generateUniqueCode() {
        String uniqueCode = String.format("%s-%d", TaskService.TASK, nextSequence);
        nextSequence++;
        return uniqueCode;
    }

    private void checkExistUser(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            LOG.error(USER_NOT_FOUND_MSG);
            throw new ServiceExceptionNotFound(USER_NOT_FOUND_MSG);
        }
    }

    private void checkExistTask(String numberTask) {
        if (taskRepository.findByNumberTask(numberTask).isEmpty()) {
            LOG.error(TASK_NOT_FOUND_MSG);
            throw new ServiceExceptionNotFound(TASK_NOT_FOUND_MSG);
        }
    }

    private void checkExistTaskStatus(String status) {
        if (taskStatusRepository.findByStatus(status).isEmpty()) {
            LOG.error(TASK_STATUS_NOT_FOUND_MSG);
            throw new ServiceExceptionNotFound(TASK_STATUS_NOT_FOUND_MSG);
        }
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceExceptionNotFound(USER_NOT_FOUND_MSG));
    }

    private Task getTask(String numberTask) {
        return taskRepository.findByNumberTask(numberTask)
                .orElseThrow(() -> new ServiceExceptionNotFound(TASK_NOT_FOUND_MSG));
    }

    private TaskStatus getStatus(String status) {
        return taskStatusRepository.findByStatus(status)
                .orElseThrow(() -> new ServiceExceptionNotFound(TASK_STATUS_NOT_FOUND_MSG));
    }
}
