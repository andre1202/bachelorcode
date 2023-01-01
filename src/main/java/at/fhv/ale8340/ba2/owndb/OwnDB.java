package at.fhv.ale8340.ba2.owndb;

import at.fhv.ale8340.ba2.owndb.exceptions.DatabaseExeption;
import at.fhv.ale8340.ba2.owndb.exceptions.NoDataDatabaseExeption;
import at.fhv.ale8340.ba2.owndb.model.DataPoint;
import at.fhv.ale8340.ba2.owndb.model.DataList;
import at.fhv.ale8340.ba2.owndb.service.ISaveDataService;
import at.fhv.ale8340.ba2.owndb.service.IloadDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OwnDB  implements IOwnDBAPI{
    private final Logger logger = (Logger) LoggerFactory.getLogger(OwnDB.class);

	private final IloadDataService loadDataService;
	private final ISaveDataService saveDataService;

	public OwnDB(IloadDataService loadDataService, ISaveDataService saveDataService) {
		this.loadDataService = loadDataService;
		this.saveDataService = saveDataService;
	}


	@Override
	public DataList requestAllValuesBetween(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) {
		return loadDataService.requestAllValuesBetween(min_value, max_value, sensorIds, startDate, endDate);
	}

	@Override
	public int countPoints(Double min_value, Double max_value, List<String> sensorIds, LocalDateTime startDate, LocalDateTime endDate) throws NoDataDatabaseExeption {
		return loadDataService.countPoints(min_value, max_value, sensorIds, startDate, endDate);
	}

	@Override
	public List<String> sensorIdsBetween(Double min_value, Double max_value, LocalDateTime startDate, LocalDateTime endDate) throws NoDataDatabaseExeption {
		return loadDataService.sensorIdsBetween(min_value, max_value, startDate, endDate);
	}

	@Override
	public void saveDataPoint(DataPoint dataPoint) throws DatabaseExeption {
		saveDataService.saveDataPoint(dataPoint);
	}

	@Override
	public void saveData(DataList dataList) throws DatabaseExeption {
		saveDataService.saveData(dataList);
	}
}
