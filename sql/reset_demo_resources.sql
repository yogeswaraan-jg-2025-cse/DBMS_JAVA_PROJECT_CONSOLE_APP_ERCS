USE ercs;

-- One-time reset for the academic demo database.
-- It releases all ambulances and returns planned emergencies to PENDING.
UPDATE AMBULANCE SET status = 'AVAILABLE' WHERE ambulance_id > 0;
UPDATE AMBULANCE SET status = 'BUSY' WHERE ambulance_id > 0 AND ambulance_number = 'AMB-102';
UPDATE RESPONSE SET response_status = 'PARTIAL' WHERE response_id > 0 AND response_status = 'PLANNED';
UPDATE EMERGENCY SET status = 'PENDING' WHERE emergency_id > 0 AND status = 'PLANNED';
