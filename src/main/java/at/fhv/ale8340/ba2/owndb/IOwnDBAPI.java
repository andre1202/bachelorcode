package at.fhv.ale8340.ba2.owndb;

import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.exceptions.NoDataDatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.model.DataList;

import java.time.LocalDateTime;
import java.util.List;

public interface IOwnDBAPI {

    //Get Information

    /**
     * Essentiol Request with al Options of filtering the values
     * @param min_value
     * @param max_value
     * @param sensorIds
     * @param startDate
     * @param endDate
     * @return
     */
    DataList requestAllValuesBetween(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate);


    int countPoints(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) throws  NoDataDatabaseExeption;

    List<String> sensorIdsBetween(Double min_value, Double max_value, LocalDateTime startDate, LocalDateTime endDate) throws NoDataDatabaseExeption;


    //SaveData

    void saveDataPoint(DataPoint dataPoint)throws DatabaseExeption;

    void saveData(DataList dataSet) throws DatabaseExeption;
}
