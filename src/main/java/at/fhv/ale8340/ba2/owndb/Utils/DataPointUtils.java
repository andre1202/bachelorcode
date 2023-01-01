package at.fhv.ale8340.ba2.owndb.Utils;

import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DataPointUtils {
    private final Logger logger = (Logger) LoggerFactory.getLogger(DataPointUtils.class);


    // just for data from the OWNDB
    public static DataPoint map(String pointString){
        String[] all = pointString.split("\t");
        Timestamp timestamp =  Timestamp.valueOf(all[0]);
        String sensorId = all[1];
        float value = Float.valueOf(all[2]);
        return new DataPoint(timestamp, sensorId, value);
    }

    //just for insertig data whithc created from python
    public static DataPoint getDatapointFromInputData(String line){
        String[] array  = line.split("\t");
        Timestamp ts = new Timestamp(Long.valueOf(array[0]));
        String sensorId = array[1];
        float value = Float.valueOf(array[2]);
        return new DataPoint(ts, sensorId, value);
    }



    public static String map(DataPoint dataPoint){
        return dataPoint.getSaveLine();
    }


    public static String getFilePartYearMonthDayWithoutFileEndingAndMainPath(DataPoint dataPoint){
        LocalDateTime localDateTime = dataPoint.getTimestamp().toLocalDateTime();
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        int day = localDateTime.getDayOfMonth();
        return StringUtils.pathbuilder(String.valueOf(year),
                String.valueOf(month), String.valueOf(day));
    }
}
