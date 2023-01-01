package at.fhv.ale8340.ba2.setUp;

import at.fhv.ale8340.ba2.Constants;
import at.fhv.ale8340.ba2.owndb.OwnDB;
import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.LoadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.SaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class SetUpOwnDb {
    private final Logger logger = (Logger) LoggerFactory.getLogger(SetUpOwnDb.class);


    public static void setup(String[] args) throws IOException {

        LocalDateTime startdate = LocalDateTime.now();
        IloadDataService loadDataService = new LoadDataService(Constants.MAIN_PATH);
        ISaveDataService saveDataService = new SaveDataService(Constants.MAIN_PATH);
        OwnDB ownDB = new OwnDB(loadDataService, saveDataService);


        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("Extended_Data_With_6_Sensors_TSV.csv");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                try {
                    ownDB.saveDataPoint(DataPointUtils.map(line));
                } catch (DatabaseExeption e) {
                    e.printStackTrace();
                }
                //System.out.println(line);
            }
//            // note that Scanner suppresses exceptions
//            if (sc.ioException() != null) {
//                throw sc.ioException();
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }  finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

//        try {
//            File f = new File("Extended_Data_With_6_Sensors_TSV-LAPTOP-3PFLJM6L.csv");
//            BufferedReader bf = new BufferedReader(new FileReader(f));
//
//            String line = bf.readLine();
//
//            while(line != null){
//                System.out.println(LocalDateTime.now() + " :: " + line);
//
//                ownDB.saveDataPoint(DataPointUtils.map(line));
//
//                line = bf.readLine();
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DatabaseExeption databaseExeption) {
//            databaseExeption.printStackTrace();
//        }
        LocalDateTime enddate = LocalDateTime.now();

        System.out.println("--- END ---");
        System.out.println("StartDateTime: " + startdate);
        System.out.println("EndDateTime: " + enddate);

    }
}
