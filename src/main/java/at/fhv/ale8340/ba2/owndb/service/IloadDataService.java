package at.fhv.ale8340.ba2.owndb.service;

import at.fhv.ale8340.ba2.owndb.exceptions.NoDataDatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataList;

import java.time.LocalDateTime;
import java.util.List;

public interface IloadDataService {


    DataList requestAllValuesBetween(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate);

    int countPoints(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate);

    List<String> sensorIdsBetween(Double min_value, Double max_value, LocalDateTime startDate, LocalDateTime endDate);
}







