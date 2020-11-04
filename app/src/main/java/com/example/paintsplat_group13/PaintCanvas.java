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

import static android.graphics.Color.GREEN;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

class PaintSplat{

    public float x;
    public float y;
    public PaintSplat(float x_x, float y_y){
        this.x = x_x;
        this.y = y_y;
    };
}

public class PaintCanvas extends View {

    private final int height = 700;
    private final int width = 900;
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
    private boolean isOverlapping = false;
    private int splatRadius = 50;

    public PaintCanvas(Context context) {
        super(context);
        this.context= context;
        screen = new Paint();
        screen.setColor(Color.GRAY);
        spot = new Paint();
        spot.setColor(GREEN);
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

        boolean temp = 1 == 1?true:false;
        canvas.drawColor(Color.BLUE);
        super.onDraw(canvas);
        canvas.drawRect(x,y,x+width, y+height,screen);
        for (int i=0; i<splat.size(); i++){
            canvas.drawCircle(splat.get(i).x,splat.get(i).y,splatRadius, spot);
        }
        SpeedUpdate();
    }

    public boolean isSplatOverlapping(List<PaintSplat> splat, PaintSplat splat2){
        isOverlapping = false;

        for(PaintSplat mySplat:splat) {
            double distance = sqrt(pow(splat2.x - mySplat.x, 2) + pow(splat2.y - mySplat.y,2));
            if (mySplat == splat2 || distance < splatRadius*2-1) {
                isOverlapping = true;
            }
        }
        return isOverlapping;
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

    public boolean isOnBoard(float xBoard, float yBoard, float xSplat, float ySplat) {
        return xSplat-splatRadius > xBoard && xSplat + splatRadius < xBoard+width &&
                ySplat-splatRadius > yBoard && ySplat + splatRadius < yBoard+height;
    }
    @Override

    public boolean onTouchEvent(MotionEvent event){

        if(!CoolDownActive) {

            if (isOnBoard(x, y, event.getX(), event.getY())) {
                CoolDownActive = true;

                if(!isSplatOverlapping(splat, (new PaintSplat(event.getX(), event.getY())))) {
                    splat.add(new PaintSplat(event.getX(), event.getY()));
                    System.out.println(splat);
                }

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


