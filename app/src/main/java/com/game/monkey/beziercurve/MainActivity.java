package com.game.monkey.beziercurve;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    List<Map<String, String>> data;
    String[] allKind;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = new ArrayList<>();
        allKind = getResources().getStringArray(R.array.all_kind);
        if (null != allKind&&allKind.length>0) {

            for (int i = 0;i<allKind.length;i++) {
                Map<String ,String> temp = new HashMap<>();
                temp.put("kind", allKind[i]);
                data.add(i,temp);
            }
        }
        ListView listView = (ListView) findViewById(R.id.display);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_1,
                new String[]{"kind"},
                new int[]{android.R.id.text1});
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent bezierCurve = new Intent(MainActivity.this, BezierCurveActivity.class);
        int flag = 1;
        switch (i) {
            case 0:
                flag = 1;
                break;
            case 1:
                flag = 2;
                break;
            case 2:
                flag = 3;
                break;
            case 3:
                flag = 4;
                break;
            case 4:
                flag = 5;
                break;
            case 5:
                flag = 6;
                break;
            case 6:
                flag = 7;
                break;
            case 7:
                flag = 8;
                break;
        }
        bezierCurve.putExtra("flag", flag);
        startActivity(bezierCurve);
    }
}
