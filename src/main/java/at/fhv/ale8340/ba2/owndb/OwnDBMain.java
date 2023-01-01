package at.fhv.ale8340.ba2.owndb;

import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.LoadDataService;
import at.fhv.ale8340.ba2.owndb.service.impl.SaveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static at.fhv.ale8340.ba2.Constants.MAIN_PATH;

public class OwnDBMain {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(OwnDBMain.class);



	public static void startOwnDB() {

		logger.info("ownDB Server Starting");

		ISaveDataService saveDataService = new SaveDataService(MAIN_PATH);
		IloadDataService loadDataService = new LoadDataService(MAIN_PATH);
		OwnDB ownDB = new OwnDB(loadDataService, saveDataService);


		try(ServerSocket serverSocket = new ServerSocket(9943)) {
			while (true) {

				Socket socket = serverSocket.accept();

				OwnDBRequestProzess CM = new OwnDBRequestProzess(ownDB, socket);
				Thread thread = new Thread(CM);
				thread.start();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}
