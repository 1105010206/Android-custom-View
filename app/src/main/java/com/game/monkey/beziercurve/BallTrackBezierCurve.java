package com.game.monkey.beziercurve;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by Administrator on 2017/9/20 0020.
 */

public class BallTrackBezierCurve extends View {

    int width, height;
    private final int PADDING = 60;

    PathMeasure pathMeasure;

    int circleRaduim = 40;

    private final int HEIGHT_difference = 60;

    private ValueAnimator[] valueAnimator;

    int[] mDataX = new int[5];
    int[] mControlX = new int[4];
    int[] mControlY = new int[4];
    private float[] pos = new float[2]; //存储某点的坐标值
    private float[] tan = new float[2]; //存储某点正切值

    int ballcount = 25;
    int[] colors = new int[]{Color.CYAN, Color.BLUE, Color.YELLOW, Color.RED};
    int[] radiums = new int[]{20, 15, 10, 8, 20};
    float[] segentFraction;
    int[] ballColors;
    int[] ballRadiums;
    int dataY;
    float[] segent = new float[4];


    Paint mPaint = new Paint();
    Paint pathPaint;
    Path currentPath = new Path();
    BallTrackInterpolator interpolatorCreater;
    Interpolator interpolator;

    public BallTrackBezierCurve(Context context) {
        this(context, null);
    }

    int[] radomRadium = new int[5];

    public BallTrackBezierCurve(Context context, AttributeSet attr) {
        super(context, attr);
        width = getWidth();
        height = getHeight();
        initTools();
        initPaint();

        pathMeasure = new PathMeasure();
        init();
        initPath();
        //animotion();
    }

    private void initTools() {
        segentFraction = new float[ballcount];
        valueAnimator = new ValueAnimator[ballcount];
        ballColors = new int[ballcount];
        ballRadiums = new int[ballcount];
        Random random = new Random();
        for (int i = 0; i < ballcount; i++) {
            int index = random.nextInt(4);//[0,4);
            ballColors[i] = colors[index];
            ballRadiums[i] = radiums[index];
        }
    }

    private void initPaint() {

        mPaint = new Paint();
        mPaint.setStrokeWidth(8);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        pathPaint = new Paint();
        pathPaint.setStrokeWidth(20);
        pathPaint.setColor(Color.GRAY);
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        init();
        initPath();
    }

    private void init() {
        int widthInterval = (width - 2 * PADDING) / 4;
        int heithInterval = (height / 2 - PADDING) / 4;

        //数据点
        mDataX[0] = PADDING;
        mDataX[1] = mDataX[0] + 1 * widthInterval;
        mDataX[2] = mDataX[0] + 2 * widthInterval;
        mDataX[3] = mDataX[0] + 3 * widthInterval;
        mDataX[4] = mDataX[0] + 4 * widthInterval;
        dataY = height / 2;

        //控制点
        mControlY[0] = PADDING;
        mControlY[1] = mControlY[0] + 1 * heithInterval;
        mControlY[2] = mControlY[0] + 2 * heithInterval;
        mControlY[3] = mControlY[0] + 3 * heithInterval;

        mControlX[0] = mDataX[0] + widthInterval / 2;
        mControlX[1] = mControlX[0] + 1 * widthInterval;
        mControlX[2] = mControlX[0] + 2 * widthInterval;
        mControlX[3] = mControlX[0] + 3 * widthInterval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBall(canvas);
        //canvas.drawCircle(pos[0],pos[1],circleRaduim,mPaint);
        //currentPath.
        // canvas.drawPath(currentPath,pathPaint);

    }

    private void drawBall(Canvas canvas) {
        for (int i = 0; i < segentFraction.length; i++) {
            if (null == pathMeasure)
                return;
            if (segentFraction[i] <(segent[0]*0.45)/pathMeasure.getLength()) {
                continue;//轨迹开始的那个波峰从最高点开始显示
            }

            float fraction = segentFraction[i];
            float currentLength = pathMeasure.getLength() * fraction;
            //setBallColor(currentLength);
            pathMeasure.getPosTan(currentLength, pos, tan);
            mPaint.setColor(ballColors[i]);
            canvas.drawCircle(pos[0], pos[1], ballRadiums[i], mPaint);
            //currentPath = new Path();
            //pathMeasure.getSegment(0,currentLength,currentPath,true);
        }
    }

    private void initPath() {
        Path path = new Path();
        path.moveTo(mDataX[0], dataY);
        path.quadTo(mControlX[0], mControlY[0], mDataX[1], dataY);
        pathMeasure.setPath(path, false);
        segent[0] = pathMeasure.getLength();

        path.quadTo(mControlX[1], mControlY[1], mDataX[2], dataY);
        pathMeasure.setPath(path, false);
        segent[1] = pathMeasure.getLength();

        path.quadTo(mControlX[2], mControlY[2], mDataX[3], dataY);
        pathMeasure.setPath(path, false);
        segent[2] = pathMeasure.getLength();

        path.quadTo(mControlX[3], mControlY[3], mDataX[4], dataY + PADDING * 2);
        pathMeasure.setPath(path, false);
        segent[3] = pathMeasure.getLength();

        if (null == interpolator) {
            interpolatorCreater = new BallTrackInterpolator();
        }
        interpolator = interpolatorCreater.createInterpolator(segent);
    }

    private void createAnimotion() {
        for (int i = 0; i < valueAnimator.length; i++) {
            createTranslateAnim(i, 3300, 200 * i);
        }
    }

    private void createTranslateAnim(final int index, int duration, int delay) {
        if (null == valueAnimator[index]) {
            valueAnimator[index] = ValueAnimator.ofFloat(0.0F, 1.0f);
            valueAnimator[index].setDuration(duration);
            valueAnimator[index].setRepeatCount(-1);
            valueAnimator[index].setStartDelay(delay);
            valueAnimator[index].setInterpolator(interpolator);
            valueAnimator[index].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float fraction = valueAnimator.getAnimatedFraction();
                    segentFraction[index] = fraction;
//                    if (null == pathMeasure)
//                        return;
//                    float currentLength = pathMeasure.getLength()*fraction;
//                    setBallColor(currentLength);
//                    pathMeasure.getPosTan(currentLength, pos, tan);
//                    currentPath = new Path();
//                    pathMeasure.getSegment(0,currentLength,currentPath,true);
                    invalidate();
                }
            });
        }
    }

    //    private Paint randomBallPaint(){
//       // return mPaint[(new Random().nextInt(4))];
//    }
//
//    private int randomRadium(){
//        //return randomRadiums[(new Random().nextInt(4))];
//    }
    private void setBallColor(float currentLenth) {
//        if (currentLenth <= segent[0]) {
//            mPaint.setColor(Color.GRAY);
//            circleRaduim = randomRadiums[0];
//        }else if(currentLenth <= segent[1]){
//            mPaint.setColor(Color.BLUE);
//            circleRaduim = randomRadiums[1];
//        }else if(currentLenth <= segent[2]){
//            mPaint.setColor(Color.YELLOW);
//            circleRaduim = randomRadiums[2];
//        }else if(currentLenth <= segent[3]){
//            mPaint.setColor(Color.RED);
//            circleRaduim = randomRadiums[3];
//        }
    }

    private int randomCircleRadium() {
        Random random = new Random();
        int radomRadium = 20 + random.nextInt(30);
        return radomRadium;
    }

    public void startAnimotion() {
        post(new Runnable() {  //放入队列（保证view已加载完成）
            @Override
            public void run() {
                createAnimotion(); //20170810备注：检查内部有没有重复创建实例
                //valueAnimator.start();
                for (int i = 0; i < valueAnimator.length; i++) {
                    valueAnimator[i].start();
                }
            }
        });
    }


}
