package at.fhv.ale8340.ba2.clients.influxDBClient;

import at.fhv.ale8340.ba2.clients.InfluxDBConnectionClass;
import at.fhv.ale8340.ba2.clients.IClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static at.fhv.ale8340.ba2.Constants.*;
import static at.fhv.ale8340.ba2.Constants.INFLUX_ORG;

public class InfluxDBClient implements IClient {
    private final Logger logger = (Logger) LoggerFactory.getLogger(InfluxDBClient.class);

    @Override
    public boolean loadData(LocalDateTime startDate, LocalDateTime endDate) {
        try{
            InfluxDBConnectionClass influxDBConnectionClass = new InfluxDBConnectionClass();
            com.influxdb.client.InfluxDBClient influxDBClient = influxDBConnectionClass.buildConnection(INFLUX_URL,
                    INFLUX_TOKEN, INFLUX_BUCKET, INFLUX_ORG);

            String flux = "from(bucket: \""+ INFLUX_BUCKET +"\"" + ")"
                    + buildRange(startDate, endDate)
                    + buildMeasurement;


//            System.out.println("FLux: " + flux);
            QueryApi queryApi = influxDBClient.getQueryApi();

            List<FluxTable> tables = queryApi.query(flux);
//            System.out.println(tables);
//            for(FluxTable fluxTable: tables){
//                for(FluxRecord fluxRecord: fluxTable.getRecords()){
////                    System.out.println(fluxRecord);
//                }
//            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadSensorID(LocalDateTime startDate, LocalDateTime endDate) {

        try{
            InfluxDBConnectionClass influxDBConnectionClass = new InfluxDBConnectionClass();
            com.influxdb.client.InfluxDBClient influxDBClient = influxDBConnectionClass.buildConnection(INFLUX_URL,
                    INFLUX_TOKEN, INFLUX_BUCKET, INFLUX_ORG);

            String flux = "from(bucket: \""+ INFLUX_BUCKET +"\"" + ")"
                    + buildRange(startDate, endDate)
                    + buildMeasurement
                    + " |> distinct(column: \"sensor_id\")";

            QueryApi queryApi = influxDBClient.getQueryApi();

//            System.out.println("FLux: " + flux);
            List<FluxTable> tables = queryApi.query(flux);
//            System.out.println(tables);
//            for(FluxTable fluxTable: tables){
//                for(FluxRecord fluxRecord: fluxTable.getRecords()){
////                    System.out.println(fluxRecord);
//                }
//            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean loadValues(LocalDateTime startDate, LocalDateTime endDate, List<String> sensorIDS, Double min_value, Double max_value) {
        try{
            InfluxDBConnectionClass influxDBConnectionClass = new InfluxDBConnectionClass();
            com.influxdb.client.InfluxDBClient influxDBClient = influxDBConnectionClass.buildConnection(INFLUX_URL,
                    INFLUX_TOKEN, INFLUX_BUCKET, INFLUX_ORG);

            String flux = "from(bucket: \""+ INFLUX_BUCKET +"\"" + ")"
                    + buildRange(startDate, endDate)
                    + buildMeasurement
                    + buildFilter(sensorIDS)
                    + buildFilter(min_value, max_value);

//            System.out.println("FLux: " + flux);

            QueryApi queryApi = influxDBClient.getQueryApi();

            List<FluxTable> tables = queryApi.query(flux);
//            System.out.println(tables);
//            for(FluxTable fluxTable: tables){
//                for(FluxRecord fluxRecord: fluxTable.getRecords()){
////                    System.out.println(fluxRecord);
//                }
//            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        return false;
    }

    @Override
    public String displayString() {
        return "InfluxDB";
    }


    private String buildRange(LocalDateTime startDate, LocalDateTime endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("|> range(");
        if(startDate != null){
            sb.append("start: ");
            sb.append(buildTime(startDate));
//            sb.append(Timestamp.valueOf(startDate).toInstant().toEpochMilli());
        }
        if(startDate != null && endDate != null){
            sb.append(", ");
        }
        if(endDate != null){
            sb.append("stop: ");
            sb.append(buildTime(endDate));
//            sb.append(Timestamp.valueOf(endDate).toInstant().toEpochMilli());
        }

        sb.append(")");

        return sb.toString();
    }


    private String buildFilter(List<String> sensorids) {
        if(!(sensorids.size() >=1)) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(" |> filter(fn: (r) => ");

        sb.append(getsensoridfilter(sensorids.get(0)));
        for(int i = 1; i < sensorids.size() ; i++ ){
            sb.append("OR ");
            sb.append(getsensoridfilter(sensorids.get(i)));
        }

        sb.append(")");

        return sb.toString();
    }
    private String getsensoridfilter(String sensorid){
        return "r[\"sensor_id\"] == \""+sensorid+"\" ";
    }

    private String buildFilter(Double min_value, Double max_value) {
        StringBuilder sb = new StringBuilder();
        sb.append(" |> filter(fn: (r) => ");

        if(min_value != null){
            sb.append("r._value > ");
            sb.append(min_value);
        }
        if(max_value != null && min_value != null){
            sb.append(" and ");
        }
        if(max_value != null){
            sb.append("r._value < ");
            sb.append(max_value);
        }
        sb.append(")");

        return sb.toString();
    }

    private String buildMeasurement = "|> filter(fn: (r) => r[\"_measurement\"] == \"sensor\")";

    private String buildTime(LocalDateTime localDateTime){
        StringBuilder sb = new StringBuilder();

        sb.append(localDateTime);

        if(localDateTime.getSecond() == 0){
            sb.append(":00");
        }

        sb.append("Z");

        return sb.toString();

    }
}
