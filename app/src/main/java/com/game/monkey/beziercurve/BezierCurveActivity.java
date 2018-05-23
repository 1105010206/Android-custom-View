package com.game.monkey.beziercurve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class BezierCurveActivity extends AppCompatActivity {

    int flag = 1;
    LinearLayout contentView;
    TextView noCustomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier_curve);
        contentView = (LinearLayout)findViewById(R.id.bezier_view);
        noCustomView = (TextView)findViewById(R.id.no_custom_view) ;
        initData();
    }

    private void initData(){
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag", 1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        switch (flag) {
            case 1:
                Toast.makeText(this,"一阶赛贝曲线",Toast.LENGTH_SHORT).show();
                OneBezierView oneBezierView = new OneBezierView(this);
                contentView.addView(oneBezierView,params);
                break;
            case 2:
                Toast.makeText(this,"二阶赛贝曲线",Toast.LENGTH_SHORT).show();
                BezierCurveView TwoBezier = new BezierCurveView(this);
                contentView.addView(TwoBezier,params);
                break;
            case 3:
                Toast.makeText(this,"三阶赛贝曲线",Toast.LENGTH_SHORT).show();
                ThreeBezierCurveView ThreeBezier = new ThreeBezierCurveView(this);
                contentView.addView(ThreeBezier,params);
                break;
            case 4:
                Toast.makeText(this,"圆形赛贝曲线",Toast.LENGTH_SHORT).show();
                LoveCircleBezierView circleBezierView = new LoveCircleBezierView(this);
                contentView.addView(circleBezierView,params);
                break;
            case 5:
                Toast.makeText(this,"球形轨迹赛贝曲线",Toast.LENGTH_SHORT).show();
                BallTrackBezierCurve ballTrackBezierCurve = new BallTrackBezierCurve(this);
                contentView.addView(ballTrackBezierCurve,params);
                ballTrackBezierCurve.startAnimotion();
                break;
            case 6:
                Toast.makeText(this,"自定义折线图",Toast.LENGTH_SHORT).show();
                LineChartView lineChartView = new LineChartView(this);
                contentView.addView(lineChartView,params);
                lineChartView.setLineData(new float[]{200,680,1000,1900});
                break;
            case 7:
                Toast.makeText(this,"自定义直方图",Toast.LENGTH_SHORT).show();
                HistogramView histogramView = new HistogramView(this);
                contentView.addView(histogramView,params);
                histogramView.setLineData(new float[]{200,680,1000,1900});
                break;
            case 8:
                Toast.makeText(this,"自定义圆环进度条",Toast.LENGTH_SHORT).show();
                RingProgressView ringProgressView = new RingProgressView(this);
                contentView.addView(ringProgressView,params);
//                histogramView.setLineData(new float[]{200,680,1000,1900});
                break;
            default:
                noCustomView.setVisibility(View.VISIBLE);
                break;
        }
    }
//     path.moveTo(startPoint[0],startPoint[1]);
//        path.rQuadTo(-interval,-interval,-2*interval,0);
//        path.rQuadTo(-interval,interval,-2*interval,0);
}
