package com.homepage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocationHistory {
    private double x;
    private double y;
    private LocalDateTime timestamp;

    public LocationHistory(double x, double y, LocalDateTime timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }
}
