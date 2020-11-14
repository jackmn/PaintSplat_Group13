package com.example.paintsplat_group13;



import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Board extends AppCompatActivity {
    //Screen
    private int screenWidth;
    private int screenHeight;

    //Image
    private ImageView board;
    //Pos
    private float boardX;
    private float boardY;

    //class initializer
    private Handler handler= new Handler();
    private Timer timer= new Timer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        board = (ImageView)findViewById(R.id.board);

        //Get screen size
        WindowManager wm= getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth=size.x;
        screenHeight=size.y;

        //move out of screen
        //board.setX(-80.0f);
        //board.setY(-80.0f);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePos();
                    }
                });
            }
        },0,20);
    }

    public void changePos(){
        //up
        boardY-=10;
        if (board.getY() + board.getHeight()<0){
            boardX=(float)Math.floor(Math.random() * (screenWidth-board.getMaxWidth()));
            boardY=screenHeight + 100.0f;
        }
        board.setX(boardX);
        board.setY(boardY);
    }

    public void invalidate() {
    }
}