package at.fhv.ale8340.ba2.owndb.sql.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SensorIdFilter {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SensorIdFilter.class);

    private List<String> sensorIds;

    public SensorIdFilter(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    @Override
    public String toString() {
        return "SensorIdFilter{" +
                "sensorIds=" + sensorIds +
                '}';
    }
}
