package com.nutticha.assign08_6206021620035;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import java.util.Random;

public class GraphicsView extends View implements View.OnTouchListener {
    private Paint p;
    private float[] X = new float[5];
    private float[] Y = new float[5];
    private int[] SPEED = new int[5];
    private boolean finish = false;
    private Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.ufo);
    private Bitmap boom = BitmapFactory.decodeResource(getResources(),R.drawable.ufo_bm);
    private int score, time;
    private CountDownTimer timer1, timer2 ;
    private final int width = Resources.getSystem().getDisplayMetrics().widthPixels;
    private final int height = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Random rnd = new Random();
    private int[] imageWidth = new int[5];
    private int[] imageHeight = new int[5];
    private boolean[] SHOOT = new boolean[5];

    private SoundPlayer sound;

    public GraphicsView(Context context) {
        super(context);
        setBackgroundColor(Color.rgb( 0, 0, 0));
        p = new Paint();
        score = 0;
        time = 0;
        setOnTouchListener(this);
        sound = new SoundPlayer(context);

        for(int i =0  ; i < 5 ; i++){

            imageWidth[i] = image.getWidth();
            imageHeight[i] = image.getHeight();
            X[i] = rnd.nextInt(width - imageWidth[i]);
            SPEED[i] = rnd.nextInt(10) + 10 ;
            SHOOT[i]  = false;
        };

        timer1 = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                time ++;
                invalidate();
            }
            public void onFinish() {
                finish=true;
                invalidate();
            }
        };

        timer2 = new CountDownTimer(30000, 50) {
            public void onTick(long millisUntilFinished) {
                for(int i =0 ; i < 5 ; i++){
                    Y[i] += SPEED[i];
                    if(Y[i] > (height + imageHeight[i])){
                        Y[i] = 0 - imageHeight[i];
                        X[i] = rnd.nextInt(width - imageWidth[i]);
                    }
                }
                invalidate();
            }
            public void onFinish() {
                finish = true;
                invalidate();
            }
        };

        timer1.start();
        timer2.start();
    }
    @Override
    public boolean onTouch(View view, MotionEvent event)
    {
        if (finish) {
            finish = false;
            timer1.start();
            timer2.start();
            score = 0;
            time = 0;
            invalidate();
        }
        else {
            float x = event.getX();
            float y = event.getY();

            for(int i = 0 ; i < 5 ; i++){
                if(x > X[i] && x < X[i] + imageWidth[i]){
                    if( y > Y[i] && y < Y[i] + imageHeight[i]){
                        sound.playHitSound();
                        SHOOT[i]  = true;
                        score++;
                        invalidate();
                    }
                }
            }

        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (finish) {
            p.setColor(Color.GREEN);
            p.setTextSize(60);
            p.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("TIME OUT!!!!", width/2, height/2-100, p);
            canvas.drawText("Press for play again or Back to exit", width/2, height/2 + 100, p);

        }
        else {
            p.setColor(Color.CYAN);
            p.setTextSize(50);
            p.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("Score : " + score, 20, 60, p);
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText("Time : " + time, width-20, 60, p);

            // Draw piwpiw
            for(int i =0 ; i < 5 ; i++){
               if(SHOOT[i]  == true){
                   canvas.drawBitmap(boom , X[i] , Y[i] , null);
                   Y[i] = 0 - imageHeight[i];
                   X[i] = rnd.nextInt(width - imageWidth[i]);
                   SHOOT[i]  = false;
               }
               else {
                   canvas.drawBitmap(image , X[i] , Y[i] , null);
               }
            }
        }
    }
}