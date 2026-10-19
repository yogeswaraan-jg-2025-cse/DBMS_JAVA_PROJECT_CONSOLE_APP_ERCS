package service;

import dao.HospitalDAO;
import model.Hospital;
import java.sql.SQLException;

public class HospitalService {
    private final HospitalDAO hospitalDAO = new HospitalDAO();

    public Hospital findBest(boolean needsTrauma, boolean needsICU) throws SQLException {
        return hospitalDAO.findNearestAvailable(needsTrauma, needsICU);
    }
}
