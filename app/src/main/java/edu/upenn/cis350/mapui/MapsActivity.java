package edu.upenn.cis350.mapui;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import android.view.*;

import java.util.List;
import android.widget.EditText;
import android.location.Geocoder;
import android.content.Context;
import android.location.Address;



public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


//        final TextView tView = findViewById(R.id.placeName);
//
//        SupportPlaceAutocompleteFragment autocompleteFragment1 = (SupportPlaceAutocompleteFragment)
//                this.getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
//
//        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                tView.setText(place.getName());
//            }
//            @Override
//            public void onError(Status status) {
//                tView.setText(status.toString());
//            }
//        });


//        SupportPlaceAutocompleteFragment autocompleteFragment1 = (SupportPlaceAutocompleteFragment)
//                this.getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment1);
//
//        autocompleteFragment1.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
//            }
//
//            @Override
//            public void onError(Status status) {
//
//            }
//        });

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

        LatLng philly = new LatLng(39.95, -75.16);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(philly));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 9.0f ) );
    }


    public void onSearch( View view) {
        String inputAddress = "";


        //get the user's input into the Search bar and extract the city/state
        inputAddress = ((EditText)findViewById(R.id.input_field)).getText().toString();

//        System.out.println("!!!!!!!!!!!!! " + inputAddress);

        //use geocoder to get coordinates of the place
        LatLng inputPlace = getLocationGivenAddress(this, inputAddress);

        System.out.println("!!!!!!!!!!!!! " + inputPlace.toString());

        //use the camera zoom to go to that place
        mMap.moveCamera(CameraUpdateFactory.newLatLng(inputPlace));

        //make the crawler using the extracted city and state info and
        //user's activity preferences and data from their profile


        //call the crawl method to return a list of Events


        //iterate through the list and get the Address object from each Event
        //from the addresses get the addressline, city, state, zip and put
        //these into the geocoder to get lat and log values to place a marker at

    }


    //method for converting an address to latlgn coordinates
    public LatLng getLocationGivenAddress(Context context, String strAddress) {
        Geocoder gc= new Geocoder(context);
        List<Address> address;
        LatLng point = null;

        try {
            address = gc.getFromLocationName(strAddress, 20);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            point = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return point;
    }



}