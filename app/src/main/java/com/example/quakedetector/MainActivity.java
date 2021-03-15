package com.example.quakedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity
{
    private static final String SAMPLE_JSON_RESPONSE = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=7";
    /** Tag for the log messages */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout

        ArrayList<LiveReportRow> earthquakes = QueryUtils.extractEarthquakes();

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        CustomAdapter adapter = new CustomAdapter(this,earthquakes);
        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               final LiveReportRow liveReportRow = adapter.getItem(position);

                String url = liveReportRow.getUrl();
                Uri uri = Uri.parse(liveReportRow.getUrl());
                Intent webintent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(webintent);
            }
        });
    }

    public class QuakeAsynTask extends AsyncTask<URL,Void,LiveReportRow>
    {
        @Override
        protected LiveReportRow doInBackground(URL... urls) {
            // Create URL object
            URL url = createUrl(SAMPLE_JSON_RESPONSE);
            String jsonresponse = "";
            jsonresponse = makeHttpRequest(url);

            LiveReportRow earthquake = extractFeatureFromJson(jsonresponse);
            return null;
        }

        protected URL createUrl(String stringUrl)
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
        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        protected String makeHttpRequest(URL url)
        {
            // If the URL is null, then return early.
            String jsonresponse = "";
            if(url == null)
            {
                return jsonresponse;
            }
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
                    jsonresponse = readFromStream(inputStream);
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
            return jsonresponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException
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
        private LiveReportRow extractFeatureFromJson(String earthquakejson)
        {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(earthquakejson)) {
                return null;
            }

            try {
                JSONObject baseJsonResponse = new JSONObject(earthquakejson);
                JSONArray featureArray = baseJsonResponse.getJSONArray("features");

                // If there are results in the features array
                if (featureArray.length() > 0) {
                    // Extract out the first feature (which is an earthquake)
                    JSONObject firstFeature = featureArray.getJSONObject(0);
                    JSONObject properties = firstFeature.getJSONObject("properties");

                    // Extract out the magnitude, time, and place values
                    double magnitude = properties.getDouble("mag");
                    long time = properties.getLong("time");
                    String place = properties.getString("place");

                    // Create a new {@link LiveReportRow} object
                    return new LiveReportRow(magnitude, place, time);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
            }
    }

}