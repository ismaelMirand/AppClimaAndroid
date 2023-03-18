package com.example.weatherapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Error extends AppCompatActivity {

    MediaPlayer zombie;
    MediaPlayer exp;
    private Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.error);

        System.out.println("Se creo el error");

        zombie = MediaPlayer.create(this,R.raw.zombie);
        exp = MediaPlayer.create(this,R.raw.exp);

        zombie.start();

        goHome = (Button) findViewById(R.id.error);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exp.start();
                finish();
                goHomeIntent();
            }
        });

    }

    public void goHomeIntent(){
        Intent intentGoHome = new Intent(this, MainActivity.class);
        startActivity(intentGoHome);
    }
}
