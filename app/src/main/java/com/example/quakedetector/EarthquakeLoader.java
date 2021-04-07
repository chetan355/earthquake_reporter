package com.example.quakedetector;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<LiveReportRow>> {
    final private String url;
    public EarthquakeLoader(@NonNull Context context,String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Nullable
    @Override
    public List<LiveReportRow> loadInBackground() {
        if(url==null)
            return null;
//        List<LiveReportRow> result = QueryUtils.fetchEarthquakeData(url);
        return QueryUtils.fetchEarthquakeData(url);
    }
}
