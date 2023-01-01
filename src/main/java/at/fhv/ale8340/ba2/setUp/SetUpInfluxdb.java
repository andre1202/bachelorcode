package at.fhv.ale8340.ba2.setUp;

import at.fhv.ale8340.ba2.clients.InfluxDBConnectionClass;
import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static at.fhv.ale8340.ba2.Constants.*;

public class SetUpInfluxdb {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SetUpInfluxdb.class);


    public static void setUpInflux() throws SQLException {
        System.out.println("Start init InfluxDB");

        InfluxDBConnectionClass influxDBConnectionClass = new InfluxDBConnectionClass();
        InfluxDBClient influxDBClient = influxDBConnectionClass.buildConnection(INFLUX_URL,
                INFLUX_TOKEN, INFLUX_BUCKET, INFLUX_ORG);

        initInfluxDB(influxDBClient, influxDBConnectionClass);

    }


    private static void initInfluxDB(InfluxDBClient influxDBClient, InfluxDBConnectionClass influxDBConnectionClass){
        LocalDateTime startdate = LocalDateTime.now();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("Extended_Data_With_6_Sensors_TSV.csv");
            sc = new Scanner(inputStream, "UTF-8");
            int count = 0;
            List<Point> points = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
//                System.out.println(line);

                DataPoint dataPoint = DataPointUtils.getDatapointFromInputData(line);
//                System.out.println(dataPoint.getTimestamp().toString());

                Point point = Point.measurement("sensor").
                        addTag("sensor_id", dataPoint.getSensorId()).

                        addField("value", dataPoint.getValue()).
                        time(dataPoint.getTimestamp().toInstant(), WritePrecision.MS);
//                influxDBConnectionClass.singlePointWrite(influxDBClient, point);
                points.add(point);


                count++;
                if((count % 100000) == 0){
                    System.out.println("AktuellerStand: " + count + " Punkte wurden verarbeitet");
                    System.out.println("Current Point: " + dataPoint.getSaveLine());
                    influxDBConnectionClass.writePoints(influxDBClient, points);
                    points = new ArrayList<>();
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
