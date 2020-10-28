package com.example.paintsplat_group13;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class PaintCanvas extends View {

    private final int height = 500;
    private final int width = 500;
    private final int boundary = 150;
    private int x, y;
    private int xVec, yVec;
    private int ScreenHeight;
    private int ScreenWidth;
    private Paint paint;

    public PaintCanvas(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.RED);
        x = y = 200;
        xVec = 2;   //Speed of change for x coordinate
        yVec = 10;  //Speed of change for y coordinate
        ScreenWidth= context.getResources().getDisplayMetrics().widthPixels;
        ScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(x,y,x+width,y+width, paint);
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
}


