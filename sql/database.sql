CREATE DATABASE IF NOT EXISTS ercs;
USE ercs;

CREATE TABLE PATIENT (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    blood_loss VARCHAR(20) NOT NULL, -- 'NONE', 'MILD', 'MODERATE', 'SEVERE'
    condition_description TEXT,
    CONSTRAINT chk_patient_age CHECK (age BETWEEN 0 AND 120)
);

CREATE TABLE EMERGENCY (
    emergency_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    emergency_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    location VARCHAR(150) NOT NULL,
    priority_score INT NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES PATIENT(patient_id),
    INDEX idx_emergency_status_priority (status, priority_score)
);

CREATE TABLE HOSPITAL (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    distance_km DECIMAL(5,2) NOT NULL,
    icu_beds INT NOT NULL,
    trauma_available BOOLEAN NOT NULL,
    emergency_available BOOLEAN NOT NULL,
    CONSTRAINT chk_hospital_distance CHECK (distance_km >= 0),
    CONSTRAINT chk_hospital_icu CHECK (icu_beds >= 0)
);

CREATE TABLE BLOOD_BANK (
    bank_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(150) NOT NULL,
    distance_km DECIMAL(5,2) NOT NULL,
    blood_group VARCHAR(5) NOT NULL,
    units_available INT NOT NULL,
    CONSTRAINT chk_blood_distance CHECK (distance_km >= 0),
    CONSTRAINT chk_blood_units CHECK (units_available >= 0),
    INDEX idx_blood_group_stock (blood_group, units_available)
);

CREATE TABLE AMBULANCE (
    ambulance_id INT AUTO_INCREMENT PRIMARY KEY,
    ambulance_number VARCHAR(20) NOT NULL,
    location VARCHAR(150) NOT NULL,
    distance_km DECIMAL(5,2) NOT NULL,
    ambulance_type VARCHAR(50) NOT NULL, -- 'BLS', 'ALS', 'MICU'
    status VARCHAR(20) DEFAULT 'AVAILABLE', -- 'AVAILABLE', 'BUSY'
    CONSTRAINT chk_ambulance_distance CHECK (distance_km >= 0),
    INDEX idx_ambulance_status_distance (status, distance_km)
);

CREATE TABLE RESPONSE (
    response_id INT AUTO_INCREMENT PRIMARY KEY,
    emergency_id INT NOT NULL,
    ambulance_id INT,
    hospital_id INT,
    blood_bank_id INT,
    response_status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (emergency_id) REFERENCES EMERGENCY(emergency_id),
    FOREIGN KEY (ambulance_id) REFERENCES AMBULANCE(ambulance_id),
    FOREIGN KEY (hospital_id) REFERENCES HOSPITAL(hospital_id),
    FOREIGN KEY (blood_bank_id) REFERENCES BLOOD_BANK(bank_id),
    INDEX idx_response_emergency (emergency_id)
);

CREATE TABLE PRIORITY_RULE (
    rule_id INT AUTO_INCREMENT PRIMARY KEY,
    rule_type VARCHAR(20) NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    points INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uq_priority_rule (rule_type, keyword)
);
