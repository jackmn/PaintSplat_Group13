package com.example.paintsplat_group13;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
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
        messageRef = database.getReference("rooms/" + roomName + "/message");
        message = role + ": Setting up game!"; // initial
        messageRef.setValue(message);

        addRoomEventListener();
    }

    private void addRoomEventListener(){
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (role.equals("host")){
                    if (dataSnapshot.getValue(String.class).contains("guest:")){
                        //button.setEnabled(true);
                        Toast.makeText(MainActivity.this, "" +
                                dataSnapshot.getValue(String.class).replace("guest:", ""), Toast.LENGTH_SHORT).show();
                    }
                } else{
                    if (dataSnapshot.getValue(String.class).contains("host:")){
                        //button.setEnabled(true);
                        Toast.makeText(MainActivity.this, "" +
                                dataSnapshot.getValue(String.class).replace("host:", ""), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // error -retry
                messageRef.setValue(message);
            }
        });
    }

//    public Object setObject(int number1, int number2, int number3){
//        Object temp: {
//                int numbera = number1,
//        }
//    }
}
