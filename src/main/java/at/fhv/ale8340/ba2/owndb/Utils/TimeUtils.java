package at.fhv.ale8340.ba2.owndb.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtils {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(TimeUtils.class);



    public static LocalDateTime getLocalDateTime(Timestamp timestamp)  {
        return timestamp.toLocalDateTime();
    }

//    public static  boolean isBetween(LocalDateTime value, LocalDateTime min_value, LocalDateTime max_value){
//        if(value.isBefore(min_value))return false;
//        if(max_value.isBefore(value))return false;
//        return true;
//    }

    public static  boolean isYearBetween(int year, LocalDateTime min_value, LocalDateTime max_value){
//        logger.debug("YEAR: " + year + ", startDate: " + min_value + ", endDate: " + max_value);
        if(min_value != null){
            if(year < min_value.getYear())return false;
        }
        if(max_value != null){
            if(year > max_value.getYear())return false;
        }
        return true;
    }
    public static  boolean isMonthBetween(int month, LocalDateTime min_value, LocalDateTime max_value){
//        logger.debug("Month: " + month + ", startDate: " + min_value + ", endDate: " + max_value);
        if(min_value != null){
            if(month < min_value.getMonth().getValue())return false;
        }
        if(max_value != null) {
            if (month > max_value.getMonth().getValue()) return false;
        }
        return true;
    }
    public static  boolean isDayBetween(int day, LocalDateTime min_value, LocalDateTime max_value){
//        logger.debug("Day: " + day + ", startDate: " + min_value + ", endDate: " + max_value);
        if(min_value != null){
            if(day < min_value.getDayOfMonth())return false;
        }
        if(max_value != null){
            if(day > max_value.getDayOfMonth())return false;
        }
        return true;
    }

}
