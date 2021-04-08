package com.example.quakedetector;

import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CustomAdapter extends ArrayAdapter<LiveReportRow>
{
    private ArrayList<LiveReportRow> reportList;
    public CustomAdapter(@NonNull MainActivity context, ArrayList<LiveReportRow> list) {
        super(context, 0,list);
        reportList = list;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.simple_list_item, parent, false);
        }
        LiveReportRow liverow = getItem(position);

        //MAGNITUDE SETTER :
        TextView mag = (TextView)listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(liverow.getMagnitude());
        mag.setText(formattedMagnitude);

        int magnitude1Color = ContextCompat.getColor(getContext(), R.color.magnitude1);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(liverow.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        //LOCATION SETTER:
        TextView offset_loc = (TextView)listItemView.findViewById(R.id.location_offset);
        TextView main_loc = (TextView) listItemView.findViewById(R.id.main_location);
        String location = liverow.getLocation();

        if(location.contains(",")) {
            String locArr[] = location.split(",");
            offset_loc.setText(locArr[0]);

            main_loc.setText(locArr[1]);
        }
        else
        {
            offset_loc.setText(R.string.near_by);
            main_loc.setText(location);
        }

        //DATE AND TIME SETTER :
        TextView dateView = (TextView)listItemView.findViewById(R.id.date);

        Date dateobj = new Date(liverow.getTimeInMiliseconds());
        String formattedDate = formatDate(dateobj);
        dateView.setText(formattedDate);

        TextView timeView = (TextView)listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateobj);
        timeView.setText(formattedTime);


        return listItemView;
    }

    public int getMagnitudeColor(double mag) {

        int magResourceId;
        int magnitude = (int) Math.floor(mag);
        switch (magnitude) {
            case 0:
            case 1:
                magResourceId = R.color.magnitude1;
                break;
            case 2:
                magResourceId = R.color.magnitude2;
                break;
            case 3:
                magResourceId = R.color.magnitude3;
                break;
            case 4:
                magResourceId = R.color.magnitude4;
                break;
            case 5:
                magResourceId = R.color.magnitude5;
                break;
            case 6:
                magResourceId = R.color.magnitude6;
                break;
            case 7:
                magResourceId = R.color.magnitude7;
                break;
            case 8:
                magResourceId = R.color.magnitude8;
                break;
            case 9:
                magResourceId = R.color.magnitude9;
                break;
            default:
                magResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magResourceId);
    }
    public String formatMagnitude(double mag)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(mag);
    }
    public String formatDate(Date dateobj)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL-dd-yyyy");
        return dateFormat.format(dateobj);
    }
    public String formatTime(Date dateobj)
    {
        SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");
        return timeformat.format(dateobj);
    }
}
