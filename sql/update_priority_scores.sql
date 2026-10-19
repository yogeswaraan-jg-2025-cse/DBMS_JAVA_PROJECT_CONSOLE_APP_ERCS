USE ercs;

-- Run install_priority_rules.sql first. This recalculates existing demo cases from that catalog.
UPDATE EMERGENCY e
JOIN PATIENT p ON p.patient_id = e.patient_id
SET e.priority_score = (
    SELECT COALESCE(SUM(r.points), 0)
    FROM PRIORITY_RULE r
    WHERE r.active = TRUE
      AND (
          (r.rule_type = 'BLOOD_LOSS' AND r.keyword = p.blood_loss)
          OR
          (r.rule_type = 'CONDITION'
              AND LOWER(COALESCE(p.condition_description, '')) LIKE CONCAT('%', LOWER(r.keyword), '%'))
      )
)
WHERE e.emergency_id > 0;

UPDATE EMERGENCY
SET severity = CASE
    WHEN priority_score >= 10 THEN 'CRITICAL'
    WHEN priority_score >= 6 THEN 'HIGH'
    WHEN priority_score >= 3 THEN 'MEDIUM'
    ELSE 'LOW'
END
WHERE emergency_id > 0 AND status = 'PENDING';
