package com.hunter.owen.assignment4;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hunter.owen.assignment4.R;

import org.w3c.dom.Text;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity2 extends AppCompatActivity {
    private TextView weather;
    private TextView temperature;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        temperature = findViewById(R.id.temperature);
        weather = findViewById(R.id.weather);
        image = findViewById(R.id.mars);
        //location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Location currentLocation;
        final TextView location_text = findViewById(R.id.location);
        ObjectAnimator rotate = ObjectAnimator.ofFloat(image, "rotation", 10f, -10f);
        rotate.setRepeatMode(ValueAnimator.REVERSE);
        rotate.setRepeatCount(10000);
        rotate.setDuration(2000);
        rotate.start();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionInfo.PROTECTION_NORMAL);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    location_text.setText("Location: " + String.valueOf(Math.sin(longitude * (Math.PI / 180))) + " + " + String.valueOf(latitude * (Math.PI / 180)) + "i");
                    getWeather(location.getLatitude(), location.getLongitude());
                    temperature.setText("");
                }
            }
        });
    }

    private void getWeather(double lattitude, double longitude) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + String.valueOf(lattitude) + "&lon=" + String.valueOf(longitude) + "&appid=715e0ca0d2c21329d77eb7282b51bc4e";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(response);
                setWeather(jsonObject);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity2.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void setWeather(JsonObject jsonObject){
        if(jsonObject.has("weather")){
            if(jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject().has("main")) {
                weather.setText("weather: " + jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("main").getAsString());
            }
        }
        if(jsonObject.has("main")){
            if(jsonObject.get("main").getAsJsonObject().has("temp")){
                temperature.setText("temp: " + jsonObject.get("main").getAsJsonObject().get("temp").getAsString() + "k");
            }

        }
    }

}