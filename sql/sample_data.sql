USE ercs;

INSERT INTO HOSPITAL (name, location, distance_km, icu_beds, trauma_available, emergency_available) VALUES
('Apollo Emergency Center', 'Downtown', 4.2, 12, TRUE, TRUE),
('City General Hospital', 'Westside', 8.5, 5, FALSE, TRUE),
('St. Mary Trauma Center', 'North Hills', 6.1, 20, TRUE, TRUE),
('Green Valley Clinic', 'Suburbs', 2.3, 0, FALSE, TRUE),
('Metro Super Specialty', 'East End', 12.0, 8, TRUE, FALSE);

INSERT INTO BLOOD_BANK (name, location, distance_km, blood_group, units_available) VALUES
('Central Blood Bank', 'Downtown', 3.2, 'O+', 15),
('Red Cross Storage', 'Westside', 7.8, 'A+', 8),
('LifeLine Blood Center', 'North Hills', 5.5, 'B-', 3),
('Community Blood Drive', 'Suburbs', 2.1, 'O-', 0),
('Metro Blood Services', 'East End', 11.0, 'AB+', 6),
('Central Blood Bank', 'Downtown', 3.2, 'A+', 10),
('Red Cross Storage', 'Westside', 7.8, 'O+', 5);

INSERT INTO AMBULANCE (ambulance_number, location, distance_km, ambulance_type, status) VALUES
('AMB-101', 'Downtown', 2.4, 'ALS', 'AVAILABLE'),
('AMB-102', 'Westside', 9.0, 'BLS', 'BUSY'),
('AMB-103', 'North Hills', 5.8, 'MICU', 'AVAILABLE'),
('AMB-104', 'Suburbs', 1.5, 'BLS', 'AVAILABLE'),
('AMB-105', 'East End', 13.2, 'ALS', 'AVAILABLE');

INSERT INTO PRIORITY_RULE (rule_type, keyword, points, active) VALUES
('BLOOD_LOSS', 'NONE', 0, TRUE), ('BLOOD_LOSS', 'MILD', 1, TRUE),
('BLOOD_LOSS', 'MODERATE', 3, TRUE), ('BLOOD_LOSS', 'SEVERE', 5, TRUE),
('CONDITION', 'unconscious', 5, TRUE), ('CONDITION', 'breathing difficulty', 5, TRUE),
('CONDITION', 'chest injury', 4, TRUE), ('CONDITION', 'head bleeding', 4, TRUE),
('CONDITION', 'head injury', 4, TRUE), ('CONDITION', 'nose bleeding', 2, TRUE),
('CONDITION', 'fracture', 3, TRUE), ('CONDITION', 'broken', 3, TRUE),
('CONDITION', 'minor injury', 1, TRUE);

INSERT INTO PRIORITY_RULE (rule_type, keyword, points, active) VALUES
('CONDITION', 'unresponsive', 5, TRUE), ('CONDITION', 'shortness of breath', 5, TRUE),
('CONDITION', 'choking', 5, TRUE), ('CONDITION', 'seizure', 5, TRUE),
('CONDITION', 'stroke', 5, TRUE), ('CONDITION', 'paralysis', 5, TRUE),
('CONDITION', 'spinal injury', 5, TRUE), ('CONDITION', 'poisoning', 5, TRUE),
('CONDITION', 'overdose', 5, TRUE), ('CONDITION', 'drowning', 5, TRUE),
('CONDITION', 'chest pain', 4, TRUE), ('CONDITION', 'severe bleeding', 4, TRUE),
('CONDITION', 'electric shock', 4, TRUE), ('CONDITION', 'major burn', 4, TRUE),
('CONDITION', 'dislocation', 3, TRUE), ('CONDITION', 'burn', 3, TRUE),
('CONDITION', 'nosebleed', 2, TRUE), ('CONDITION', 'sprain', 1, TRUE), ('CONDITION', 'cut', 1, TRUE);
