package at.fhv.ale8340.ba2.requests;

import at.fhv.ale8340.ba2.owndb.model.DataList;

import java.time.LocalDateTime;
import java.util.List;

public interface ITesting {

    DataList requestAllValuesBetween(Double min_value, Double max_value, List<String> sensorIds,
                                            LocalDateTime startDate, LocalDateTime endDate);
}
