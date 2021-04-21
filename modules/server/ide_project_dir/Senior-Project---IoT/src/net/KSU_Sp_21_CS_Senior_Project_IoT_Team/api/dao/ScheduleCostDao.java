package net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.dao;

import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.auth.models.Token;
import net.KSU_Sp_21_CS_Senior_Project_IoT_Team.api.models.ScheduleCost;

import java.io.IOException;
import java.util.List;

public class ScheduleCostDao implements Dao {
    private enum Query {
        ;
        public final String sql;

        Query(String sql) {
            this.sql = sql;
        }
    }

    public List<ScheduleCost> secureGetCosts(String serial, boolean onlyActive, Token token) {
        return null; // TODO: implement
    }

    public boolean secureCreateCost(ScheduleCost cost, Token token) {
        return false; // TODO: implement
    }


    @Override
    public void close() throws IOException {
        // TODO
    }
}
