package at.fhv.ale8340.ba2.owndb;

import at.fhv.ale8340.ba2.owndb.Utils.DataPointUtils;
import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.exceptions.NoDataDatabaseExeption;
import at.fhv.ale8340.ba2.owndb.inputparser.Parser;
import at.fhv.ale8340.ba2.owndb.model.DataList;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.sql.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class OwnDBRequestProzess implements Runnable{
    private final Logger logger = (Logger) LoggerFactory.getLogger(OwnDBRequestProzess.class);

	private final IOwnDBAPI ownDB;
	private final Socket socket;

	public OwnDBRequestProzess(IOwnDBAPI ownDB, Socket socket) {
		this.ownDB = ownDB;
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			String inputString = getInputSTring(socket);

			Request request = Parser.parse(inputString);
			logger.debug("REQUEST: {}", request);

			if(request == null){
				logger.error(inputString);
				throw new NullPointerException("REQUEST IS NULL");
			}

			switch (request.getStatementType()){
				case INSERT:
					try{
						DataPoint dataPoint =request.getDataPoint();
						ownDB.saveDataPoint(dataPoint);
						sendanswer();
					}catch (DatabaseExeption e){
						e.printStackTrace();
						sendError();
					}
					break;
				case SELECT:
					executeSelect(request);
					break;
				case COUNT:
					if(!request.getSelectFilter().isAll() && request.getSelectFilter().isSensorid()){
						executeSensorIDS(request);
					}
					executeCOUNT(request);
					break;
				case ERROR:
					sendError();
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getInputSTring(Socket socket) throws IOException {
		String output = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line;
		while((line = br.readLine()) != null){
			output += line;
			if(line.equals("")){
				break;
			}
		}
		System.out.println(output);
		return output;
	}

	private void executeSensorIDS(Request request) {
		try{
			LocalDateTime starDate = null;
			LocalDateTime endDate = null;
			Double min_value = null;
			Double max_value = null;

			if(request.getTimeFilter() != null){
				starDate = request.getTimeFilter().getStartDate();
				endDate = request.getTimeFilter().getEndDate();
			}
			if(request.getValueFilter() != null){
				min_value = request.getValueFilter().getMin_value();
				max_value = request.getValueFilter().getMax_value();
			}
			List<String> ids = ownDB.sensorIdsBetween(min_value, max_value, starDate, endDate);
			sendanswer(ids);
		}catch (NoDataDatabaseExeption e){
			e.printStackTrace();
			sendError();
		}
	}

	private void executeCOUNT(Request request) {
		try{
			LocalDateTime starDate = null;
			LocalDateTime endDate = null;
			List<String> sensorIds = null;
			Double min_value = null;
			Double max_value = null;

			if(request.getTimeFilter() != null){
				starDate = request.getTimeFilter().getStartDate();
				endDate = request.getTimeFilter().getEndDate();
			}
			if( request.getSensorIdFilter() != null){
				sensorIds = request.getSensorIdFilter().getSensorIds();
			}
			if(request.getValueFilter() != null){
				min_value = request.getValueFilter().getMin_value();
				max_value = request.getValueFilter().getMax_value();
			}
			int point = ownDB.countPoints(min_value, max_value, sensorIds, starDate, endDate);
			sendanswer(point);
		}catch (NoDataDatabaseExeption e){
			e.printStackTrace();
			sendError();
		}

	}

	private void executeSelect(Request request) {
		LocalDateTime starDate = null;
		LocalDateTime endDate = null;
		List<String> sensorIds = null;
		Double min_value = null;
		Double max_value = null;

		if(request.getTimeFilter() != null){
			starDate = request.getTimeFilter().getStartDate();
			endDate = request.getTimeFilter().getEndDate();
		}
		if( request.getSensorIdFilter() != null){
			sensorIds = request.getSensorIdFilter().getSensorIds();
		}
		if(request.getValueFilter() != null){
			min_value = request.getValueFilter().getMin_value();
			max_value = request.getValueFilter().getMax_value();
		}
		logger.debug("executeSelect: " + min_value + ","+ max_value);
		DataList dataList = ownDB.requestAllValuesBetween(min_value, max_value, sensorIds, starDate, endDate);
		sendanswer(dataList);
	}

	private void sendError() {
		logger.debug("sendAnswer: ERROR");
		try{
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("ERROR");
			pw.println("=END=");
			pw.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void sendanswer(int i){
		logger.debug("sendAnswer: i={}",i);
		try{

			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("Number of Points = " + i);
			pw.println("=END=");
			pw.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void sendanswer(){
		logger.debug("sendAnswer: INSERTED");
		try{

			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			pw.println("INSERTED");
			pw.println("=END=");

			pw.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	private void sendanswer(List<String> ids) {
		logger.debug("sendAnswer: {}",ids);
		try{

			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			for(String id : ids){
				pw.println(id);
			}
			pw.println("=END=");

			pw.flush();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public void sendanswer(DataList dataList)  {
		logger.debug("sendAnswerX: {}", dataList.getDataPoints().size());

		try{
			PrintWriter pw = new PrintWriter(socket.getOutputStream());
			processDataPoint(dataList, pw);
//			pw.println( "SIZE: "+ dataList.getDataPoints().size());
			pw.println("=END=");
			pw.flush();
		}catch (IOException e){
			e.printStackTrace();
		}




	}

	private void processDataPoint(DataList dataList, PrintWriter pw) {
		if(dataList.hasFiles()){
			for(String filePath: dataList.getFiles()){
				try {
					File myObj = new File(filePath);
					Scanner myReader = new Scanner(myObj);
					while (myReader.hasNextLine()) {
						pw.println(myReader.nextLine());
					}
					pw.flush();
					myReader.close();
				} catch (FileNotFoundException e) {
					System.out.println("An error occurred.");
					e.printStackTrace();
				}

			}
		}
		for(DataPoint dataPoint: dataList.getDataPoints()){
			pw.println(dataPoint.getSaveLine());
			pw.flush();
		}
	}

}
