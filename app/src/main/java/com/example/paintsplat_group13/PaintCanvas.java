package com.example.paintsplat_group13;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

class PaintSplat{

    public float x;
    public float y;
    public PaintSplat(float x_x, float y_y){
        this.x = x_x;
        this.y = y_y;
    };
}

public class PaintCanvas extends View {

    private final int height = 500;
    private final int width = 500;
    private final int boundary = 150;
    private final Context context;
    private int x, y;
    private int xVec, yVec;
    private int ScreenHeight;
    private int ScreenWidth;
    private Paint screen;
    private Paint spot;
    private splash sh;
    private boolean CoolDownActive;
    private List <PaintSplat>splat;
    private int previousSize;

    public PaintCanvas(Context context) {
        super(context);
        this.context= context;
        screen = new Paint();
        screen.setColor(Color.GRAY);
        spot = new Paint();
        spot.setColor(Color.GREEN);
        x = y = 200;
        xVec = 2;   //Speed of change for x coordinate
        yVec = 2;  //Speed of change for y coordinate
        previousSize = 0;
        ScreenWidth= context.getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
        splat = new ArrayList<PaintSplat>();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLUE);
        super.onDraw(canvas);
        canvas.drawRect(x,y,x+width,y+width, screen);
        for (int i=0; i<splat.size(); i++){
            //canvas.drawColor(Color.GREEN);
            canvas.drawRect(splat.get(i).x,splat.get(i).y,splat.get(i).x+50,splat.get(i).y+50, spot);
        }
        SpeedUpdate();
    }

    public void SpeedUpdate(){

        if(splat.size() % 3 == 0 && splat.size()!=previousSize){
            Random rand = new Random();
            previousSize = splat.size();
            xVec = xVec * 2;
            yVec = yVec * 2;
        }
        System.out.println(previousSize);
        System.out.println(xVec);

    }

    public void moveRect() {
        //Checking screen width boundaries
        if ((x + width) + boundary > ScreenWidth || x - boundary < 0) {
            xVec = xVec*-1;
        }
        //Checking screen height boundaries
        if ((y + height) + boundary > ScreenHeight || y - boundary < 0) {
            yVec = yVec*-1;
        }
        //Setting new target position
        x = x + xVec;
        y = y + yVec;

        for (int i=0; i<splat.size(); i++){
            //canvas.drawColor(Color.GREEN);
            splat.get(i).x = splat.get(i).x + xVec;
            splat.get(i).y = splat.get(i).y + yVec;
        }
    }

    public boolean isOnBoard(float x2, float y2, float x, float y) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
    @Override

    public boolean onTouchEvent(MotionEvent event){

        if(!CoolDownActive) {

            if (isOnBoard(event.getX(), event.getY(), x, y)) {
                CoolDownActive = true;

                splat.add(new PaintSplat(event.getX(), event.getY()));
                System.out.println(splat);

                final Timer CoolDownTimer = new Timer();  //Starts game timer
                CoolDownTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        CoolDownActive = false;
                    }
                }, 1000);

            }
        }
        return true;
    }

}


