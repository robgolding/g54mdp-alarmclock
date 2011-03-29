package com.robgolding.android.alarmclock;

import java.lang.Math;
import java.lang.Runnable;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
    public static final int UPDATE_UI_INTERVAL = 1000;

    class ClockView extends View
    {
        public ClockView(Context context)
        {
            super(context);
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
            int boxSize = Math.min(getWidth(), getHeight());
            int midX = (int)Math.round((double)getWidth()/2.0d);
            int midY = (int)Math.round((double)getHeight()/2.0d);

            Paint p = new Paint();
            p.setStyle(Paint.Style.FILL);
            canvas.drawColor(Color.BLUE);
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);

            long millis = System.currentTimeMillis();
            long seconds = millis / 1000;
            int secsInDay = (int) (seconds % 86400);
            int secsInHour = secsInDay % 3600;
            int hour = secsInDay / 3600;
            int minute = secsInHour / 60;
            int second = secsInHour % 60;

            double proportion = 80.0d;

            canvas.save();
            for (int i=0; i<12; i++)
            {
                if (i % 3 == 0)
                {
                    Rect r1 = new Rect(
                            (int) (midX-(double)boxSize*3/proportion),
                            (int) (midY-(double)boxSize/2.2d),
                            (int) (midX-(double)boxSize/proportion),
                            (int) (midY-(double)boxSize/3.2d)
                            );
                    Rect r2 = new Rect(
                            (int) (midX+(double)boxSize*3/proportion),
                            (int) (midY-(double)boxSize/2.2d),
                            (int) (midX+(double)boxSize/proportion),
                            (int) (midY-(double)boxSize/3.2d)
                            );
                    canvas.drawRect(r1, p);
                    canvas.drawRect(r2, p);
                }
                else
                {
                    Rect r = new Rect(
                            (int) (midX-(double)boxSize/proportion),
                            (int) (midY-(double)boxSize/2.2d),
                            (int) (midX+(double)boxSize/proportion),
                            (int) (midY-(double)boxSize/3.2d)
                            );
                    canvas.drawRect(r, p);
                }
                canvas.rotate(30, midX, midY);
            }
            canvas.restore();

            p.setStrokeWidth(8);
            canvas.save();
            canvas.rotate(30 * hour + minute / 2, midX, midY);
            canvas.drawLine(
                    midX, (int) (midY-(double)boxSize/2.5d),
                    midX, (int) (midY+(double)boxSize/7.0d),
                    p
            );
            canvas.restore();

            p.setStrokeWidth(4);
            canvas.save();
            canvas.rotate(6 * minute, midX, midY);
            canvas.drawLine(
                    midX, (int) (midY-(double)boxSize/2.2d),
                    midX, (int) (midY+(double)boxSize/7.0d),
                    p
            );
            canvas.restore();

            p.setStrokeWidth(2);
            canvas.save();
            canvas.rotate(6 * second, midX, midY);
            canvas.drawLine(
                    midX, (int) (midY-(double)boxSize/2.2d),
                    midX, (int) (midY+(double)boxSize/7.0d),
                    p
            );
            canvas.restore();

            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.BLUE);
            canvas.drawCircle(midX, midY, boxSize/15, p);
            p.setStyle(Paint.Style.STROKE);
            p.setColor(Color.WHITE);
            p.setStrokeWidth(2);
            canvas.drawCircle(midX, midY, boxSize/15, p);
        }
    }

    private View clockView;
    private Handler handler;

    private Runnable updateUITask = new Runnable() {
        public void run() {
            Log.i(TAG, "Updating UI");
            clockView.invalidate();
            handler.postDelayed(this, UPDATE_UI_INTERVAL);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        clockView = new ClockView(this);
        setContentView(clockView);
        handler = new Handler();
        handler.postDelayed(updateUITask, UPDATE_UI_INTERVAL);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        handler.removeCallbacks(updateUITask);
    }
}
