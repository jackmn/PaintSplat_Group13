package com.example.paintsplat_group13;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class splash extends View {
    private  int h;
    private  int w;
    private int x, y;
    private int xVec, yVec;
    private int ScreenHeight;
    private int ScreenWidth;
    public Paint screen;

    public splash(Context context, float x, float y) {
        super(context);
        screen = new Paint();
        screen.setColor(Color.GREEN);
        x= this.x;
        y=this.y;
    }


}
