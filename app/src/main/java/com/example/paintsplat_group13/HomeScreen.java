package com.example.paintsplat_group13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeScreen extends AppCompatActivity {

    ImageButton userButton;
    Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        userButton = (ImageButton)findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserScreen();
            }
        });

        playButton = (Button)findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGameScreen();
            }
        });

    }

    public void startUserScreen(){
        Intent intent = new Intent(this, UserScreen.class);
        startActivity(intent);
    }

    public void startGameScreen(){
        Intent intent = new Intent(this, gameScreen.class);
        startActivity(intent);
    }

}