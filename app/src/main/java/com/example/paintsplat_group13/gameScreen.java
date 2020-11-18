package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.StreamSupport;

class boardMoves {

    public int x,y;

    public boardMoves() {}

    public boardMoves(int _x, int _y) {
        this.x = _x;
        this.y = _y;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("x", x);
        result.put("y", y);

        return result;
    }

}

public class gameScreen<sharedPreferences> extends AppCompatActivity {

    private PaintCanvas paintCanvas;
    FirebaseDatabase database;
    DatabaseReference finishGame;
    String playerName = "";
    String roomName= "";
    String role = "";
    String player1 = "";
    String player2 = "";
    String player3 = "";
    String player4 = "";
    int playerCount = 0;
    int scoreplayer = 0;

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
        Log.d("roomName", roomName);
        finishGame = database.getReference("rooms/" + roomName + "/gameRunning");

        final Timer CoolDownTimer = new Timer();  //Starts game timer
        CoolDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("finishGame", String.valueOf(finishGame));
                finishGame.setValue(false);
                Log.d("10 seconds have passed", roomName);
            }
        }, 30000);

        scoreplayer = paintCanvas.getScore();
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
        boardMoves newMoves = new boardMoves(X, Y);
        Map<String, Object> moveUpdates = new HashMap<>();
        moveUpdates.put("rooms/" + roomName + "/" + playerName + "/moves/" + playerCount ,newMoves.toMap());
        database.getReference().updateChildren(moveUpdates);
        playerCount++;
    }

    private void addRoomEventListener(String nameOfPlayer, int color){
        (database.getReference("rooms/" + roomName + "/" + nameOfPlayer +
                "/moves")).addValueEventListener(new CustomUpdateListener(paintCanvas, color));
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
                player2 = String.valueOf(dataSnapshot.child("player2").getValue());
                player3 = String.valueOf(dataSnapshot.child("player3").getValue());
                player4 = String.valueOf(dataSnapshot.child("player4").getValue());

                String[] players = {player1, player2, player3, player4};
                int[] playerColours = {Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW};

                for(int i = 0; i < players.length; i++) {
                    if(players[i] == playerName) {
                        setsplatColour(playerColours[i]);
                    }
                }
                if(!player1.equals(oldPlayer1) && !player1.equals(playerName) && !player1.equals(null)){
                    addRoomEventListener(player1, Color.GREEN);
                }
                if(!player2.equals(oldPlayer2) && !player2.equals(playerName) && !player2.equals(null)){
                    addRoomEventListener(player2, Color.RED);
                }
                if(!player3.equals(oldPlayer3) && !player3.equals(playerName) && !player3.equals(null)){
                    addRoomEventListener(player3, Color.BLUE);
                }
                if(!player4.equals(oldPlayer4) && !player4.equals(playerName) && !player4.equals(null)){
                    addRoomEventListener(player4, Color.YELLOW);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error -retry
                //messageRef.setValue(message);
            }
        });
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setsplatColour(int colour) {
        paintCanvas.setplayerSplat(colour);
    }
}

class CustomUpdateListener implements ValueEventListener {

    private PaintCanvas canvas;
    private int playerColour;

    public CustomUpdateListener(PaintCanvas _canvas, int _playerColour) {
        this.canvas = _canvas;
        this.playerColour = _playerColour;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        DataSnapshot lastchild = null;
        for (DataSnapshot child: snapshot.getChildren()) {
            lastchild = child;
        }
        if (lastchild != null){
            String x = String.valueOf(lastchild.child("x").getValue());
            String y = String.valueOf(lastchild.child("y").getValue());
            canvas.addSplat(Integer. parseInt(x),Integer. parseInt(y),playerColour);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
