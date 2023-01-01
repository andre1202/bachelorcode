package at.fhv.ale8340.ba2.owndb.inputparser;

import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.sql.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.IconUIResource;
import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(Parser.class);



	public static Request parse(String inputString){
		Request ouput = null;
		String[] inputArray = inputString.split(" ");
		StatementType statementType = StatementType.getType(inputArray[0]);
		//Check last element if is a ;
		if(inputArray[inputArray.length-1].charAt(inputArray[inputArray.length-1].length()-1) == ';'){
			switch (statementType){
				case SELECT:
					ouput = parseSelect(inputString);
					break;
				case INSERT:
					ouput = parseInsert(inputString);
					break;
				case COUNT:
					ouput = parseCount(inputString);

					break;
				case ERROR:
					ouput = new Request(statementType,null, null, null, null);
					break;
			}
		}

		return ouput;

	}

	private static Request parseCount(String inputString) {
		return parseWithFilters(StatementType.COUNT, inputString);
	}

	private static Request parseInsert(String inputString) {

		DataPoint dataPoint = getDatapoint(inputString);

		return new Request(dataPoint);
	}

	private static DataPoint getDatapoint(String inputString) {
		String[] values = inputString.split(";")[0].split("INSERT ")[0].split(", ");

		Timestamp timestamp  =Timestamp.valueOf(values[0]);
		String sensorid = values[1];
		float value = Float.valueOf(values[2]);

		return new DataPoint(timestamp, sensorid, value);

	}

	private static Request parseSelect(String inputString) {
		return parseWithFilters(StatementType.SELECT, inputString);
	}


	private static Request parseWithFilters(StatementType statementType, String inputString) {
		String[] inputArray = inputString.split(" WHERE ");

		SelectFilter selectFilter = parseSelectFileter(inputArray[0]);
		TimeFilter timeFilter =  null;
		SensorIdFilter sensorIdFilter = null;
		ValueFilter valueFilter = null;


		if(inputArray.length >= 2){
			String[] filters = inputArray[1].split(" AND ");

			for(String filter: filters){
				String[] splittedFilter = filter.split(" ");
				switch (splittedFilter[0]){
					case "TIME":
						timeFilter = parseTimeFilter(splittedFilter);
						continue;
					case "SENSORID":
						sensorIdFilter = parseSensorIdFilter(filter);
						continue;
					case "VALUE":
						valueFilter = parseValueFilter(splittedFilter);
						continue;
				}


			}

		}
		return new Request(statementType, selectFilter, timeFilter, sensorIdFilter, valueFilter);

	}

	private static ValueFilter parseValueFilter(String[] splittedFilter) {
		Double min_value = null;
		Double max_value = null;
		if(splittedFilter.length == 6 && splittedFilter[3].equals("VALUE")){
			min_value = Double.parseDouble(splittedFilter[1]);
			max_value = Double.parseDouble(splittedFilter[5].split(";")[0]);
		}else if(splittedFilter.length == 4 && splittedFilter[3].equals("VALUE;")){
			min_value = Double.parseDouble(splittedFilter[1]);
		}else if(splittedFilter.length == 4 && splittedFilter[1].equals("VALUE")){
			max_value = Double.parseDouble(splittedFilter[3].split(";")[0]);
		}

		return new ValueFilter(min_value, max_value);
	}

	private static SensorIdFilter parseSensorIdFilter(String filter) {
		logger.debug("Filter IDFILTER: " + filter);
		List<String> sensorIds = new ArrayList<>();

		String[] splittet = filter.split("\"");
		logger.debug("Length: " + splittet.length);
		logger.debug("Splitted: " + splittet);

		for(int i = 1; i <= splittet.length-2; i++){
			logger.debug(splittet[i]);
			if(!splittet[i].equals(",")){
				logger.debug("Part: " + splittet[i]);
				sensorIds.add(splittet[i]);
			}
		}
		return new SensorIdFilter(sensorIds);

	}

	private static TimeFilter parseTimeFilter(String[] splittedFilter) {
		LocalDateTime stardate = null;
		LocalDateTime endDate = null;
		if(splittedFilter.length == 6 && splittedFilter[3].equals("TIMESTAMP")){
			stardate = parseLocalDateTime(splittedFilter[1]);
			endDate = parseLocalDateTime(splittedFilter[5].split(";")[0]);
		}else if(splittedFilter.length == 4 && splittedFilter[3].equals("TIMESTAMP;")){
			stardate = parseLocalDateTime(splittedFilter[1]);
		}else if(splittedFilter.length == 4 && splittedFilter[1].equals("TIMESTAMP")){
			endDate = parseLocalDateTime(splittedFilter[3].split(";")[0]);
		}

		return new TimeFilter(stardate, endDate);
	}

	private static SelectFilter parseSelectFileter(String selectFilter) {
		String[] selectFilterd = selectFilter.split(" ");

		boolean valueBoolean = false;
		boolean allBoolean = false;
		boolean idsBoolean = false;
		boolean tsBoolean = false;

		for(String all: selectFilterd){
			if(all.equals("SELECT") || all.equals("INSERT") || all.equals("COUNT")){
				//Do NOTHING
			}
			switch (all){
				case "*":
					allBoolean = true;
					continue;
				case "timestamp":
					tsBoolean = true;
					continue;
				case "sensorid":
					idsBoolean = true;
					continue;
				case "value":
					valueBoolean = true;
					continue;
			}
		}
		return new SelectFilter(allBoolean, tsBoolean, idsBoolean, valueBoolean);
	}



	private static LocalDateTime parseLocalDateTime(String input){

		try{
			String[] tsplit = input.split("T");
			String[] date = tsplit[0].split("-");
			String[] time = tsplit[1].split(":");

			int year = Integer.parseInt(date[2]);
			int month = Integer.parseInt(date[1]);
			int day = Integer.parseInt(date[0]);
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			int second = Integer.parseInt(time[2]);

			return LocalDateTime.of(
					year, month, day,
					hour, minute,second
			);

		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
