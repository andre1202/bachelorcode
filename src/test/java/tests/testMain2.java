package tests;

import at.fhv.ale8340.ba2.Constants;
import at.fhv.ale8340.ba2.monitoring.Monitor;
import at.fhv.ale8340.ba2.owndb.OwnDB;
import at.fhv.ale8340.ba2.owndb.model.DataList;
import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.LoadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.SaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.List;

import com.sun.management.OperatingSystemMXBean;

public class testMain2 {
    private final Logger logger = (Logger) LoggerFactory.getLogger(testMain2.class);

    public static void ta(String[] args) {
//        testCPUandMemoryLoad();

//        testLoadData();

//        testCountSensorIDS();

//        testCountEntrys();
//        testMonitor();

        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(localDateTime);

    }

    private static void testCountEntrys() {
        IloadDataService loadDataService = new LoadDataService(Constants.MAIN_PATH);
        ISaveDataService saveDataService = new SaveDataService(Constants.MAIN_PATH);
        OwnDB ownDB = new OwnDB(loadDataService, saveDataService);
        Monitor monitor = new Monitor("AX35", "./testmonitoring/",1);
        monitor.startMonitor();
        try {
            int count = ownDB.countPoints(null, null, null,
                    LocalDateTime.of(2021,5,1,0,0),
                    LocalDateTime.of(2021,5,8,0,0)
            );

            System.out.println("==START==");
            System.out.println(count);
            System.out.println("==END==");
        } catch (Exception e) {
            e.printStackTrace();
        }
        monitor.stopMonitor();
    }

    private static void testMonitor() {

        Monitor monitor = new Monitor("AX47", "./testmonitoring/",1);
        monitor.startMonitor();
        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(10);


        try {
            Thread.sleep(10*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        monitor.stopMonitor();


    }

    private static void testCountSensorIDS(){
        IloadDataService loadDataService = new LoadDataService(Constants.MAIN_PATH);
        ISaveDataService saveDataService = new SaveDataService(Constants.MAIN_PATH);
        OwnDB ownDB = new OwnDB(loadDataService, saveDataService);
        Monitor monitor = new Monitor("AX35", "./testmonitoring/",1);
        monitor.startMonitor();
        try {
            List<String> sensors = ownDB.sensorIdsBetween(null, null,
                    LocalDateTime.of(2021,5,1,0,0),
                    LocalDateTime.of(2021,5,8,0,0)
                    );

            System.out.println("==START==");
            System.out.println(sensors);
            System.out.println("==END==");
        } catch (Exception e) {
            e.printStackTrace();
        }
        monitor.stopMonitor();


    }

    private static void testLoadData() {
        IloadDataService loadDataService = new LoadDataService(Constants.MAIN_PATH);
        ISaveDataService saveDataService = new SaveDataService(Constants.MAIN_PATH);
        OwnDB ownDB = new OwnDB(loadDataService, saveDataService);
        Monitor monitor = new Monitor("AX35", "./testmonitoring/",1);
        monitor.startMonitor();
        try {
            DataList dataList = ownDB.requestAllValuesBetween(
                    null, null, null,
                    LocalDateTime.of(2021,5,1,0,0),
                    LocalDateTime.of(2021,5,8,0,0)
            );
            System.out.println("==START==");
            System.out.println(dataList.getDataPoints().size());
            System.out.println("==END==");
        } catch (Exception e) {
            e.printStackTrace();
        }
        monitor.stopMonitor();

    }

    public static void testCPUandMemoryLoad(){
        System.out.println("START");

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        // What % CPU load this current JVM is taking, from 0.0-1.0
        System.out.println(osBean.getProcessCpuLoad());

        // What % load the overall system is at, from 0.0-1.0
        System.out.println(osBean.getSystemCpuLoad());

        System.out.println(osBean.getFreePhysicalMemorySize());

        osBean.getTotalPhysicalMemorySize();
        
    }
    
    


    
    
    
}
