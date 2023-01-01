package at.fhv.ale8340.ba2.owndb.service;

import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.model.DataList;

public interface ISaveDataService {

    void saveDataPoint(DataPoint dataPoint)throws DatabaseExeption;

    void saveData(DataList dataSet) throws DatabaseExeption;


}






















