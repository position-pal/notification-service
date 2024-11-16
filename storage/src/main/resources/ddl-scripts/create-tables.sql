CREATE DATABASE notifications_service;

\c notifications_service;

CREATE TABLE users_tokens (
    user_id VARCHAR(50) NOT NULL,
    token VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, token)
);
