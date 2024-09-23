package com.mycompany.oodj_assignment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeSlot {
    private LocalTime start;
    private LocalTime end;
    private String timeRange;
    
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public TimeSlot(String timeRange) {
        String[] times = timeRange.split("-");
        this.start = LocalTime.parse(times[0].trim(), timeFormatter);
        this.end = LocalTime.parse(times[1].trim(), timeFormatter);
    }

    public boolean isOverlapping(TimeSlot other) {
        return start.isBefore(other.end) && other.start.isBefore(end);
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
    public String getTimeRange(){
        return timeRange;
    }

    @Override
    public String toString() {
        return start.format(timeFormatter) + "-" + end.format(timeFormatter);
    }
}
