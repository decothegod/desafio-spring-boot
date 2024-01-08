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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TaskServiceTest {
    @InjectMocks
    TaskService taskService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TaskStatusRepository taskStatusRepository;
    @Mock
    private ModelMapper mapper;

    @Test
    public void createTask() {
        TaskRequest request = TaskRequest.builder()
                .title("title task")
                .description("description task")
                .build();

        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .username("userTest")
                .status("ON HOLD")
                .build();

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        when(taskStatusRepository.findByStatus(anyString())).thenReturn(Optional.ofNullable(taskStatus));
        when(mapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO response = taskService.createTask("userTest", request);
        assertNotNull(response);
        assertEquals("TASK-1", response.getNumberTask());
        assertEquals("title task", response.getTitle());
        assertEquals("description task", response.getDescription());
        assertEquals("userTest", response.getUsername());
        assertEquals("ON HOLD", response.getStatus());
    }

    @Test
    public void updateTask() {
        UpdateTaskRequest request = UpdateTaskRequest.builder()
                .title("title update task")
                .description("description updated task")
                .username("userTest2")
                .status("COMPLETE")
                .build();

        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("userTest2")
                .password("password")
                .build();

        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        TaskStatus taskStatusUpdated = TaskStatus.builder()
                .id(2L)
                .status("COMPLETE")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title update task")
                .description("description updated task")
                .username("userTest")
                .status("COMPLETE")
                .build();

        when(taskRepository.findByNumberTask(anyString())).thenReturn(Optional.ofNullable(task));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user2));
        when(taskStatusRepository.findByStatus(anyString())).thenReturn(Optional.ofNullable(taskStatusUpdated));
        when(mapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO response = taskService.updateTask("TASK-1", request);
        assertNotNull(response);
        assertEquals("TASK-1", response.getNumberTask());
        assertEquals("title update task", response.getTitle());
        assertEquals("description updated task", response.getDescription());
        assertEquals("userTest2", response.getUsername());
        assertEquals("COMPLETE", response.getStatus());
    }

    @Test
    public void deleteTask() {
        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        when(taskRepository.findByNumberTask(anyString())).thenReturn(Optional.ofNullable(task));

        taskService.deleteTask(anyString());
        verify(taskRepository, times(1)).delete(any(Task.class));
    }

    @Test
    public void getTaskByNumberTask() {
        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .username("userTest")
                .status("ON HOLD")
                .build();

        when(taskRepository.findByNumberTask(anyString())).thenReturn(Optional.ofNullable(task));
        when(taskRepository.searchByNumberTask(anyString())).thenReturn(task);
        when(mapper.map(any(Task.class), eq(TaskDTO.class))).thenReturn(taskDTO);

        TaskDTO response = taskService.getTaskByNumberTask(anyString());
        assertNotNull(response);
        assertEquals("TASK-1", response.getNumberTask());
        assertEquals("title task", response.getTitle());
        assertEquals("description task", response.getDescription());
        assertEquals("userTest", response.getUsername());
        assertEquals("ON HOLD", response.getStatus());
    }

    @Test
    public void getTaskByUser() {
        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .user(user)
                .status(taskStatus)
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .username("userTest")
                .status("ON HOLD")
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .username("userTest")
                .status("ON HOLD")
                .build();

        List<Task> tasks = new ArrayList<>(Arrays.asList(task, task2));

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(user));
        when(taskRepository.findByUsername(anyString())).thenReturn(tasks);
        when(mapper.map(task, TaskDTO.class)).thenReturn(taskDTO);
        when(mapper.map(task2, TaskDTO.class)).thenReturn(taskDTO2);

        List<TaskDTO> response = taskService.getTaskByUser(anyString());
        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    public void getTaskByStatus() {
        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .user(user)
                .status(taskStatus)
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .username("userTest")
                .status("ON HOLD")
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .username("userTest")
                .status("ON HOLD")
                .build();

        List<Task> tasks = new ArrayList<>(Arrays.asList(task, task2));

        when(taskStatusRepository.findByStatus(anyString())).thenReturn(Optional.ofNullable(taskStatus));
        when(taskRepository.findByTaskStatus(anyString())).thenReturn(tasks);
        when(mapper.map(task, TaskDTO.class)).thenReturn(taskDTO);
        when(mapper.map(task2, TaskDTO.class)).thenReturn(taskDTO2);

        List<TaskDTO> response = taskService.getTaskByStatus(anyString());
        assertNotNull(response);
        assertEquals(2, response.size());

    }

    @Test
    public void getAllTasks() {
        TaskStatus taskStatus = TaskStatus.builder()
                .id(1L)
                .status("ON HOLD")
                .build();

        User user = User.builder()
                .id(1L)
                .username("userTest")
                .password("password")
                .build();

        Task task = Task.builder()
                .id(1L)
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .user(user)
                .status(taskStatus)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .user(user)
                .status(taskStatus)
                .build();

        TaskDTO taskDTO = TaskDTO.builder()
                .numberTask("TASK-1")
                .title("title task")
                .description("description task")
                .username("userTest")
                .status("ON HOLD")
                .build();

        TaskDTO taskDTO2 = TaskDTO.builder()
                .numberTask("TASK-2")
                .title("title task 2")
                .description("description task 2")
                .username("userTest")
                .status("ON HOLD")
                .build();


        List<Task> tasks = new ArrayList<>(Arrays.asList(task, task2));

        when(taskRepository.findByAllTask()).thenReturn(tasks);
        when(mapper.map(task, TaskDTO.class)).thenReturn(taskDTO);
        when(mapper.map(task2, TaskDTO.class)).thenReturn(taskDTO2);

        List<TaskDTO> response = taskService.getAllTasks();
        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void checkExistUser_NotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        taskService.getTaskByUser(anyString());
    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void checkExistTask_NotFound() {
        when(taskRepository.findByNumberTask(anyString())).thenReturn(Optional.empty());

        taskService.getTaskByNumberTask(anyString());
    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void checkExistTaskStatus_NotFound() {
        when(taskStatusRepository.findByStatus(anyString())).thenReturn(Optional.empty());

        taskService.getTaskByStatus(anyString());
    }
}
