package com.example.paintsplat_group13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserScreen extends AppCompatActivity {

    Button enterButton;
    EditText editUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        editUsername = (EditText)findViewById(R.id.editUsername);
        editUsername.getText().toString();

        enterButton = (Button)findViewById(R.id.playButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("text:", editUsername.getText().toString());
                startHomeScreen();
            }
        });
    }

    public void startHomeScreen(){
        Intent intent = new Intent(this, HomeScreen.class);
        startActivity(intent);
    }
}