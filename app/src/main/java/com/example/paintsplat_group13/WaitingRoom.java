package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        database = FirebaseDatabase.getInstance();

        //get the player name and assign his room to thr player name
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
//        playerName = preferences.getString("playerName", "");
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

//                    Log.d("playerName", playerName);
                    button.setText("Starting Game");
                    button.setEnabled(false);
                    Intent intent = new Intent(getApplicationContext(), gameScreen.class);
                    intent.putExtra("roomName", roomName);
                    startActivity(intent);
                    GameStarted = "true";
                    GlobalGameStarted.setValue(true);

//                roomName = playerName;
//                roomRef = database.getReference("rooms/" + roomName + "/player1");
//                addRoomEventListener();
//                roomRef.setValue(playerName);
                }
            });
        }
        else{
            button.setVisibility(View.GONE);
            GlobalGameStarted.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.d("HEY", String.valueOf(snapshot.getValue()));
                    if(String.valueOf(snapshot.getValue()) == "true") {
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


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                // join an existing room
//                roomName = roomsList.get(position);
//                roomRef = database.getReference("rooms/" + roomName);
//
//                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(DataSnapshot snap) {
//
//                        for(int i=2; i<5;i++) {
//                            DataSnapshot player = snap.child("player" + String.valueOf(i));
//
//                            if(player.getValue() == null) {
//                                DatabaseReference setRef = roomRef = database.getReference("rooms/" + roomName + "/player" + String.valueOf(i));
//                                addRoomEventListener();
//                                setRef.setValue(playerName);
//                                break;
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });

        //show if new room is available
        addPlayersEventListener();
    }

//    private void addPlayersEventListener(){
//        roomRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //join yhe room
//                button.setText("Create Room");
//                button.setEnabled(true);
//                Intent intent = new Intent(getApplicationContext(), gameScreen.class);
//                intent.putExtra("roomName", roomName);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // error
//                button.setText("Create Room");
//                button.setEnabled(true);
//                Toast.makeText(PlayerLobbies.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
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