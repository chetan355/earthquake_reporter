package com.example.quakedetector;

public class LiveReportRow {
private final double magnitude;
private final String location;
private final long date;
private final String url;
    public LiveReportRow(double mag, String location, long date,String url)
    {
        this.magnitude = mag;
        this.location = location;
        this.date = date;
        this.url = url;
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
