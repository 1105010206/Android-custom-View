package com.game.monkey.beziercurve;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class OneBezierView extends TextView{

    PointF startPoint,endPoint,currentPoint;
    Paint background,track;
    float pecent = (float) 0.0;
    Context mContext;
    Bitmap pecentBackground;
    int pictureWidth,pictureHeight;
    public OneBezierView(Context context) {
        this(context, null);
    }

    public OneBezierView(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
        int w = this.getWidth();
        int h = this.getHeight();

        startPoint = new PointF(0, 0);
        currentPoint = new PointF(0, 0);
        endPoint = new PointF(0, 0);
        endPoint.x = w-200;
        endPoint.y = h/2;
        startPoint.x = 0+200;
        startPoint.y = h/2;

        currentPoint = new PointF(0, 0);
        currentPoint.x = startPoint.x;
        currentPoint.y = startPoint.y;

        background = new Paint();
        track = new Paint();
        background.setColor(Color.RED);
        background.setStrokeWidth(40);
        background.setAntiAlias(true);

        track.setColor(Color.RED);
        track.setStrokeWidth(40);
        track.setAntiAlias(true);
        mContext = context;

        pecentBackground = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.progress_pecent_background);
        pictureWidth = pecentBackground.getWidth();
        pictureHeight = pecentBackground.getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        endPoint.x = w-200;
        endPoint.y = h/2;
        startPoint.x = 0+200;
        startPoint.y = h/2;

        currentPoint = new PointF(0, 0);
        currentPoint.x = startPoint.x;
        currentPoint.y = startPoint.y;

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("test_one_bezier", "startX: " + startPoint.x + "; startY" + startPoint.y +
                "\n endPointX: " + endPoint.x + "; endPointY: " + endPoint.y);
        background.setStrokeWidth(40);
        background.setColor(Color.GRAY);
//        canvas.drawPoint(startPoint.x,startPoint.y,background);
//        canvas.drawPoint(endPoint.x,endPoint.y,background);

        //绘制进度条背景
        background.setStrokeWidth(40);
        background.setStrokeCap(Paint.Cap.ROUND);
        track.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(startPoint.x,startPoint.y,endPoint.x,endPoint.y,background);
        //绘制进度条比例长度
        canvas.drawLine(startPoint.x,startPoint.y,currentPoint.x,currentPoint.y,track);

        int pcent = (int) ((currentPoint.x-startPoint.x)/(endPoint.x-startPoint.x) *100);
        if (pcent > 100) {
            pcent = 100;
        }


        //绘制进度条文字背景
//        Rect srcRect = new Rect((int)(currentPoint.x - pictureWidth / 2),
//                (int)(currentPoint.y - 15- pictureHeight),
//                (int)(currentPoint.x + pictureWidth / 2),
//                (int)(currentPoint.y - 15 ));
        Rect srcRect = new Rect(0,
                0,pictureWidth,
                pictureHeight);
        // 计算左边位置
        int left = (int) (currentPoint.x - pictureWidth / 2);
        // 计算上边位置
        int top = (int) (currentPoint.y - pictureHeight);
        Rect desRect = new Rect(left,top,left + pictureWidth,top+pictureHeight-5);
        canvas.drawBitmap(pecentBackground,srcRect,desRect,background);

        //绘制进度条文字
        background.setTextSize(40);
        String textPecent = pcent+"%";
        Rect textRect = new Rect();
        background.getTextBounds(textPecent,0,textPecent.length(),textRect);
        int textWidth = textRect.width();
        canvas.drawText(textPecent,
                left+(pictureWidth-textWidth)/2,
                top+pictureHeight/2+10,background);
        if (currentPoint.x < endPoint.x) {
            currentPoint.x = currentPoint.x+1;
            if (currentPoint.x > endPoint.x) {
                currentPoint.x = endPoint.x;
            }
            mHandler.sendEmptyMessage(1);
        }else {
            Toast.makeText(mContext,"绘制完成",Toast.LENGTH_SHORT).show();
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    OneBezierView.this.invalidate();
                    break;
            }
        }
    };

}
