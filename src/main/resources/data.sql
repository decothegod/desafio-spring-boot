--Table Task status
INSERT INTO TASK_STATUS (id, status) VALUES ('1', 'on hold');
INSERT INTO TASK_STATUS (id, status) VALUES ('2', 'in progress');
INSERT INTO TASK_STATUS (id, status) VALUES ('3', 'complete');

--Table User
INSERT INTO USERS (id,username,password) VALUES ('10', 'user1', '$2a$10$M0mpYyFU2X9h3d7D/OjoSOz6j32sfLMn3WUG07A2pIFzOz/fcvu6q'); --Password: PasswordEncoder
INSERT INTO USERS (id,username,password) VALUES ('11', 'user2', '$2a$10$JYfXyWbYIvJllaLnf03MzubOw7EW13ruVDWc1b960LydTB4RjBK0m'); --Password: PasswordEncoder2