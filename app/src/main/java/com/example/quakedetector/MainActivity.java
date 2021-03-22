package com.example.quakedetector;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=15";
    /** Tag for the log messages */
//    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    ArrayList<LiveReportRow> earthquakes;
    private CustomAdapter adapter;
    ListView earthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find a reference to the {@link ListView} in the layout
        // so the list can be populated in the user interface
        earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new CustomAdapter(this,new ArrayList<LiveReportRow>());

        // Set the adapter on the {@link ListView}
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final LiveReportRow liveReportRow = adapter.getItem(position);
                Uri uri = Uri.parse(liveReportRow.getUrl());
                Intent webintent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(webintent);
            }
        });
        EarthQuakeAsynctask task = new EarthQuakeAsynctask();
        task.execute(SAMPLE_JSON_RESPONSE);
    }
    //AsyncTask Implementation :

    public class EarthQuakeAsynctask extends AsyncTask<String,Void, List<LiveReportRow>>
    {
        @Override
        protected List<LiveReportRow> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null)
                return null;
            List<LiveReportRow> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<LiveReportRow> data)
        {
            adapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }

    }

}