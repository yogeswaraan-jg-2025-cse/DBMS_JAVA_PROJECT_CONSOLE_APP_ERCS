package service;

import dao.AmbulanceDAO;
import model.Ambulance;
import java.sql.SQLException;

public class AmbulanceService {
    private final AmbulanceDAO ambulanceDAO = new AmbulanceDAO();

    public Ambulance findBest(boolean critical) throws SQLException {
        return ambulanceDAO.findNearestAvailable(critical ? "MICU" : "ALS");
    }
}
