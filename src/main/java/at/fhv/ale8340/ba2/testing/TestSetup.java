package at.fhv.ale8340.ba2.testing;

import at.fhv.ale8340.ba2.clients.IClient;
import at.fhv.ale8340.ba2.clients.influxDBClient.InfluxDBClient;
import at.fhv.ale8340.ba2.clients.ownDBClient.OwnDBClient;
import at.fhv.ale8340.ba2.clients.timescaleDBClient.TimescaleDBclient;
import at.fhv.ale8340.ba2.monitoring.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static at.fhv.ale8340.ba2.Constants.MONITORING_PATH;
import static at.fhv.ale8340.ba2.Constants.getTestingSensorIDS;

public class TestSetup {
    private final Logger logger = (Logger) LoggerFactory.getLogger(TestSetup.class);


    public static void executeTests() {
        System.out.println("TestSetup.ExecuteTests");

        List<Test> allTests = Test.getAllTests();

        for(IClient client: getAllClients()) {
            System.out.println("StartTestsOF:" + client.displayString());
            for(int i = 0; i < 100; i++){
                if(client instanceof InfluxDBClient){
                    for (Test test : Test.getAllInfluxTests()) {

                        executeMonitorTest(test, client, (i+1));
                    }
                }else{
                    for (Test test : allTests) {

                        executeMonitorTest(test, client, (i+1));
                    }
                }

            }

        }


    }

    private static void executeMonitorTest(Test test, IClient client, int testNumber) {
        Monitor monitor = new Monitor(getMonitorID(test, client, testNumber), MONITORING_PATH(client.displayString()),1);
        monitor.startMonitor();

        executeTest(test, client);

        monitor.stopMonitor();
    }

    private static void executeTest(Test test, IClient client) {

        switch(test.getExecutionMethod()){
            case ALL_VALUES:
                client.loadData(test.getStartDate(), test.getEndDate());
                break;
            case COUNT_ENTRYS:
                //TODO
                break;
            case LIST_SENSORIDS:
                client.loadSensorID(test.getStartDate(), test.getEndDate());
                break;
            case ALL_VALUES_OF_SENSORS:
                client.loadValues(test.getStartDate(), test.getEndDate(),
                        getTestingSensorIDS(),
                        test.getMin_value(), test.getMax_value());
                break;


        }




    }

    private static String getMonitorID(Test test, IClient client, int testNumber){
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDate.now());
        sb.append("_");
        sb.append(client.displayString());
        sb.append("_");
        sb.append("NR-");
        sb.append(testNumber);
        sb.append("_");
        sb.append(test.getName());
        return sb.toString();
    }


    public static List<IClient> getAllClients(){
        List<IClient> clients = new ArrayList<>();

        OwnDBClient ownDBClient = new OwnDBClient();
        clients.add(ownDBClient);
//        TimescaleDBclient timescaleDBclient = new TimescaleDBclient();
//        clients.add(timescaleDBclient);
//        InfluxDBClient influxDBClient = new InfluxDBClient();
//        clients.add(influxDBClient);

        return clients;
    }



}
