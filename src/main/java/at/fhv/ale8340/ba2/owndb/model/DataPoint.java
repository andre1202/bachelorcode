package at.fhv.ale8340.ba2.owndb.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DataPoint implements Comparable<DataPoint>{
    private final Logger logger = (Logger) LoggerFactory.getLogger(DataPoint.class);

    private Timestamp timestamp;
    private String sensorId;
    private float value;

    public String getSaveLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(timestamp);
        sb.append("\t");
        sb.append(sensorId);
        sb.append("\t");
        sb.append(value);
        return sb.toString();
    }

    public DataPoint(Timestamp timestamp, String sensorId, float value) {
        this.timestamp = timestamp;
        this.sensorId = sensorId;
        this.value = value;
    }

    @Override
    public int compareTo(DataPoint o) {
        if (timestamp.after(o.getTimestamp())) {
            return -1;
        } else {
            return 1;
        }
    }

    public float getValue() {
        return value;
    }

    public String getSensorId() {
        return sensorId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
