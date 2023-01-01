package at.fhv.ale8340.ba2.owndb.sql.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueFilter {
    private final Logger logger = (Logger) LoggerFactory.getLogger(ValueFilter.class);


    private double min_value;
    private double max_value;

    public ValueFilter(double min_value, double max_value) {
        this.min_value = min_value;
        this.max_value = max_value;
    }

    public double getMax_value() {
        return max_value;
    }

    public double getMin_value() {
        return min_value;
    }

    @Override
    public String toString() {
        return "ValueFilter{" +
                "min_value=" + min_value +
                ", max_value=" + max_value +
                '}';
    }
}
