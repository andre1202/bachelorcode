package at.fhv.ale8340.ba2.owndb.sql.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class TimeFilter {
    private final Logger logger = (Logger) LoggerFactory.getLogger(TimeFilter.class);

    private LocalDateTime startDate;
    private LocalDateTime emdDate;

    @Override
    public String toString() {
        return "TimeFilter{" +
                "startDate=" + startDate +
                ", emdDate=" + emdDate +
                '}';
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return emdDate;
    }

    public TimeFilter(LocalDateTime startDate, LocalDateTime emdDate) {
        this.startDate = startDate;
        this.emdDate = emdDate;
    }
}
