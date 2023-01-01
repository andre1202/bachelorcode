package at.fhv.ale8340.ba2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Constants {
    private final Logger logger = (Logger) LoggerFactory.getLogger(Constants.class);

	public static final String MAIN_PATH = getPropertie("MAIN_PATH");
	public static final String MONITORING_PATH(String db){

        StringBuilder sb = new StringBuilder();
        sb.append(getPropertie("MONITORING_PATH"));
        sb.append(db);
        sb.append("/");
        return sb.toString();

    }
	public static final String OUTSORUCING_PATH = getPropertie("OUTSORUCING_PATH");

    //INFLUXDB
    public  static final String INFLUX_URL = getPropertie("influx_url");
    public  static final String INFLUX_TOKEN = getPropertie("influx_token");
    public  static final String INFLUX_BUCKET = getPropertie("influx_bucket");
    public  static final String INFLUX_ORG = getPropertie("influx_org");

    //TIMESCALE
    public static final String CONNECTION_URL = getPropertie("timescale_connection");

    public static final String getPropertie(String key){
        Properties properties = new Properties();
        File f = new File("settings.properties");
        try {
            properties.load(new FileInputStream(f));
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }




    public static List<String> GETALL_SENSORS(){
        List<String> sensors  = new ArrayList<>();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("Extended_Data_With_6_Sensors_TSV.csv");
            Scanner sc = new Scanner(inputStream, "UTF-8");
            int counter = 0;
            while (sc.hasNextLine() && counter < 6) {
                String line = sc.nextLine();
                sensors.add(line.split("\t")[1]);
                counter++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return sensors;
    }

    public static List<String> getTestingSensorIDS(){
        List<String> output = new ArrayList<>();
        output.add("Ana3 - 3: T.Abluft");
        return output;
    }

}
