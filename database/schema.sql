CREATE DATABASE IF NOT EXISTS vuln_scanner;
USE vuln_scanner;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS scans (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50),
    scan_type VARCHAR(100),
    scan_result TEXT,
    scan_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
