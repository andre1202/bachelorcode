package at.fhv.ale8340.ba2.owndb.sql.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectFilter {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SelectFilter.class);

    private boolean all;
    private boolean timestamp;
    private boolean sensorid;
    private boolean value;

    public SelectFilter(boolean all, boolean timestamp, boolean sensorid, boolean value) {
        this.all = all;
        this.timestamp = timestamp;
        this.sensorid = sensorid;
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectFilter{" +
                "all=" + all +
                ", timestamp=" + timestamp +
                ", sensorid=" + sensorid +
                ", value=" + value +
                '}';
    }

    public boolean isAll() {
        return all;
    }

    public boolean isTimestamp() {
        return timestamp;
    }

    public boolean isSensorid() {
        return sensorid;
    }

    public boolean isValue() {
        return value;
    }
}
