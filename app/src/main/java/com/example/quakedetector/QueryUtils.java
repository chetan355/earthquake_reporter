package com.example.quakedetector;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryUtils {
    /** Sample JSON response for a USGS query */
    private static final String SAMPLE_JSON_RESPONSE = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link LiveReportRow} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<LiveReportRow> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<LiveReportRow> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.\
        try
        {
            JSONObject jsonObject = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = jsonObject.getJSONArray("features");

            for(int i=0;i<features.length();i++)
            {
                JSONObject currobj = features.getJSONObject(i);
                JSONObject propobj = currobj.getJSONObject("properties");
                double mag = propobj.getDouble("mag");
                String location = propobj.getString("place");
                long time = propobj.getLong("time");
                String url = propobj.getString("url");

                earthquakes.add(new LiveReportRow(mag,location,time,url));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // build up a list of Earthquake objects with the corresponding data.

        // Return the list of earthquakes
        return earthquakes;
    }
}