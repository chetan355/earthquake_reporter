package com.example.quakedetector;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {
    /** Sample JSON response for a USGS query */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils()
    {

    }

    /**
     * Return a list of {@link LiveReportRow} objects that has been built up from
     * parsing a JSON response.
     * @param urls
     */
    public static List<LiveReportRow> fetchEarthquakeData(String urls)
    {
        URL url = QueryUtils.createUrl(urls);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        jsonResponse = QueryUtils.makeHttpRequest(url);

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<LiveReportRow> earthquakes = QueryUtils.extractFeatureFromJson(jsonResponse);

        return earthquakes;
    }

/*    public static ArrayList<LiveReportRow> extractEarthquakes() {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<LiveReportRow> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.\
        try {
            JSONObject jsonObject = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray features = jsonObject.getJSONArray("features");

            for (int i = 0; i < features.length(); i++) {
                JSONObject currobj = features.getJSONObject(i);
                JSONObject propobj = currobj.getJSONObject("properties");
                double mag = propobj.getDouble("mag");
                String location = propobj.getString("place");
                long time = propobj.getLong("time");
                String url = propobj.getString("url");

                earthquakes.add(new LiveReportRow(mag, location, time, url));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }
*/
    protected static URL createUrl(String stringUrl)
    {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }
    protected static String makeHttpRequest(URL url)
    {
        String jsonresponce = "";
        if(url == null)
            return jsonresponce;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try
        {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000); //miliseconds
            urlConnection.setConnectTimeout(15000); //miliseconds
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode()==200)
            {
                inputStream = urlConnection.getInputStream();
                jsonresponce = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally
        {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonresponce;
    }
    protected static String readFromStream(InputStream inputStream)throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static List<LiveReportRow> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<LiveReportRow> earthquakes = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < earthquakeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);

                // For a given earthquake, extract the JSONObject associated with the
                // key called "properties", which represents a list of all properties
                // for that earthquake.
                JSONObject properties = currentEarthquake.getJSONObject("properties");

                // Extract the value for the key called "mag"
                double magnitude = properties.getDouble("mag");

                // Extract the value for the key called "place"
                String location = properties.getString("place");

                // Extract the value for the key called "time"
                long time = properties.getLong("time");

                // Extract the value for the key called "url"
                String url = properties.getString("url");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                LiveReportRow earthquake = new LiveReportRow(magnitude, location, time, url);

                // Add the new {@link Earthquake} to the list of earthquakes.
                earthquakes.add(earthquake);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

        // build up a list of Earthquake objects with the corresponding data.

        // Return the list of earthquakes
}