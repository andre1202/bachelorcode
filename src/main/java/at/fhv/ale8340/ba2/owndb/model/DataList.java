package at.fhv.ale8340.ba2.owndb.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.fhv.ale8340.ba2.Constants.OUTSORUCING_PATH;

@ToString
public class DataList {
    private final Logger logger = (Logger) LoggerFactory.getLogger(DataList.class);

    private int filecount = 0;
    private int random = 0;
    private List<DataPoint> dataPoints;
    private List<DataPoint> dataPointsToSave;


    public DataList() {
        dataPoints = new ArrayList<>();
        Random rand = new Random();
        random = rand.nextInt(200000) + rand.nextInt(200000) + rand.nextInt(200000);
    }

    public DataList(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public void addSet(DataList dataList) {
        dataPoints.addAll(dataList.getDataPoints());
        if (dataPoints.size() > 2000000) {
            while (hasToWait = false) {
                //wait
            }
            dataPointsToSave = dataPoints;
            dataPoints = new ArrayList<>();
            outsourceData();
        }
    }

    public void addDataPoint(DataPoint dataPoint) {

        dataPoints.add(dataPoint);
        if (dataPoints.size() > 2000000) {
            while (hasToWait = false) {
                //wait
            }
            dataPointsToSave = dataPoints;
            dataPoints = new ArrayList<>();
            outsourceData();
        }
    }

    private boolean hasToWait = false;

    private void outsourceData() {
        hasToWait = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                filecount += 1;

                Writer writer = null;
                try {
                    File f = new File(OUTSORUCING_PATH + random + "_" + filecount + ".csv");
                    if (!f.exists()) {
                        f.createNewFile();
                    }
                    writer = new BufferedWriter(new FileWriter(f, true));
                    for (DataPoint dataPoint : dataPointsToSave) {
                        writer.write(dataPoint.getSaveLine() + "\n");
                    }
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                hasToWait = false;
            }
        });
        thread.start();

    }

    public boolean hasFiles(){
        if(filecount != 0) return true;
        return false;
    }
    public void sortDataList() {
        dataPoints.sort(new Comparator<DataPoint>() {
            @Override
            public int compare(DataPoint o1, DataPoint o2) {
                return o1.compareTo(o2);
            }
        });
    }

    public List<String> getFiles(){
        List<String> paths = new ArrayList<>();
        for(int i  = 1 ; i< filecount; i++){
            paths.add(OUTSORUCING_PATH + random + "_" +  i + ".csv");
        }
        return paths;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }


}




















