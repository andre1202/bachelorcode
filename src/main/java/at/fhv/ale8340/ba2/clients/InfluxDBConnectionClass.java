package at.fhv.ale8340.ba2.clients;

import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Instant;
import java.util.List;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxRecord;

public class InfluxDBConnectionClass {
    private final Logger logger = (Logger) LoggerFactory.getLogger(InfluxDBConnectionClass.class);



    private String token;
    private String bucket;
    private String org;

    private String url;

    public  InfluxDBClient buildConnection(String url, String token, String bucket, String org) {
        setToken(token);
        setBucket(bucket);
        setOrg(org);
        setUrl(url);
        return InfluxDBClientFactory.create(getUrl(), getToken().toCharArray(), getOrg(), getBucket());
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean singlePointWrite(InfluxDBClient influxDBClient, Point point) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

//            Point point = Point.measurement("sensor").addTag("sensor_id", "TLM0100").addField("location", "Main Lobby")
//                    .addField("model_number", "TLM89092A")
//                    .time(Instant.parse("2021-10-11T15:18:15.117484Z"), WritePrecision.MS);

            writeApi.writePoint(point);
            flag = true;
        } catch (InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
        return flag;
    }

    public boolean writePoints(InfluxDBClient influxDBClient, List<Point> points) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

//            Point point = Point.measurement("sensor").addTag("sensor_id", "TLM0100").addField("location", "Main Lobby")
//                    .addField("model_number", "TLM89092A")
//                    .time(Instant.parse("2021-10-11T15:18:15.117484Z"), WritePrecision.MS);

            writeApi.writePoints(points);
            flag = true;
        } catch (InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
        return flag;
    }


    @Deprecated
    public boolean writePointbyPOJO(InfluxDBClient influxDBClient) {
        boolean flag = false;
        try {
            WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

            Sensor sensor = new Sensor();
            sensor.sensor_id = "TLM0101";
            sensor.location = "Room 101";
            sensor.model_number = "TLM89092A";
            sensor.last_inspected = Instant.parse("2021-10-12T05:10:15.187484Z");

            writeApi.writeMeasurement(WritePrecision.MS, sensor);
            flag = true;
        } catch (

                InfluxException e) {
            System.out.println("Exception!!" + e.getMessage());
        }
        return flag;
    }

    @Measurement(name = "sensor")
    private static class Sensor {

        @Column(tag = true)
        String sensor_id;

        @Column
        String location;

        @Column
        String model_number;

        @Column(timestamp = true)
        Instant last_inspected;
    }

    public Integer queryData(InfluxDBClient influxDBClient, String queryString) {
//        String flux = "from(bucket:\"myFirstBucket\") |> range(start:0) |> filter(fn: (r) => r[\"_measurement\"] == \"sensor\") |> filter(fn: (r) => r[\"sensor_id\"] == \"TLM0100\"or r[\"sensor_id\"] == \"TLM0101\" or r[\"sensor_id\"] == \"TLM0103\" or r[\"sensor_id\"] == \"TLM0200\") |> sort() |> yield(name: \"sort\")";
        // from(bucket: "myFirstBucket")
        // |> range(start: v.timeRangeStart, stop: v.timeRangeStop)
        // |> filter(fn: (r) => r["_measurement"] == "sensor")
        // |> filter(fn: (r) => r["_field"] == "model_number")
        // |> filter(fn: (r) => r["sensor_id"] == "TLM0100" or r["sensor_id"] ==
        // "TLM0101" or r["sensor_id"] == "TLM0103" or r["sensor_id"] == "TLM0200")
        // |> sort()
        // |> yield(name: "sort")

        QueryApi queryApi = influxDBClient.getQueryApi();

        List<FluxTable> tables = queryApi.query(queryString);
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
//                System.out.println(fluxRecord.getValueByKey("sensor_id"));
            }
            return records.size();
        }
        return null;
    }



}
