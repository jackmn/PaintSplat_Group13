package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserScreen extends AppCompatActivity {

    EditText editText;
    Button button;

    String playerName = "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    Button enterButton;
    EditText editUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        editText = findViewById(R.id.editUsername);
        button = findViewById(R.id.playButton);

        database = FirebaseDatabase.getInstance();
        // Check if the player exists and get reference
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        if(!playerName.equals("")){
            playerRef = database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){
                //logging the player in
                playerName = editText.getText().toString();
                editText.setText("");
                if(!playerName.equals("")) {
                    button.setText("Logging In");
                    button.setEnabled(false);
                    playerRef = database.getReference("players/" + playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });
    }

    private void addEventListener(){
        // read from the database
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //success - continue to the next screen after saving the player name
                if(!playerName.equals("")) {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), PlayerLobbies.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                button.setText("LOG IN");
                button.setEnabled(true);
                Toast.makeText(UserScreen.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

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