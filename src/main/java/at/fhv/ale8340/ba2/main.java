package at.fhv.ale8340.ba2;

import at.fhv.ale8340.ba2.owndb.OwnDB;
import at.fhv.ale8340.ba2.owndb.OwnDBMain;
import at.fhv.ale8340.ba2.setUp.SetUpInfluxdb;
import at.fhv.ale8340.ba2.setUp.setUpTimescale;
import at.fhv.ale8340.ba2.testing.TestSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class main {
    private final Logger logger = (Logger) LoggerFactory.getLogger(main.class);


    public static void main(String[] args) {
        System.out.println("Prozess Started");
        if(args.length == 3){

            if(args[0].equals("-mode")){
                if(args[1].equals("setup")){
                    if(args[2].equals("influx")){
                        try {
                            SetUpInfluxdb.setUpInflux();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else if(args[2].equals("timescale")){
                        try {
                            setUpTimescale.intTimescale();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }else if(args[1].equals("start") && args[2].equals("owndb")){
                    OwnDBMain.startOwnDB();
                } else
                if(args[1].equals("execute") && args[2].equals("tests")){
                    TestSetup.executeTests();
                }
            }
        }
        System.out.println("Prozess ENDED");


    }

}
