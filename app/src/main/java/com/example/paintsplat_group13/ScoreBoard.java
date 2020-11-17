package com.example.paintsplat_group13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

class Player{
    public int score;
    public String userName;

    public Player(int _score, String user_name){
        score = _score;
        userName = user_name;
    }
}


public class ScoreBoard extends AppCompatActivity{

    String roomName = "";
    String playerName = "";
    FirebaseDatabase database;
    DatabaseReference playerRef;
    DatabaseReference roomRef;

    Button endButton;

    ListView listView;

    List<String> playerList;

    int[] finalScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        listView = findViewById(R.id.listOfPlayerScores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Final ScoreBoard");

        Bundle extras = getIntent().getExtras();
        roomName = extras.getString("roomName");
        playerName = extras.getString("playerName");
        playerList = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        database = FirebaseDatabase.getInstance();
        addScoreListener();

        endButton = (Button)findViewById(R.id.endGame);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeRoomData();
                startHomeScreen();
            }
        });
    }

    private void addScoreListener(){

        playerRef = database.getReference("rooms/" + roomName);
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                playerList.clear();
                Iterable<DataSnapshot> players = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : players) {
                    System.out.println("snapshot " + String.valueOf(snapshot));
                    Iterable<DataSnapshot> iterablePlayers = snapshot.getChildren();
                    for(DataSnapshot childSnapshot : iterablePlayers) {
                        System.out.println("childSnapshot " + String.valueOf(childSnapshot));
                        if (String.valueOf(childSnapshot.getKey()).contains("Score")) {
                            String temp = String.valueOf(childSnapshot.getKey()).split("Score")[0];
                            String playerScore = String.valueOf(((Number) childSnapshot.getValue()).intValue());
                            String playerDetails = "Username:  " + temp + "     Score:  " + playerScore;
                            playerList.add(playerDetails);
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ScoreBoard.this,
                        android.R.layout.simple_list_item_1, playerList);
                listView.setAdapter(adapter);
                System.out.println(playerList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //nothing
            }
        });
    }

    private void removeRoomData() {

        roomRef = database.getReference("rooms/" + roomName);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //nothing
            }
        });
    }

    public void startHomeScreen(){
        final Timer CoolDownTimer = new Timer();  //Starts game timer
        final Intent intent = new Intent(this, HomeScreen.class);
        CoolDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        }, 500);
    }
}
