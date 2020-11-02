package com.example.paintsplat_group13;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

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
    private splash sh;
    private boolean b;

    public PaintCanvas(Context context) {
        super(context);
        this.context= context;
        screen = new Paint();
        screen.setColor(Color.GRAY);
        x = y = 200;
        xVec = 2;   //Speed of change for x coordinate
        yVec = 10;  //Speed of change for y coordinate
        ScreenWidth= context.getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = context.getResources().getDisplayMetrics().heightPixels;

    }


    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLUE);
        super.onDraw(canvas);
        canvas.drawRect(x,y,x+width,y+width, screen);
        if (b==true){
            canvas.drawColor(Color.GREEN);
            canvas.drawCircle(x,y,1,screen);
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

    //public boolean isOnBoard(float x2, float y2, float x, float y) {
     //   return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    //}
    @Override

    public boolean onTouchEvent(MotionEvent event){
           // if(isOnBoard(event.getX(),event.getY(),0,0)){
            b= true;
           // }
        return true;
    }

}


