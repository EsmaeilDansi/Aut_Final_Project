package com.example.aut_final_project;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class san extends Activity {
    ValueLineChart v1,v2;
    int cover [] ;
    int stego [];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.san);
        Bundle extras = getIntent().getExtras();
        cover= extras.getIntArray("cover");
        stego=extras.getIntArray("stego");
        v1=(ValueLineChart)findViewById(R.id.v_line1);
        v2=(ValueLineChart)findViewById(R.id.v_line2);
        loaddata1();
        loaddata2();
        v1.startAnimation();
        v2.startAnimation();

    }
    public void loaddata1 ()
    {
        ValueLineSeries s=new ValueLineSeries();
        s.setColor(Color.parseColor("#ffff0000"));
        for(int i=0;i<cover.length;i++)
        {
            s.addPoint(new ValueLinePoint(cover[i]));
        }
        v1.addSeries(s);



    }
    public void loaddata2 ()
    {
        ValueLineSeries s=new ValueLineSeries();
        s.setColor(Color.parseColor("#FF56B7F1"));
        for(int i=0;i<stego.length;i++)
        {
            s.addPoint(new ValueLinePoint(stego[i]));
        }
        v2.addSeries(s);

    }
}
