package at.fhv.ale8340.ba2.owndb.service.impl;

import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.Utils.FileUtils;
import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.model.DataList;
import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class SaveDataService implements ISaveDataService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SaveDataService.class);

    private String mainPath;

    public SaveDataService(String mainPath){
        this.mainPath = mainPath;
    }

    @Override
    public void saveDataPoint(DataPoint dataPoint) throws DatabaseExeption {
//        System.out.println("Save data Service Point");
        try {
            File filetoSaveDate = FileUtils.getFileByTimeStamp(mainPath, dataPoint.getTimestamp());
            Writer writer = new BufferedWriter(new FileWriter(filetoSaveDate, true));
            writer.write(dataPoint.getSaveLine() + "\n");
            writer.close();
        } catch (IOException e) {
            throw new DatabaseExeption();
        }
    }

    @Override
    public void saveData(DataList dataSet) throws DatabaseExeption {
        for(DataPoint dataPoint: dataSet.getDataPoints()){
            saveDataPoint(dataPoint);
        }
    }
}
