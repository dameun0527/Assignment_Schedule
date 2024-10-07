DROP TABLE If Exists schedules;

# 일반 ver.
CREATE TABLE schedules(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    title VARCHAR(255),
    password VARCHAR(255),
    contents VARCHAR(255),
    created_at VARCHAR(10),
    updated_at VARCHAR(10)
);
