package com.example.weatherapp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PackageManagerCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends AppCompatActivity {

    private Button continueToSantiago;
    private Button continueToChillan;
    private Button continueToConcepcion;
    private Button continueToGps;
    private ImageButton searchButton;

    private AlertDialog.Builder alertBuilder;
    MediaPlayer openChest;
    MediaPlayer closeChest;
    MediaPlayer ground;

    private LocationListener locListener;
    protected LocationManager locManager;
    String lat;
    String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int gpsPermission = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        if(gpsPermission == PackageManager.PERMISSION_DENIED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
            }
        }

        continueToSantiago = (Button) findViewById(R.id.santiagoButton);
        continueToChillan = (Button) findViewById(R.id.chillanButton);
        continueToConcepcion = (Button) findViewById(R.id.concepcionButton);
        continueToGps = (Button) findViewById(R.id.gpsButton);
        searchButton = (ImageButton) findViewById(R.id.search);

        openChest =MediaPlayer.create(this,R.raw.open);
        closeChest =MediaPlayer.create(this,R.raw.close);
        ground=MediaPlayer.create(this,R.raw.ground);

        continueToChillan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ground.start();
                openChillan();
            }
        });

        continueToSantiago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ground.start();
                openSantiago();
            }
        });

        continueToConcepcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ground.start();
                openConcepcion();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChest.start();
                closeChest.start();
                searchCity();
            }
        });

        continueToGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ground.start();
                locManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                Intent intentGps = new Intent(getApplicationContext(), CityWeather.class);

                locListener = new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        String lon = ""+location.getLongitude();
                        String lat = ""+location.getLatitude();
                        saveLonAndLat(lon,lat);
                    }
                };

                ContextCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION);
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(lat == null && lon == null){
                            ContextCompat.checkSelfPermission(MainActivity.this,ACCESS_FINE_LOCATION);
                            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
                            continueToGps.performClick();
                        }else{
                            intentGps.putExtra("lat",lat);
                            intentGps.putExtra("lon",lon);
                            startActivity(intentGps);
                            locManager.removeUpdates(locListener);
                        }
                    }
                },500);
            }
        });

    }

    public void openChillan(){
        Intent intentChillan = new Intent(this, CityWeather.class);
        String city= "Chillan";
        intentChillan.putExtra(Intent.EXTRA_TEXT,city);
        startActivity(intentChillan);
    }

    public void openSantiago(){
        Intent intentSantiago = new Intent(this, CityWeather.class);
        String city= "Santiago";
        intentSantiago.putExtra(Intent.EXTRA_TEXT,city);
        startActivity(intentSantiago);
    }

    public void openConcepcion(){
        Intent intentConcepcion = new Intent(this, CityWeather.class);
        String city= "Concepcion";
        intentConcepcion.putExtra(Intent.EXTRA_TEXT,city);
        startActivity(intentConcepcion);
    }
    public void searchCity(){
        Intent intentSearchCity = new Intent(this, CityWeather.class);
        EditText textToPass = (EditText) findViewById(R.id.inputCityName);
        String cityName = textToPass.getText().toString();
        intentSearchCity.putExtra(Intent.EXTRA_TEXT,cityName);
        startActivity(intentSearchCity);
    }

    public void saveLonAndLat(String lon,String lat){
        this.lat = lat;
        this.lon = lon;
    }
}
