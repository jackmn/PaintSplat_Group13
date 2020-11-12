package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class gameScreen extends AppCompatActivity {

    private PaintCanvas paintCanvas;
    FirebaseDatabase database;
    DatabaseReference messageRef;
    String playerName = "";
    String roomName= "";
    String role = "";
    int count = 0;
    String player1 = "";
    String player2 = "";
    String player3 = "";
    String player4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");
        database = FirebaseDatabase.getInstance();
        paintCanvas = new PaintCanvas(this);
        setContentView(paintCanvas);

        Bundle extras = getIntent().getExtras();

        if (extras != null){
            roomName = extras.getString("roomName");
            if (roomName.equals(playerName)){
                role = "host";
            } else{
                role = "guest";
            }
        }
        addPlayerNameListener();
        Log.d("roomName in onCreate", roomName);
        Timer t = new Timer();  //Starts game timer
        t.schedule(new TimerTask() {

            @Override
            public void run() {
               paintCanvas.moveRect();

                paintCanvas.invalidate();   //Calls screen update
            }
        }, 0, 30);
    }

    public void propagateMove(int X, int Y){
        Log.d("Player name", playerName);
        Log.d("roomName", roomName);
        messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveX" + count);
        messageRef.setValue(X);
        messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveY" + count );
        messageRef.setValue(Y);
        messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "counter");
        messageRef.setValue(count);
        count++;
    }

    private void addRoomEventListener(String nameOfPlayer){

//        (database.getReference("rooms/" + roomName)).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String currentCounter = String.valueOf(dataSnapshot);
//                messageRef = database.getReference("rooms/" + roomName + "/" + playerName + "/moveX" + currentCounter);
//                Log.d("currentCounter", currentCounter);
//                Log.d("messageRef", String.valueOf(dataSnapshot.getValue()));


//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // error -retry
//                //messageRef.setValue(message);
//            }
//        });
    }

    private void addPlayerNameListener(){
        database.getReference("rooms/" + roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String oldPlayer1 = player1;
                String oldPlayer2 = player2;
                String oldPlayer3 = player3;
                String oldPlayer4 = player4;

                player1 = String.valueOf(dataSnapshot.child("player1").getValue());
                Log.d("Player 1's name", player1);
                player2 = String.valueOf(dataSnapshot.child("player2").getValue());
                Log.d("Player 2's name", player2);
                player3 = String.valueOf(dataSnapshot.child("player3").getValue());
                Log.d("Player 3's name", player3);
                player4 = String.valueOf(dataSnapshot.child("player4").getValue());
                Log.d("Player 4's name", player4);

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
                Log.d("(dataSnapshot)", String.valueOf(dataSnapshot));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error -retry
                //messageRef.setValue(message);
            }
        });
    }

}
