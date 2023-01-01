package at.fhv.ale8340.ba2.monitoring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class MonitorLoggerEntry {
    private final Logger logger = (Logger) LoggerFactory.getLogger(MonitorLoggerEntry.class);

	private LocalDateTime localDateTime;
	private double cpu_load; // 0.0 - 1.0 percentage
	private double system_cpu_load; // 0.0 - 1.0 percentage
	private double java_load_on_cpu_load;
	private float memory_load; // 0.0 - 1.0 percentage

	public MonitorLoggerEntry(LocalDateTime localDateTime, double cpu_load, double system_cpu_load, float memory_load) {
		this.localDateTime = localDateTime;
		this.cpu_load = cpu_load;
		this.system_cpu_load = system_cpu_load;
		this.memory_load = memory_load;
		java_load_on_cpu_load = cpu_load / system_cpu_load;

	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public double getJava_load_of_system(){
		return cpu_load;
	}

	public double getCpu_load() {
		return system_cpu_load;
	}

	public float getMemory_load() {
		return memory_load;
	}

	public String getEntryLine(){
		StringBuilder sb = new StringBuilder();
		sb.append(localDateTime);
		sb.append("\t");
		sb.append(cpu_load);
		sb.append("\t");
		sb.append(system_cpu_load);
		sb.append("\t");
		sb.append(java_load_on_cpu_load);
		sb.append("\t");
		sb.append(memory_load);
		return  sb.toString();
	}

}
