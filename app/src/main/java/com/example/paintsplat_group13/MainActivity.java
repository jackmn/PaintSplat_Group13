package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button button;

    String playerName = "";
    String roomName= "";
    String role = "";
    String message = "";
    int count = 0;
    FirebaseDatabase database;
    DatabaseReference messageRef;
    String player1 = "";
    String player2 = "";
    String player3 = "";
    String player4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        button = findViewById(R.id.poke);
        //button.setEnabled(false);
        database = FirebaseDatabase.getInstance();
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            roomName = extras.getString("roomName");
            if (roomName.equals(playerName)){
                role = "host";
            } else{
                role = "guest";
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //button.setEnabled(false);
                messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveX" + count);
                //message = role + move; //every other one
                messageRef.setValue(1);
                messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveY" + count );
                //message = role + move; //every other one
                messageRef.setValue(2);
                count++;
            }
        });
        //listen for incoming messages
      //  messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveX" + count);
          message = role + ": Setting up game!"; // initial
    //    messageRef.setValue(message);
        System.out.println("Hello");
        addPlayerNameListener();


    }

    private void addRoomEventListener(String nameOfPlayer){
        Log.d("Inroomlistlook@", nameOfPlayer);
        (database.getReference("rooms/" + roomName + "/" + nameOfPlayer)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Log.d("In room listener", String.valueOf(dataSnapshot));
//                    if (dataSnapshot.getValue(String.class).contains("guest")){
//                    //if (dataSnapshot.getKey().contains("guest")){
//                        //button.setEnabled(true);
//                Toast.makeText(MainActivity.this, "" +
//                        dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
//                    }

//                else{
//                    Log.d("Hello", String.valueOf(dataSnapshot));
//                    if (dataSnapshot.getValue(String.class).contains("host")){
//                        //button.setEnabled(true);
//                        Toast.makeText(MainActivity.this, "" +
//                                dataSnapshot.getValue(String.class).replace("host:", ""), Toast.LENGTH_SHORT).show();
//                    }
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error -retry
                //messageRef.setValue(message);
            }
        });
    }

    private void addPlayerNameListener(){
        database.getReference("rooms/" + roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String oldPlayer1 = player1;
                String oldPlayer2 = player2;
                String oldPlayer3 = player3;
                String oldPlayer4 = player4;
//                if (player1 == "" || player1 == null) {
//                    if (String.valueOf(dataSnapshot.child("player1").getValue()) != null) {
                        player1 = String.valueOf(dataSnapshot.child("player1").getValue());
                        Log.d("Player 1's name", player1);
//                    }
//                }
//                if (player2 == "" || player2 == null) {
//                    if (String.valueOf(dataSnapshot.child("player2").getValue()) != null) {
                        player2 = String.valueOf(dataSnapshot.child("player2").getValue());
                        Log.d("Player 2's name", player2);
//                    }
//                }
//                if (player3 == "" || player3 == null) {
//                    if (String.valueOf(dataSnapshot.child("player3").getValue()) != null) {
//                        Log.d("SETTINGTONULLDIDNTWORK", player3);
                        player3 = String.valueOf(dataSnapshot.child("player3").getValue());
                        Log.d("Player 3's name", player3);
//                    }
//                }
//                if (player4 == "" || player4 == null) {
//                    if (String.valueOf(dataSnapshot.child("player4").getValue()) != null) {
                        player4 = String.valueOf(dataSnapshot.child("player4").getValue());
                        Log.d("Player 4's name", player4);
//                    }
//                }
                if(!player1.equals(oldPlayer1) && !player1.equals(playerName) && !player1.equals(null)){
                    addRoomEventListener(player1);
                }
                if(!player2.equals(oldPlayer2) && !player2.equals(playerName) && !player2.equals(null)){
                    addRoomEventListener(player2);
                }
                if(!player3.equals(oldPlayer3) && !player3.equals(playerName) && !player3.equals(null)){
                    addRoomEventListener(player3);
                }
                if(!player4.equals(oldPlayer4) && !player4.equals(playerName) && !player4.equals(null)){
                    addRoomEventListener(player4);
                }
            }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
               // error -retry
                //messageRef.setValue(message);
            }
        });
    }

}
