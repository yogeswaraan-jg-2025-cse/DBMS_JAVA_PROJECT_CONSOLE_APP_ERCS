USE ercs;

CREATE TABLE IF NOT EXISTS PRIORITY_RULE (
    rule_id INT AUTO_INCREMENT PRIMARY KEY,
    rule_type VARCHAR(20) NOT NULL,
    keyword VARCHAR(100) NOT NULL,
    points INT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uq_priority_rule (rule_type, keyword)
);

-- Academic simulation rules only. Edit this catalog in MySQL Workbench to add demo keywords.
INSERT INTO PRIORITY_RULE (rule_type, keyword, points, active) VALUES
('BLOOD_LOSS', 'NONE', 0, TRUE),
('BLOOD_LOSS', 'MILD', 1, TRUE),
('BLOOD_LOSS', 'MODERATE', 3, TRUE),
('BLOOD_LOSS', 'SEVERE', 5, TRUE),
('CONDITION', 'unconscious', 5, TRUE),
('CONDITION', 'unresponsive', 5, TRUE),
('CONDITION', 'breathing difficulty', 5, TRUE),
('CONDITION', 'shortness of breath', 5, TRUE),
('CONDITION', 'choking', 5, TRUE),
('CONDITION', 'seizure', 5, TRUE),
('CONDITION', 'stroke', 5, TRUE),
('CONDITION', 'paralysis', 5, TRUE),
('CONDITION', 'spinal injury', 5, TRUE),
('CONDITION', 'poisoning', 5, TRUE),
('CONDITION', 'overdose', 5, TRUE),
('CONDITION', 'drowning', 5, TRUE),
('CONDITION', 'chest injury', 4, TRUE),
('CONDITION', 'chest pain', 4, TRUE),
('CONDITION', 'head bleeding', 4, TRUE),
('CONDITION', 'head injury', 4, TRUE),
('CONDITION', 'severe bleeding', 4, TRUE),
('CONDITION', 'electric shock', 4, TRUE),
('CONDITION', 'major burn', 4, TRUE),
('CONDITION', 'fracture', 3, TRUE),
('CONDITION', 'broken', 3, TRUE),
('CONDITION', 'dislocation', 3, TRUE),
('CONDITION', 'burn', 3, TRUE),
('CONDITION', 'nose bleeding', 2, TRUE),
('CONDITION', 'nosebleed', 2, TRUE),
('CONDITION', 'minor injury', 1, TRUE),
('CONDITION', 'sprain', 1, TRUE),
('CONDITION', 'cut', 1, TRUE)
ON DUPLICATE KEY UPDATE points = VALUES(points), active = VALUES(active);
