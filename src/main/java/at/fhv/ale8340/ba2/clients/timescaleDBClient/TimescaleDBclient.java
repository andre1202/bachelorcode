package at.fhv.ale8340.ba2.clients.timescaleDBClient;

import at.fhv.ale8340.ba2.clients.IClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static at.fhv.ale8340.ba2.Constants.CONNECTION_URL;

public class TimescaleDBclient implements IClient {
    private final Logger logger = (Logger) LoggerFactory.getLogger(TimescaleDBclient.class);

    @Override
    public boolean loadData(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL);
            try (var stmt = conn.prepareStatement("SELECT * FROM ba2_8_data" +
                    " WHERE time > ? AND time < ?")) {
                stmt.setTimestamp(1, Timestamp.valueOf(startDate));
                stmt.setTimestamp(2, Timestamp.valueOf(endDate));
                ResultSet resultSet = stmt.executeQuery();

                return true;
            }catch (SQLException sqlException){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadSensorID(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL);
            try (var stmt = conn.prepareStatement("SELECT sensorid FROM ba2_8_data" +
                    " WHERE time > ? AND time < ? GROUP BY sensorid")) {
                stmt.setTimestamp(1, Timestamp.valueOf(startDate));
                stmt.setTimestamp(2, Timestamp.valueOf(endDate));
                ResultSet resultSet = stmt.executeQuery();

                return true;
            }catch (SQLException sqlException){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadValues(LocalDateTime startDate, LocalDateTime endDate, List<String> sensorIDS, Double min_value, Double max_value) {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL);
            try (var stmt = conn.prepareStatement("SELECT * FROM ba2_8_data" +
                    " WHERE time > ? AND time < ? AND sensorid IN ? AND value > ? AND value < ?" )) {
                stmt.setTimestamp(1, Timestamp.valueOf(startDate));
                stmt.setTimestamp(2, Timestamp.valueOf(endDate));
                stmt.setString(3, getSensoridsInListForm(sensorIDS));
                stmt.setDouble(4, min_value);
                stmt.setDouble(5, max_value);
                ResultSet resultSet = stmt.executeQuery();

                return true;
            }catch (SQLException sqlException){

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String displayString() {
        return "timeScaleDB";
    }

    private String getSensoridsInListForm(List<String> sensorids){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for(int i = 0; i < sensorids.size(); i++){
            sb.append(sensorids.get(i));
            if(!(i == (sensorids.size()-1))){
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
