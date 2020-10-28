package com.example.paintsplat_group13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Timer;
import java.util.TimerTask;

public class gameScreen extends AppCompatActivity {

    private PaintCanvas paintCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paintCanvas = new PaintCanvas(this);
        setContentView(paintCanvas);
        Timer t = new Timer();  //Starts game timer
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                paintCanvas.moveRect();
                paintCanvas.invalidate();   //Calls screen update
            }
        }, 0, 30);
    }
}
