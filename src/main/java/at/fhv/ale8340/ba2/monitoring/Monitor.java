package at.fhv.ale8340.ba2.monitoring;

import at.fhv.ale8340.ba2.monitoring.model.MonitorLoggerEntry;
import at.fhv.ale8340.ba2.monitoring.model.MonitorLoggerList;
import at.fhv.ale8340.ba2.owndb.Utils.StringUtils;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class Monitor implements Runnable{
    private final Logger logger = (Logger) LoggerFactory.getLogger(Monitor.class);

	private String monitoringID;
	private String filePath;
	private int loggingIntervall;

	private LocalDateTime startDate;
	private LocalDateTime endDate;

	private Thread monitorThread;
	private boolean isRunning;

	private MonitorLoggerList monitorLoggerEntries;


	/**
	 *
	 * @param monitoringID
	 * @param filePath
	 * @param loggingIntervall in Seconds
	 */
	public Monitor(String monitoringID, String filePath, int loggingIntervall){
		this.monitoringID = monitoringID;
		this.filePath = filePath;
		this.loggingIntervall = loggingIntervall;

		isRunning = true;

		monitorLoggerEntries = new MonitorLoggerList();
	}

	public void startMonitor(){
		monitorThread = new Thread(this);
		isRunning = true;
		monitorThread.start();
		startDate = LocalDateTime.now();
	}

	public void stopMonitor(){
		endDate = LocalDateTime.now();
		isRunning = false;

		saveOutputFile();
	}

	private void saveOutputFile() {
		try {
			File filetoSaveDate = new File(StringUtils.pathbuilderPlusFileEnding(filePath, monitoringID));

			System.out.println(filetoSaveDate.getName());
			System.out.println(filetoSaveDate.getAbsolutePath());
			filetoSaveDate.getParentFile().mkdirs();
			if(!filetoSaveDate.exists()){
				filetoSaveDate.createNewFile();
			}
			filetoSaveDate.createNewFile();
			Writer writer = new BufferedWriter(new FileWriter(filetoSaveDate, true));

			writer.write("time\tcpu_load\tsystem_cpu_load\tjava_cpu_load\tmemory_load\n");
			for(MonitorLoggerEntry all: monitorLoggerEntries){
				writer.write(all.getEntryLine() + "\n");
			}
			writer.write("time\tcpu_load\tsystem_cpu_load\tmemory_load\t Average\n");
			writer.write(LocalDateTime.now() + "\t"
					+ monitorLoggerEntries.getAverageCPULoad() +"\t"
					+ monitorLoggerEntries.getAverageSystemCPULoad() +"\t"
					+ monitorLoggerEntries.getAverageMemoryLoad()+"\n");
			writer.write("time\tcpu_load\tsystem_cpu_load\tmemory_load\t Highest\n");
			writer.write(LocalDateTime.now() + "\t"
					+ monitorLoggerEntries.getHighestCPULoad() +"\t"
					+ monitorLoggerEntries.getHighestSystemCPULoad() +"\t"
					+ monitorLoggerEntries.getHighestMemoryLoad()+"\n");
			writer.write("time\tcpu_load\tsystem_cpu_load\tmemory_load\t Lowest\n");
			writer.write(LocalDateTime.now() + "\t"
					+ monitorLoggerEntries.getLowesCPULoad() +"\t"
					+ monitorLoggerEntries.getLowesSystemCPULoad() +"\t"
					+ monitorLoggerEntries.getLowestMemoryLoad()+"\n");
			writer.write("startTime\tendTime\tduration");
			writer.write(startDate + "\t"
					+ endDate + "\t"
					+ getDuration());

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getDuration() {
		long millis = ChronoUnit.MILLIS.between(startDate, endDate);
		return String.format("%02d:%02d:%02d",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis) -
						TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis) -
						TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

	@Override
	public void run() {
		System.out.println("StartRUNNIGN");
		while(isRunning){
//			System.out.println("RUNNIGN");

			OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
					OperatingSystemMXBean.class);

			double systemCpuLoad = osBean.getSystemCpuLoad();
			double javaCpuLoad = osBean.getProcessCpuLoad();
			float freeMemory = osBean.getFreePhysicalMemorySize();
			float totalMemory = osBean.getTotalPhysicalMemorySize();
			float freePercentage = freeMemory/totalMemory;

			if(systemCpuLoad != 0 || freePercentage != 0){
				monitorLoggerEntries.add(new MonitorLoggerEntry(LocalDateTime.now(),
						javaCpuLoad,
						systemCpuLoad,
						freePercentage));

			}


//			System.out.println("sleep");
			try {
				Thread.sleep(loggingIntervall*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

//			System.out.println("Weiter");
		}
	}
}
