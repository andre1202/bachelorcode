package at.fhv.ale8340.ba2.clients;

import java.time.LocalDateTime;
import java.util.List;

public interface IClient {


    boolean loadData(LocalDateTime startDate, LocalDateTime endDate);

    boolean loadSensorID(LocalDateTime startDate, LocalDateTime endDate);
    boolean loadValues(LocalDateTime startDate, LocalDateTime endDate , List<String> sensorIDS, Double min_value, Double max_value);


    String displayString();

}
