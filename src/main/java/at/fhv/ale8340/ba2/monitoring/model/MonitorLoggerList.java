package at.fhv.ale8340.ba2.monitoring.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MonitorLoggerList extends ArrayList<MonitorLoggerEntry> {
    private final Logger logger = (Logger) LoggerFactory.getLogger(MonitorLoggerList.class);

    public String getAverageCPULoad() {
        if(size() == 0 ) return "0";
        double sum = 0;
        for(MonitorLoggerEntry all: this){
            sum += all.getCpu_load();
        }
        double average = sum/this.size();
        return String.valueOf(average);
    }
    public String getHighestCPULoad() {
        if(size() == 0 ) return "0";
        double highest = get(0).getCpu_load();
        for(MonitorLoggerEntry all: this){
            if(all.getCpu_load() > highest){
                highest = all.getCpu_load();
            }
        }
        return String.valueOf(highest);
    }
    public String getLowesCPULoad() {
        if(size() == 0 ) return "0";
        double lowest = get(0).getCpu_load();
        for(MonitorLoggerEntry all: this){
            if(all.getCpu_load() < lowest){
                lowest = all.getCpu_load();
            }
        }
        return String.valueOf(lowest);
    }

    public String getAverageSystemCPULoad() {
        if(size() == 0 ) return "0";
        double sum = 0;
        for(MonitorLoggerEntry all: this){
            sum += all.getJava_load_of_system();
        }
        double average = sum/this.size();
        return String.valueOf(average);
    }
    public String getHighestSystemCPULoad() {
        if(size() == 0 ) return "0";
        double highest = get(0).getJava_load_of_system();
        for(MonitorLoggerEntry all: this){
            if(all.getCpu_load() > highest){
                highest = all.getJava_load_of_system();
            }
        }
        return String.valueOf(highest);
    }
    public String getLowesSystemCPULoad() {
        if(size() == 0 ) return "0";
        double lowest = get(0).getJava_load_of_system();
        for(MonitorLoggerEntry all: this){
            if(all.getCpu_load() < lowest){
                lowest = all.getJava_load_of_system();
            }
        }
        return String.valueOf(lowest);
    }





    public String getAverageMemoryLoad() {
        if(size() == 0 ) return "0";
        double sum = 0;
        for(MonitorLoggerEntry all: this){
            sum += all.getMemory_load();
        }
        double average = sum/this.size();
        return String.valueOf(average);
    }
    public String getHighestMemoryLoad() {
        if(size() == 0 ) return "0";
        double highest = get(0).getCpu_load();
        for(MonitorLoggerEntry all: this){
            if(all.getMemory_load() > highest){
                highest = all.getMemory_load();
            }
        }
        return String.valueOf(highest);
    }
    public String getLowestMemoryLoad() {
        if(size() == 0 ) return "0";
        double lowest = get(0).getCpu_load();
        for(MonitorLoggerEntry all: this){
            if(all.getMemory_load() < lowest){
                lowest = all.getMemory_load();
            }
        }
        return String.valueOf(lowest);
    }
}
