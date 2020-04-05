package edu.upenn.cis350.mapui;

import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;


public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View window;
    private final Context c;

    public MyInfoWindowAdapter(Context context) {
        c = context;
        window = LayoutInflater.from(context).inflate(R.layout.my_info_window, null);
    }

    private void showWindowText (Marker m, View v) {

        // Displaying the url
        String url = m.getTitle();
        TextView tvUrl = (TextView) v.findViewById(R.id.url);
        if (!url.equals("")) {
            tvUrl.setText(url);
        }

        // Displaying the description
        String description = m.getSnippet();
        TextView tvDescription = (TextView) v.findViewById(R.id.description);
        if (!description.equals("")) {
            tvDescription.setText(description);
        }

    }

    @Override
    public View getInfoWindow(Marker marker) {
        showWindowText(marker, window);
        return window;
    }

    @Override
    public View getInfoContents(Marker marker) {
        showWindowText(marker, window);
        return window;
    }
}
