package at.fhv.ale8340.ba2.owndb.service.impl;

import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.Utils.StringUtils;
import at.fhv.ale8340.ba2.owndb.Utils.TimeUtils;
import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataList;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class LoadDataService implements IloadDataService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(LoadDataService.class);

    private String mainPath;

    public LoadDataService(String mainPath){
        this.mainPath = mainPath;
    }

    private final int MODE_STARTFILE = 0;
    private final int MODE_MIDDLEFILE = 1;
    private final int MODE_ENDFILE = 2;

    @Override
    public DataList requestAllValuesBetween(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) {
        DataList outputDataList = new DataList();
        List<String> filePaths = getAllFilePaths(mainPath, startDate, endDate);

//        logger.debug("ALL FILES: {}", filePaths);

        for(int i = 0; i < filePaths.size(); i++){
            if(i == 0 && startDate != null){
                DataList dataSet = loadDataFromFile(MODE_STARTFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                outputDataList.addSet(dataSet);

            }else if(i == filePaths.size()-1 && startDate != null){
                DataList dataSet = loadDataFromFile(MODE_ENDFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                outputDataList.addSet(dataSet);
            }else{
                DataList dataSet = loadDataFromFile(MODE_MIDDLEFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                outputDataList.addSet(dataSet);
            }
        }
        return outputDataList;
    }

    @Override
    public int countPoints(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) {
        int datapoint = 0;
        List<String> filePaths = getAllFilePaths(mainPath, startDate, endDate);

        for(int i = 0; i < filePaths.size(); i++){
            if(i == 0 && startDate != null){
                DataList dataSet = loadDataFromFile(MODE_STARTFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                datapoint = datapoint + dataSet.getDataPoints().size();

            }else if(i == filePaths.size()-1 && startDate != null){
                DataList dataSet = loadDataFromFile(MODE_ENDFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                datapoint = datapoint + dataSet.getDataPoints().size();

            }else{
                DataList dataSet = loadDataFromFile(MODE_MIDDLEFILE, filePaths.get(i), min_value, max_value, sensorIds, startDate, endDate);
                datapoint = datapoint + dataSet.getDataPoints().size();

            }
        }
        return  datapoint;
    }

    @Override
    public List<String> sensorIdsBetween(Double min_value, Double max_value, LocalDateTime startDate, LocalDateTime endDate) {
        Set<String> sensorIdsList = new HashSet<>();
        List<String> filePaths = getAllFilePaths(mainPath, startDate, endDate);

        for(int i = 0; i < filePaths.size(); i++){
            if(i == 0 && startDate != null){
                Set<String> sensoridsFromFile = loadSensorIdsFromFile(MODE_STARTFILE, filePaths.get(i), min_value, max_value, startDate, endDate);
                for(String all: sensoridsFromFile){
                    sensorIdsList.add(all);
                }
            }else if(i == filePaths.size()-1 && startDate != null){
                Set<String> sensoridsFromFile = loadSensorIdsFromFile(MODE_ENDFILE, filePaths.get(i), min_value, max_value, startDate, endDate);
                for(String all: sensoridsFromFile){
                    sensorIdsList.add(all);
                }
            }else{
                Set<String> sensoridsFromFile = loadSensorIdsFromFile(MODE_MIDDLEFILE, filePaths.get(i), min_value, max_value, startDate, endDate);
                for(String all: sensoridsFromFile){
                    sensorIdsList.add(all);
                }
            }
        }
        return new ArrayList<>(sensorIdsList);
    }

    private Set<String> loadSensorIdsFromFile(int mode, String filePath, Double min_value, Double max_value, LocalDateTime startDate, LocalDateTime endDate) {
        Set<String> output = new HashSet<>();

        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
//                logger.debug(data);
                DataPoint dataPoint = DataPointUtils.map(data);

                switch(mode){
                    case MODE_STARTFILE:
                        if(dataPoint.getTimestamp().after(Timestamp.valueOf(startDate)) ||
                                dataPoint.getTimestamp().equals(Timestamp.valueOf(startDate))){
                            if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, null)){
                                output.add(dataPoint.getSensorId());
                                continue;
                            }
                        }
                        break;
                    case MODE_MIDDLEFILE:
                        if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, null)){
                            output.add(dataPoint.getSensorId());
                            continue;
                        }
                        break;
                    case MODE_ENDFILE:
                        if(dataPoint.getTimestamp().before(Timestamp.valueOf(startDate)) ||
                                dataPoint.getTimestamp().equals(Timestamp.valueOf(startDate))){
                            if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, null)){
                                output.add(dataPoint.getSensorId());
                                continue;
                            }
                        }
                        break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }


        return output;
    }

    private DataList loadDataFromFile(int mode, String filePath, Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) {
        DataList dataList = new DataList();
//        logger.debug("LoadFromFile: {}", filePath);
        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
//                logger.debug(data);
                DataPoint dataPoint = DataPointUtils.map(data);

                switch(mode){
                    case MODE_STARTFILE:
                        if(dataPoint.getTimestamp().after(Timestamp.valueOf(startDate)) ||
                                dataPoint.getTimestamp().equals(Timestamp.valueOf(startDate))){
                            if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, sensorIds)){
                                dataList.addDataPoint(dataPoint);
                                continue;
                            }
                        }
                        break;
                    case MODE_MIDDLEFILE:
                        if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, sensorIds)){
                            dataList.addDataPoint(dataPoint);
                            continue;
                        }
                        break;
                    case MODE_ENDFILE:
                        if(dataPoint.getTimestamp().before(Timestamp.valueOf(startDate)) ||
                                dataPoint.getTimestamp().equals(Timestamp.valueOf(startDate))){
                            if(checkDatapointSensorIdsAndValue(dataPoint,min_value, max_value, sensorIds)){
                                dataList.addDataPoint(dataPoint);
                                continue;
                            }
                        }
                        break;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        dataList.sortDataList();

        return dataList;

    }

    private boolean checkDatapointSensorIdsAndValue(DataPoint dataPoint, Double min_value, Double max_value, List<String> sensorIds) {
        if(!checkSensorid(dataPoint, sensorIds))return false;
        if(!checkValue(dataPoint, max_value, min_value))return false;
        return true;
    }

    private boolean checkValue(DataPoint dataPoint, Double max_value, Double min_value) {
        if(min_value != null){
            if(dataPoint.getValue() < min_value){
                return false;
            }
        }
        if(max_value != null){
            if(dataPoint.getValue() > max_value){
                return false;
            }
        }
        return true;
    }

    private boolean checkSensorid(DataPoint dataPoint, List<String> sensorIds){
        if(sensorIds != null){
            if(!sensorIds.contains(dataPoint.getSensorId())){
                return false;
            }
        }
        return true;
    }

    private List<String> getAllFilePaths(String mainPath, LocalDateTime startDate, LocalDateTime endDate) {
        List<String> allPaths = new ArrayList<>();
        File directoryWithYears = new File(mainPath);
        List<Integer> allYears = getAllYears(directoryWithYears.list());

        for(Integer year: allYears) {
            File directoryWithMonts = new File(StringUtils.pathbuilder(mainPath, String.valueOf(year)));
            List<Integer> allMonthsInYear = getAllMonths(directoryWithMonts.list());
            for (Integer month : allMonthsInYear) {
                File directoryWithDays = new File(StringUtils.pathbuilder(mainPath, String.valueOf(year), String.valueOf(month)));
                List<Integer> days = getAllDay(directoryWithDays.list());
                for (Integer day : days) {
                    String path = StringUtils.pathbuilderPlusFileEnding(mainPath, String.valueOf(year), String.valueOf(month), String.valueOf(day));
//                                System.out.println("Path: " +path);
                    LocalDate date = LocalDate.of(year, month, day);
                    if(dateIsInRange(date, startDate, endDate)){
                        allPaths.add(path);

                    }
                }

            }

        }
        return allPaths;
    }

    private boolean dateIsInRange(LocalDate date, LocalDateTime startDate, LocalDateTime endDate) {
        if(startDate != null){
            if(startDate.toLocalDate().isAfter(date))return false;
        }
        if(endDate != null){
            if(endDate.toLocalDate().isBefore(date))return false;
        }
        return true;
    }

    private List<Integer> getAllYears(String[] list) {
        List<Integer> output = new ArrayList<>();
        for(String all: list){
            Integer allInt = Integer.parseInt(all);
            output.add(allInt);
        }
        Collections.sort(output);
        return output;
    }
    private List<Integer> getAllMonths(String[] list) {
        List<Integer> output = new ArrayList<>();
        for(String all: list){
            Integer allInt = Integer.parseInt(all);
            output.add(allInt);
        }
        Collections.sort(output);
        return output;
    }
    private List<Integer> getAllDay(String[] list) {
        List<Integer> output = new ArrayList<>();
        for(String all: list){
            Integer allInt = Integer.parseInt(all.split(".csv")[0]);
            output.add(allInt);
        }
        Collections.sort(output);
        return output;
    }
}
