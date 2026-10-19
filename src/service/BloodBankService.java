package service;

import dao.BloodBankDAO;
import model.BloodBank;
import java.sql.SQLException;

public class BloodBankService {
    private final BloodBankDAO bloodBankDAO = new BloodBankDAO();

    public BloodBank findBest(String bloodGroup) throws SQLException {
        return bloodBankDAO.findNearestWithStock(bloodGroup, 1);
    }
}
