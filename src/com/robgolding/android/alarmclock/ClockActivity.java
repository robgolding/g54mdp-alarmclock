package com.robgolding.android.alarmclock;

import java.lang.Math;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.format.Time;
import android.util.Log;

public class ClockActivity extends Activity
{
    public static final String TAG = "ClockActivity";

    class ScalableHelper
    {
        int width;
        int height;
        double spWidth;
        double spHeight;

        public ScalableHelper(int canvasWidth, int canvasHeight,
                int scalableX, int scalableY)
        {
            width = canvasWidth;
            height = canvasHeight;
            spWidth = (double) canvasWidth / (double) scalableX;
            spHeight = (double) canvasHeight / (double) scalableY;
            Log.i(TAG, "spWidth: " + spWidth);
            Log.i(TAG, "spHeight: " + spHeight);
        }

        public int getX(int scalableX)
        {
            return (int) (scalableX * spWidth);
        }

        public int getY(int scalableY)
        {
            return (int) (scalableY * spHeight);
        }

        public int getHalfX()
        {
            return (int)Math.round((double)width/2.0);
        }

        public int getHalfY()
        {
            return (int)Math.round((double)height/2.0);
        }
    }

    class ClockView extends View
    {
        Time time;

        public ClockView(Context context)
        {
            super(context);
            time = new Time();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            ScalableHelper s = new ScalableHelper(getWidth(),
                    getHeight(), 100, 200);

            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            canvas.drawColor(Color.RED);
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);

            long millis = System.currentTimeMillis();
            long seconds = millis / 1000;
            int secsInDay = (int) (seconds % 86400);
            int secsInHour = secsInDay % 3600;
            int hour = secsInDay / 3600;
            int minute = secsInHour / 60;
            int second = secsInHour % 60;

            canvas.save();
            for (int i=0; i<12; i++)
            {
                if (i % 3 == 0)
                {
                    canvas.drawRect(new Rect(s.getX(47), s.getY(30), s.getX(49), s.getY(55)), p);
                    canvas.drawRect(new Rect(s.getX(51), s.getY(30), s.getX(53), s.getY(55)), p);
                }
                else
                {
                    canvas.drawRect(new Rect(s.getX(49), s.getY(30), s.getX(51), s.getY(55)), p);
                }
                canvas.rotate(30, s.getHalfX(), s.getHalfY());
            }
            canvas.restore();

            p.setStrokeWidth(8);
            canvas.save();
            canvas.rotate(30 * hour + minute / 2, s.getHalfX(), s.getHalfY());
            canvas.drawLine(s.getHalfX(), s.getY(50), s.getHalfX(), s.getY(120), p);
            canvas.restore();

            p.setStrokeWidth(4);
            canvas.save();
            canvas.rotate(6 * minute, s.getHalfX(), s.getHalfY());
            canvas.drawLine(s.getHalfX(), s.getY(30), s.getHalfX(), s.getY(120), p);
            canvas.restore();

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.RED);
            canvas.drawCircle(s.getHalfX(), s.getHalfY(), s.getY(10), p);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.WHITE);
            p.setStrokeWidth(2);
            canvas.drawCircle(s.getHalfX(), s.getHalfY(), s.getY(10), p);
        }
    }

    ClockView clockView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        clockView = new ClockView(this);
        setContentView(clockView);
    }
}
