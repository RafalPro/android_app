package edu.upenn.cis350.mapui;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.view.*;

import java.util.List;
import android.widget.EditText;
import android.location.Geocoder;
import android.content.Context;
import android.location.Address;
import com.google.android.gms.location.FusedLocationProviderClient;


import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.widget.Toast;
import java.util.Locale;


import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    LatLng userLocation;
    List<Address> addresses;
    private GoogleApiClient googleApiClient;
    private static final String urlStub = "https://maps.googleapis.com/maps/api/geocode/json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and learn when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Philadelphia, PA.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(Task<Location> task) {
//                        if (task == null) {
//                            System.out.println("TASK IS NULL!!!!!!!!!!!!!!!");
//                        }
//                        Location location = task.getResult();
//                        if (location == null) {
//                            System.out.println("!!! NULL LOCATION");
//                        } else {
//                            userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                        }
//                    }
//                }
//        );

//        Location location = mFusedLocationClient.getLastLocation().getResult();
//        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
//        getLastLocation();

        userLocation = new LatLng(39.95, -75.16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 9.0f ) );
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(MapsActivity.this));

        googleMap.setOnMarkerClickListener(this);
    }


    public void onSearch( View view) {
        String inputAddress = "";

        //get the user's input into the Search bar and extract the city/state
        inputAddress = ((EditText)findViewById(R.id.input_field)).getText().toString();

        //invalid-format checking the input from the user
        if (!inputAddress.contains(",")) {
            ((EditText)findViewById(R.id.input_field)).setText("");
            Toast t = Toast.makeText(this,"Invalid Input Format!", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP, 0, 150);
            t.show();
            return;
        }
        String[] input = inputAddress.split(",");
        if (input.length == 0 || input.length < 4 || input[1].length() != 2|| !input[3].contains("-")) {
            ((EditText)findViewById(R.id.input_field)).setText("");
            Toast t = Toast.makeText(this,"Invalid Input Format!", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP, 0, 150);
            t.show();
            return;
        }
        String place = input[0] + "," + input[1];


        //use geocoder api to get coordinates of the place
        LatLng inputPlace = getLocationGivenAddress(this, place);

        //use the camera zoom to go to that place
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inputPlace));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 10.0f ) );

        //make the crawler using the extracted city and state info and
        //user's activity preferences and data from their profile
        Crawler c = new Crawler(input[0], input[1], input[2], input[3]);

        //call the crawl method to return a list of Events
        List<Event> events = c.crawl();

        //iterate through the list and get the Address object from each Event
        //from the addresses get the addressline, city, state, zip and put
        //these into the geocoder to get lat and log values to place a marker at
        for (Event e : events) {
            edu.upenn.cis350.mapui.Address adr = e.getAddress();
            String activitySpot = adr.getAddressLine() + "," + adr.getCity() + "," + adr.getState();
            LatLng activityPlace = getLocationGivenAddress(this, activitySpot);
            final Marker m = mMap.addMarker(new MarkerOptions().position(activityPlace)
                            .title(e.getUrlRegistrationAddress())
                            .snippet(e.getDescription()));
        }
    }


    //method for converting an address to latlgn coordinates
    public LatLng getLocationGivenAddress(Context context, String strAddress) {
        Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
        LatLng point = null;

        try {
            if (isConnected()) {

                point = getJSONAndProcess(strAddress);

//                addresses = gc.getFromLocationName(strAddress, 1);
//                if (addresses == null) {
//                     System.out.println("Addresses was null!");
//                }
//                Address location = addresses.get(0);
//
//                System.out.println(location.getLatitude());
//                System.out.println(location.getLongitude());
//
//                point = new LatLng(location.getLatitude(), location.getLongitude());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return point;
    }


    // Checking if a network connection is present
    private boolean isConnected() {
        ConnectivityManager connectivityManager =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo().isConnected();
    }



    // Method for getting latitude and longitude json from Google
    public LatLng getJSONAndProcess(String fullAddress) {

        try {
            URL url = null;
            url = new URL(urlStub + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&key=AIzaSyCEIVAeRbOj_a7OJj68N0RzRcHxC8bM8KQ"
                    + "&sensor=false");

            // Open the Connection
            URLConnection conn = url.openConnection();

            // Reading in the json response from Google
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();


            // Extracting the lat and long values from the
            String s = out.toString();

            int latIndex = s.indexOf("lat");
            latIndex+=7;
            String latStr = "";
            while(s.charAt(latIndex) != ',' && s.charAt(latIndex) != ' ' &&
                    latStr.length() < 11) {
                latStr += s.charAt(latIndex) + "";
                latIndex++;
            }
            double lat = Double.parseDouble(latStr);

            int lngIndex = s.indexOf("lng");
            lngIndex+=7;
            String lngStr = "";
            while (s.charAt(lngIndex) != ',' && s.charAt(lngIndex) != ' ' &&
                    lngStr.length() < 11) {
                lngStr += s.charAt(lngIndex) + "";
                lngIndex++;
            }
            double lng = Double.parseDouble(lngStr);


            LatLng result = new LatLng(lat, lng);
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onMarkerClick(final Marker m) {
        //display description and url
        m.showInfoWindow();
        return true;
    }







//    private boolean checkPermissions(){
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            return true;
//        }
//        return false;
//    }
//
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                // Granted. Start getting the location information
//            }
//        }
//    }
//
//    private boolean isLocationEnabled(){
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }
//
//
//
//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(Task<Location> task) {
//                                Location uLoc = task.getResult();
//                                if (uLoc == null) {
//                                    //requestNewLocationData();
//                                }
//                                else {
//                                    double userLat = uLoc.getLatitude();
//                                    double userLong = uLoc.getLongitude();
//                                    userLocation = new LatLng(userLat, userLong);
//                                }
//                            }
//                        }
//                );
//            }
//        } else {
//            requestPermissions();
//        }
//    }


}