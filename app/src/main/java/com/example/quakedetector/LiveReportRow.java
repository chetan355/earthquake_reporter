package com.example.quakedetector;

public class LiveReportRow {
private double magnitude;
private String location;
private long date;
private String url;
    public LiveReportRow(double mag, String location, long date,String url)
    {
        this.magnitude = mag;
        this.location = location;
        this.date = date;
        this.url = url;
    }

    public LiveReportRow(double magnitude, String place, long time) {
        this.magnitude = magnitude;
        this.location = place;
        this.date = time;
    }

    public double getMagnitude()
    {
        return magnitude;
    }
    public String getLocation()
    {
        return location;
    }
    public long getTimeInMiliseconds()
    {
        return date;
    }
    public String getUrl()
    {
        return url;
    }
}
