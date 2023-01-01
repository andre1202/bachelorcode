package at.fhv.ale8340.ba2.testing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static at.fhv.ale8340.ba2.Constants.getTestingSensorIDS;

public enum Test {

    DATA_VALUE_1_DAY("DATA_VALUE_1_DAY", ExecutionMethod.ALL_VALUES,
            LocalDateTime.of(2021, 02, 12, 0,0, 0),
            LocalDateTime.of(2021, 02, 13, 0,0, 0),
            null, null, null),
    DATA_VALUE_1_WEEK("DATA_VALUE_1_WEEK", ExecutionMethod.ALL_VALUES,
            LocalDateTime.of(2021, 02, 1, 0,0, 0),
            LocalDateTime.of(2021, 02, 8, 0,0, 0),
            null, null, null),
    DATA_VALUE_1_MONTH("DATA_VALUE_1_MONTH", ExecutionMethod.ALL_VALUES,
            LocalDateTime.of(2021, 02, 12, 0,0, 0),
            LocalDateTime.of(2021, 03, 13, 0,0, 0),
            null, null, null),
    DATA_VALUE_1_YEAR("DATA_VALUE_1_YEAR", ExecutionMethod.ALL_VALUES,
            LocalDateTime.of(2021, 02, 12, 0,0, 0),
            LocalDateTime.of(2022, 02, 13, 0,0, 0),
            null, null, null),

    SENSORIDS_1_DAY("SENSORIDS_1_DAY", ExecutionMethod.LIST_SENSORIDS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2021, 02, 13, 0,0),
            getTestingSensorIDS(), null, null),
    SENSORIDS_1_WEEK("SENSORIDS_1_WEEK", ExecutionMethod.LIST_SENSORIDS,
            LocalDateTime.of(2021, 02, 1, 0,0),
            LocalDateTime.of(2021, 02, 7, 0,0),
            getTestingSensorIDS(), null, null),
    SENSORIDS_1_MONTH("SENSORIDS_1_MONTH", ExecutionMethod.LIST_SENSORIDS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2021, 03, 13, 0,0),
            getTestingSensorIDS(), null, null),
    SENSORIDS_1_YEAR("SENSORIDS_1_YEAR", ExecutionMethod.LIST_SENSORIDS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2022, 02, 13, 0,0),
            getTestingSensorIDS(), null, null),


    VALUES_OF_SENSOR_1_DAY("VALUES_OF_SENSOR_1_DAY", ExecutionMethod.ALL_VALUES_OF_SENSORS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2021, 02, 13, 0,0),
            getTestingSensorIDS(), 15.0, 25.0),
    VALUES_OF_SENSOR_1_WEEK("VALUES_OF_SENSOR_1_WEEK", ExecutionMethod.ALL_VALUES_OF_SENSORS,
            LocalDateTime.of(2021, 02, 1, 0,0),
            LocalDateTime.of(2021, 02, 7, 0,0),
            getTestingSensorIDS(), 15.0, 25.0),
    VALUES_OF_SENSOR_1_MONTH("VALUES_OF_SENSOR_1_MONTH", ExecutionMethod.ALL_VALUES_OF_SENSORS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2021, 03, 13, 0,0),
            getTestingSensorIDS(), 15.0, 25.0),
    VALUES_OF_SENSOR_1_YEAR("VALUES_OF_SENSOR_1_YEAR", ExecutionMethod.ALL_VALUES_OF_SENSORS,
            LocalDateTime.of(2021, 02, 12, 0,0),
            LocalDateTime.of(2022, 02, 13, 0,0),
            getTestingSensorIDS(), 15.0, 25.0);

    private final String name;
    private final ExecutionMethod executionMethod;

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final List<String> sensorIds;
    private final Double min_value;
    private final Double max_value;

    Test(String name, ExecutionMethod executionMethod, LocalDateTime startDate, LocalDateTime endDate, List<String> sensorIds, Double min_value, Double max_value) {
        this.name = name;
        this.executionMethod = executionMethod;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sensorIds = sensorIds;
        this.min_value = min_value;
        this.max_value = max_value;
    }

    public String getName() {
        return name;
    }

    public ExecutionMethod getExecutionMethod() {
        return executionMethod;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public Double getMax_value() {
        return max_value;
    }

    public Double getMin_value() {
        return min_value;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public static List<Test> getAllTests(){
        return  new ArrayList<Test>(EnumSet.allOf(Test.class));
    }
    public static List<Test> getAllInfluxTests(){
        ArrayList<Test> tests =  new ArrayList<Test>(EnumSet.allOf(Test.class));

        if(tests.contains(Test.DATA_VALUE_1_YEAR)) tests.remove(Test.DATA_VALUE_1_YEAR);
        if(tests.contains(Test.SENSORIDS_1_YEAR)) tests.remove(Test.SENSORIDS_1_YEAR);
        if(tests.contains(Test.VALUES_OF_SENSOR_1_YEAR)) tests.remove(Test.VALUES_OF_SENSOR_1_YEAR);

        return tests;
    }

}
