package com.example.quakedetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<LiveReportRow>>
{
    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=2&limit=20";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    /** Tag for the log messages */
//    private static final String LOG_TAG = MainActivity.class.getSimpleName();

//    ArrayList<LiveReportRow> earthquakes;
    private CustomAdapter adapter;
    ListView earthquakeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Find a reference to the {@link ListView} in the layout
        // so the list can be populated in the user interface
        earthquakeListView = findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new CustomAdapter(this,new ArrayList<>());

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
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        LoaderManager.getInstance(this).initLoader(EARTHQUAKE_LOADER_ID,null,this);
    }


    @NonNull
    @Override
    public Loader<List<LiveReportRow>> onCreateLoader(int id, Bundle args)
    {
        return new EarthquakeLoader(this,SAMPLE_JSON_RESPONSE);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<LiveReportRow>> loader, List<LiveReportRow> data) {
        adapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<LiveReportRow>> loader) {
        adapter.clear();
    }

    //AsyncTask Implementation :
/*    public class EarthQuakeAsynctask extends AsyncTask<String,Void, List<LiveReportRow>>
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
  */

}