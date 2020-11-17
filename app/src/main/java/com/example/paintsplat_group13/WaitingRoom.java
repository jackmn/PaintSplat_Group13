package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoom extends AppCompatActivity {

    ListView listView;
    Button button;

    List<String> playerList;

    String playerName = "";
    String roomName = "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference GlobalGameStarted;
    DatabaseReference playerRef;
    String GameStarted = "false";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Players in your lobby");

        database = FirebaseDatabase.getInstance();

        //get the player name and assign his room to thr player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        roomName = getIntent().getExtras().getString("roomName");
        playerName = getIntent().getExtras().getString("playerName");
        listView = findViewById(R.id.listOfPlayers);
        button = findViewById(R.id.startGame);
        //all viable rooms
        playerList = new ArrayList<>();
        GlobalGameStarted = database.getReference("rooms/" + roomName + "/gameRunning");
        Log.d("playerName", playerName);
        Log.d("roomName", roomName);
        if(playerName.equals(roomName)){

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                // create room and add yourself

                button.setText("Starting Game");
                button.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), gameScreen.class);
                intent.putExtra("roomName", roomName);
                intent.putExtra("playerName", playerName);
                startActivity(intent);
                GameStarted = "true";
                GlobalGameStarted.setValue(true);
                }
            });
        }
        else{
            button.setVisibility(View.GONE);
            GlobalGameStarted.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("HEY", String.valueOf(snapshot.getValue()));
                    if (GameStarted == "true" && String.valueOf(snapshot.getValue()).equals("false")){
                        Intent intent = new Intent(getApplicationContext(), ScoreBoard.class);
                        startActivity(intent);
                    }
                    if (String.valueOf(snapshot.getValue()) == "true") {
                        GameStarted = "true";
                        Intent intent = new Intent(getApplicationContext(), gameScreen.class);
                        intent.putExtra("roomName", roomName);
                        startActivity(intent);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        addPlayersEventListener();
    }

    private void addPlayersEventListener(){
//        roomsRef = database.getReference("rooms");
        playerRef = database.getReference("rooms/" + roomName);
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show list of rooms
                if(GameStarted.equals("false")) {
                    playerList.clear();
                    Iterable<DataSnapshot> players = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : players) {
                        playerList.add(String.valueOf(snapshot.getValue()));

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(WaitingRoom.this,
                                android.R.layout.simple_list_item_1, playerList);
                        listView.setAdapter(adapter);
                    }
                }
                Log.d("Trying to enter if", String.valueOf(dataSnapshot));
                if(String.valueOf(dataSnapshot.child("gameRunning").getValue()).equals("false")){
                    Intent intent = new Intent(getApplicationContext(), ScoreBoard.class);
                    intent.putExtra("roomName", roomName);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //nothing
            }
        });
    }
}