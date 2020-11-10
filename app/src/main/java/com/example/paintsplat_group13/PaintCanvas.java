package com.example.paintsplat_group13;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
    private final Context context; // need for screen sizing
    private int x, y;
    private int xVec, yVec;
    private int ScreenHeight;
    private int ScreenWidth;
    private Paint screen;
    private Paint spot;
    private boolean CoolDownActive;
    private List <PaintSplat>splat;
    private int previousSize;
    private boolean isOverlapping = false;
    private int splatRadius = 50;
    private Bitmap bitmap;
    private Canvas test_canvas;

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

        //Set up board
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmap = Bitmap.createBitmap(width, height, conf);
        test_canvas = new Canvas(bitmap);
        test_canvas.drawColor(Color.BLACK);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,x, y, new Paint());
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
    }

    public boolean isOnBoard(float xBoard, float yBoard, float xSplat, float ySplat) {
        return xSplat - splatRadius > xBoard && xSplat + splatRadius < xBoard+width &&
                ySplat - splatRadius > yBoard && ySplat + splatRadius < yBoard+height;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(!CoolDownActive) {
            // record x and y at time of entering function (may change during the func)
            int tempx = x;
            int tempy = y;

            if (isOnBoard(tempx, tempy, event.getX(), event.getY())) {
                CoolDownActive = true;
                PaintSplat newSplat = new PaintSplat(event.getX() - tempx, event.getY()-tempy);
                if(!isSplatOverlapping(splat, newSplat)) {
                    splat.add(newSplat);
                    test_canvas.drawCircle(newSplat.x,newSplat.y,splatRadius, spot);
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


