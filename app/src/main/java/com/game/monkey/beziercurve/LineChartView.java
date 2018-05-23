package com.game.monkey.beziercurve;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class LineChartView extends View {

    int viewWidth;
    int viewHeight;
    Context mContext;
    public static final int paddingLeft = 160;
    public static final int paddingRight = 60;
    public static final int paddingTop = 400;
    public static final int paddingBottom = 500;

    int horizontalPointCount = 4;
    int verticalPointCount = 5;

    Paint framePaint;
    Paint trackPaint;
    Paint textPaint;
    Paint pecentPaint;
    Paint valuePointPaint;
    Paint valueTextPaint;

    PointF[] verticalPoint;
    PointF[] horizontalPoint;
    PointF[] valuePoint;

    float verticalInterval = 0.0f;
    float horizontalInterval = 0.0f;

    float[] value;
    float[] segent;

    Path linePath;
    PathMeasure pathMeasure;

    ValueAnimator lineDrawAnimator;

    float lineFraction = 0.0f;

    public LineChartView(Context context){
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attr) {
        super(context,attr);
        mContext = context;
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        initPaint();
        init();
        createAnimator();
    }

    public LineChartView(Context context, AttributeSet attributeSet, int verticalCount, int horizontalPointCount) {
        super(context,attributeSet);
        mContext = context;
        viewWidth = this.getWidth();
        viewHeight = this.getHeight();
        initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
        init();
        initLineData();
        lineDrawAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        viewWidth = widthMeasureSpec;
//        viewHeight = heightMeasureSpec;
//        init();
    }

    private void createAnimator(){
        lineDrawAnimator = ValueAnimator.ofFloat(0.0F, 1.0f);
        lineDrawAnimator.setRepeatCount(0);
        lineDrawAnimator.setDuration(1000);
        lineDrawAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lineFraction = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
    }

    private void init(){
        horizontalPoint = new PointF[horizontalPointCount + 2];
        verticalPoint = new PointF[verticalPointCount + 2];
        verticalInterval = (viewHeight - paddingBottom - paddingTop)/(verticalPointCount + 1);
        horizontalInterval = (viewWidth - paddingLeft - paddingRight)/(horizontalPointCount + 1);

        for (int i= 0;i<verticalPoint.length;i++) {
            verticalPoint[i] = new PointF(paddingLeft,
                    viewHeight - paddingBottom - verticalInterval*i);
            if (verticalPoint[i].y < paddingTop) {
                verticalPoint[i].y = paddingTop;
            }
        }
        for (int i= 0;i<horizontalPoint.length;i++) {
            horizontalPoint[i] = new PointF(paddingLeft + horizontalInterval*i,
                    viewHeight -paddingBottom);
            if (horizontalPoint[i].x >viewWidth - paddingRight){
                horizontalPoint[i].x = viewWidth - paddingRight;
            }
        }

        createAnimator();
    }

    private void initPaint(){
        framePaint = new Paint();
        trackPaint = new Paint();
        textPaint = new Paint();
        pecentPaint = new Paint();
        valuePointPaint = new Paint();
        valueTextPaint = new Paint();

        framePaint.setColor(Color.GRAY);
        framePaint.setStrokeWidth(8);
        framePaint.setStyle(Paint.Style.STROKE);
        framePaint.setAntiAlias(true);

        trackPaint.setColor(Color.BLUE);
        trackPaint.setStrokeWidth(8);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setAntiAlias(true);

        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(2);
        textPaint.setTextSize(60);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);

        pecentPaint.setColor(Color.YELLOW);
        pecentPaint.setStrokeWidth(16);
        pecentPaint.setStyle(Paint.Style.STROKE);
        pecentPaint.setAntiAlias(true);

        valuePointPaint.setColor(Color.BLUE);
        valuePointPaint.setStrokeWidth(30);
        valuePointPaint.setStyle(Paint.Style.STROKE);
        valuePointPaint.setStrokeCap(Paint.Cap.ROUND);
        valuePointPaint.setAntiAlias(true);

        valueTextPaint.setColor(Color.RED);
        valueTextPaint.setStrokeWidth(2);
        valueTextPaint.setTextSize(30);
        valueTextPaint.setStyle(Paint.Style.STROKE);
        valueTextPaint.setAntiAlias(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (verticalInterval == 0.0f || horizontalInterval == 0.0f) {
            return;
        }
        drawFrame(canvas);
        drawValueLine(canvas);
    }
    private void drawFrame(Canvas canvas){
        //绘制Y轴
        canvas.drawLine(verticalPoint[0].x,verticalPoint[0].y,
                verticalPoint[verticalPoint.length-1].x,
                verticalPoint[verticalPoint.length-1].y,
                framePaint);
        //绘制X轴
        canvas.drawLine(horizontalPoint[0].x,horizontalPoint[0].y,
                horizontalPoint[horizontalPoint.length-1].x,
                horizontalPoint[horizontalPoint.length-1].y,
                framePaint);

        //绘制Y轴比例值点以及数值
        for (int i = 1;i<verticalPoint.length-1;i++) {
            canvas.drawPoint(verticalPoint[i].x,verticalPoint[i].y,pecentPaint);
            String text = String.valueOf(500*i);
            Rect rect = new Rect();
            textPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,verticalPoint[i].x-rect.width()-5,verticalPoint[i].y - rect.height()/2,textPaint);
        }

        //绘制原点
        String textZoro = "0";
        Rect rectZoro = new Rect();
        textPaint.getTextBounds(textZoro,0,textZoro.length(),rectZoro);
        canvas.drawText(textZoro,verticalPoint[0].x-rectZoro.width()-5,verticalPoint[0].y - rectZoro.height()/2,textPaint);

        //绘制X轴比例值点以及数值
        for (int i = 1;i<horizontalPoint.length-1;i++) {
            canvas.drawPoint(horizontalPoint[i].x,horizontalPoint[i].y,pecentPaint);

            String text = String.valueOf(21+i)+"日";
            Rect rect = new Rect();
            textPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,horizontalPoint[i].x-rect.width()/2,horizontalPoint[i].y + rect.height()+10,textPaint);
        }

    }

    private void drawValueLine(Canvas canvas){
        if (null == pathMeasure|| 0.0f == lineFraction){
            return;
        }
        Path path = new Path();
        float currentLength = pathMeasure.getLength() * lineFraction;
        pathMeasure.getSegment(0,currentLength , path, true);
        canvas.drawPath(path,trackPaint);

        if (null == valuePoint||value.length<1){
            return;
        }
        int index = -1;
        for(int j = 0;j<segent.length;j++) {
            if (currentLength < segent[j]) {
                index = j;
                break;
            }
        }
        //真个轨迹的长度
        if (-1 == index) {
            index = 3;
        }
        for (int i = 0;i<valuePoint.length&&i<(index+1);i++) {
            canvas.drawPoint(valuePoint[i].x,valuePoint[i].y,valuePointPaint);
            String text = "" + (int)value[i];
            Rect rect  = new Rect();
            valueTextPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,valuePoint[i].x-rect.width()/2,valuePoint[i].y-rect.height(),valueTextPaint);
        }
    }

    public void setLineData(float[] data){
        if (null == data)
            return;
        this.value = data;

    }

    private void initLineData(){
        if (null == value||null == horizontalPoint) {
            return;
        }
        int[] verticalValue = new int[]{500, 1000, 1500, 2000, 2500,3000};
        linePath = new Path();
        pathMeasure = new PathMeasure();
        int length = value.length<(horizontalPoint.length-2)?value.length:(horizontalPoint.length-2);
        valuePoint = new PointF[length];
        segent = new float[length -1];
        for (int i=0;i<length;i++){
            float pecent = 0.0f;
            if (value[i]<0){
                pecent = 0.0f;
            } else if (value[i] > verticalValue[4]) {
                pecent = 5/6;
            }else {
                pecent = value[i] / 3000;
            }
            float valueY = viewHeight - paddingBottom-(viewHeight-paddingTop-paddingBottom)*pecent;
            Log.d("get_line_value", "valueY:" + valueY);
            if (0 == i){
                linePath.moveTo(horizontalPoint[i+1].x,valueY);
            }else{
                linePath.lineTo(horizontalPoint[i+1].x,valueY);
                pathMeasure.setPath(linePath,false);
                segent[i-1] = pathMeasure.getLength();
            }
            valuePoint[i] = new PointF(horizontalPoint[i + 1].x, valueY);
        }
    }
}
