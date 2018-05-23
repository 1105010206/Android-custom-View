package com.game.monkey.beziercurve;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/9/26 0026.
 */

public class RingProgressView extends View{


    int viewWidth;
    int viewHeight;

    PointF circleCenter;
    float radiusBigCircle = 200;
    float radiusSmallCircle = 180;

    Paint circlePaint;
    Paint ringPaint;
    Paint percentTextPaint;
    Paint progressPaint;
    Paint progressSwalfPaint;
    ValueAnimator valueAnimator;
    float fraction = 0.0f;

    boolean isStop = false;
    int flag = 1;
    public RingProgressView(Context context) {
        this(context,null);
    }

    public RingProgressView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
        init();
        thread.start();
        creatAnimation();
        valueAnimator.start();
    }

    private void init(){
        circlePaint = new Paint();
        circlePaint.setStrokeWidth(8);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);

        ringPaint = new Paint();
        ringPaint.setStrokeWidth(12);
        ringPaint.setColor(Color.RED);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setAntiAlias(true);

        percentTextPaint = new Paint();
        percentTextPaint.setStrokeWidth(2);
        percentTextPaint.setTextSize(60);
        percentTextPaint.setColor(Color.BLUE);
        percentTextPaint.setStyle(Paint.Style.FILL);
        percentTextPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setStrokeWidth(2);
        progressPaint.setColor(Color.GREEN);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setAntiAlias(true);

        progressSwalfPaint = new Paint();
        progressSwalfPaint.setStrokeWidth(2);
        progressSwalfPaint.setColor(Color.GREEN);
        progressSwalfPaint.setStyle(Paint.Style.STROKE);
//        progressSwalfPaint.set
        progressSwalfPaint.setAntiAlias(true);
        //progressSwalfPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        circleCenter = new PointF(viewWidth/2,viewHeight/2);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(circleCenter.x,circleCenter.y);
        //canvas.scale(1,-1);
        //圆环进度条
        canvas.drawCircle(0,0-500,radiusBigCircle,circlePaint);
        canvas.drawCircle(0,0-500,radiusSmallCircle,circlePaint);
        String text = ((int)(fraction*100))+"%";
        Rect textRect = new Rect();
        percentTextPaint.getTextBounds(text,0,text.length(),textRect);
        canvas.drawText(text,0-textRect.width()/2,0-500,percentTextPaint);
        float arcRadius = (radiusBigCircle+radiusSmallCircle)/2;
        RectF ovalRect = new RectF(0 - arcRadius,
                0-arcRadius-500,
                0 + arcRadius,
                0+arcRadius-500);
        float endAngle = 360*fraction;
        canvas.drawArc(ovalRect,270,endAngle,false,ringPaint);

        //球形垂直上升进度条
        canvas.drawCircle(0,0,radiusBigCircle,circlePaint);
        RectF ovalRect2 = new RectF(- arcRadius,
                -arcRadius,
                arcRadius,
                arcRadius);
        float angleWidth = 360*fraction;
        float startAngle = 90-angleWidth/2;
        canvas.drawArc(ovalRect2,startAngle,angleWidth,false,progressPaint);
        canvas.drawText(text,0-textRect.width()/2,0,percentTextPaint);

        //球形波浪形上升进度条
        canvas.drawCircle(0,500,radiusBigCircle,circlePaint);
        RectF ovalRect3 = new RectF( - arcRadius,
                500 - arcRadius,
                arcRadius,
                500 + arcRadius);
        float angleWidth2 = 360*fraction;
        float startAngle2 = 90-angleWidth2/2;
        Path path = new Path();
        PathMeasure pathMeasure = new PathMeasure();
        path.addArc(ovalRect3,startAngle2,angleWidth2);
//        canvas.drawPath(path,progressPaint);
        pathMeasure.setPath(path,false);

        float[] startPoint = new float[2];
        float[] tans = new float[2];
        float[] endPoint = new float[2];
        float[] control1 = new float[2];
        float[] control2 = new float[2];

        pathMeasure.getPosTan(0,startPoint,tans);
        pathMeasure.getPosTan(pathMeasure.getLength(),endPoint,tans);

        float interval = (startPoint[0] -endPoint[0])/3;
        control1[0] = startPoint[0] - interval;
        control1[1] = startPoint[1] - interval*flag;

        control2[0] = endPoint[0] + interval;
        control2[1] = endPoint[1] + interval*flag;
        //canvas.drawArc(ovalRect3,startAngle2,angleWidth2,false,progressPaint);
        Path cubed = new Path();
        cubed.moveTo(startPoint[0],startPoint[1]);
        cubed.cubicTo(control1[0],control1[1],control2[0],control2[1],endPoint[0],endPoint[1]);
        cubed.addArc(ovalRect3,startAngle2,angleWidth2);
        canvas.drawPath(cubed,progressSwalfPaint);

        canvas.drawText(text,0-textRect.width()/2,500,percentTextPaint);
        canvas.restore();


    }

    private void creatAnimation(){
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(10000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                fraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
    }

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                flag = -flag;
                if (!isStop) {
                    thread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    });
}
