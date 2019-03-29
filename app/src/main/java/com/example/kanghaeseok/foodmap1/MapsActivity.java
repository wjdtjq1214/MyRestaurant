package com.example.kanghaeseok.foodmap1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    String result;
    ArrayList<restaurant> restaurantsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try {
            URL url = new URL("http://13.209.39.126:8000/getRestaurantList");
            InputStream is = url.openConnection().getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuffer buffer = new StringBuffer();

            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }

            result = buffer.toString();

            Log.i("json", result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            JSONArray jList = new JSONArray(result);
            for (int i = 0; i < jList.length(); i++) {
                JSONObject jsonObject = jList.getJSONObject(i);
                restaurant restaurantObj = new restaurant();
                restaurantObj.setrId(jsonObject.getInt("rId"));
                restaurantObj.setrName(jsonObject.getString("rName"));
                restaurantObj.setrNum(jsonObject.getString("rNum"));
                restaurantObj.setrX(jsonObject.getString("rX"));
                restaurantObj.setrY(jsonObject.getString("rY"));
                restaurantObj.setrAdd(jsonObject.getString("rAdd"));
                restaurantObj.setrImage(jsonObject.getString("rImage"));
                restaurantsList.add(restaurantObj);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng startingPoint = new LatLng(37.4710317, 126.8498153);
        mMap = googleMap;
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {

        }

        for(int i = 0; i < restaurantsList.size(); i++){
            restaurant restaurantObj = restaurantsList.get(i);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(restaurantObj.getrY()), Double.parseDouble(restaurantObj.getrX())))
                    //.position(new LatLng(10, 10))
                    .title(restaurantObj.getrName())).setTag(i);
        }

        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startingPoint,15));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        restaurant restaurantObj = restaurantsList.get(Integer.parseInt(marker.getTag().toString()));
        Intent i = new Intent(MapsActivity.this, RestaurantDetail.class);
        Log.i("rId", " " + restaurantObj.getrId());
        i.putExtra("rId", restaurantObj.getrId());
        i.putExtra("rName", restaurantObj.getrName());
        i.putExtra("rAdd", restaurantObj.getrAdd());
        i.putExtra("rNum", restaurantObj.getrNum());
        i.putExtra("rImage", restaurantObj.getrImage());
        MapsActivity.this.startActivity(i);
    }
}
