package at.fhv.ale8340.ba2.setUp;

import at.fhv.ale8340.ba2.Constants;
import at.fhv.ale8340.ba2.owndb.OwnDB;
import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.LoadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.SaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Scanner;

import static at.fhv.ale8340.ba2.Constants.CONNECTION_URL;
import static at.fhv.ale8340.ba2.Constants.GETALL_SENSORS;

public class setUpTimescale {
    private final Logger logger = (Logger) LoggerFactory.getLogger(setUpTimescale.class);

    public static void intTimescale() throws SQLException {
        System.out.println("Start init timeScaleDB");

        DriverManager.drivers().forEach(System.out::println);
        var conn = DriverManager.getConnection(CONNECTION_URL);
        System.out.println(conn.getClientInfo());

        for(String sensor: GETALL_SENSORS()){
            try (var stmt = conn.prepareStatement("INSERT INTO ba2_8 (sensorid) VALUES (?)")) {
                stmt.setString(1, sensor);
                stmt.executeUpdate();
            }catch (SQLException sqlException){
                continue;
            }
        }


        initTimescaleDB(conn);

    }


    private static void initTimescaleDB(Connection connection){
        LocalDateTime startdate = LocalDateTime.now();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("Extended_Data_With_6_Sensors_TSV.csv");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
//                System.out.println(line);

                DataPoint dataPoint = DataPointUtils.getDatapointFromInputData(line);
//                System.out.println(dataPoint.getTimestamp().toString());


                try (var stmt = connection.prepareStatement("INSERT INTO \"ba2_8_data\" (time, sensorid, value) VALUES (?, ?, ?)")) {
                    stmt.setTimestamp(1, dataPoint.getTimestamp());
                    stmt.setString(2, dataPoint.getSensorId());
                    stmt.setDouble(3, dataPoint.getValue());
                    stmt.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    continue;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        LocalDateTime enddate = LocalDateTime.now();

        System.out.println("--- END ---");
        System.out.println("StartDateTime: " + startdate);
        System.out.println("EndDateTime: " + enddate);

    }



}
