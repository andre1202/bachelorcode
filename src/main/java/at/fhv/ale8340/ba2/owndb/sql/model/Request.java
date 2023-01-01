package at.fhv.ale8340.ba2.owndb.sql.model;

import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;

public class Request {
    private final Logger logger = (Logger) LoggerFactory.getLogger(Request.class);

	private StatementType statementType;
    private TimeFilter timeFilter;
    private SensorIdFilter sensorIdFilter;
    private ValueFilter valueFilter;

    private DataPoint dataPoint;

    private SelectFilter selectFilter;

    public Request(StatementType statementType, SelectFilter selectFilter, TimeFilter timeFilter, SensorIdFilter sensorIdFilter, ValueFilter valueFilter) {
        this.statementType = statementType;
        this.selectFilter = selectFilter;
        this.timeFilter = timeFilter;
        this.sensorIdFilter = sensorIdFilter;
        this.valueFilter = valueFilter;
    }

    public Request(DataPoint dataPoint){
        statementType = StatementType.INSERT;
        timeFilter = null;
        sensorIdFilter = null;
        selectFilter = null;
        valueFilter = null;

        this.dataPoint = dataPoint;

    }

    public DataPoint getDataPoint() {
        return dataPoint;
    }

    public SelectFilter getSelectFilter() {
        return selectFilter;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public TimeFilter getTimeFilter() {
        return timeFilter;
    }

    public SensorIdFilter getSensorIdFilter() {
        return sensorIdFilter;
    }

    public ValueFilter getValueFilter() {
        return valueFilter;
    }

    @Override
    public String toString() {
        return "Request{" +
                "statementType=" + statementType +
                ", timeFilter=" + timeFilter +
                ", sensorIdFilter=" + sensorIdFilter +
                ", valueFilter=" + valueFilter +
                ", dataPoint=" + dataPoint +
                ", selectFilter=" + selectFilter +
                '}';
    }
}
