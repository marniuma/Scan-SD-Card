package com.marni.android.scanfiles;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    private TextView top10;
    private TextView listTop10;
    private TextView average;
    private TextView averageFileSize;
    private TextView top5;
    private TextView top5Frequent;

    private String avg;
    private String top;
    private String freTop;

    private SharedPreferences sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreference = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE);

        top10 = (TextView) findViewById(R.id.top10);
        listTop10 = (TextView) findViewById(R.id.list_top_10);
        average = (TextView) findViewById(R.id.average);
        averageFileSize = (TextView) findViewById(R.id.average_file);
        top5 = (TextView) findViewById(R.id.top5);
        top5Frequent = (TextView) findViewById(R.id.list_top_5);

        if(sharedPreference.contains(MainActivity.AVGFILESIZE)) {
            avg = sharedPreference.getString(MainActivity.AVGFILESIZE, "");
        }
        if(sharedPreference.contains(MainActivity.TENBIG)) {
            top = sharedPreference.getString(MainActivity.TENBIG, "");
        }
        if(sharedPreference.contains(MainActivity.EXTFREQUENT)) {
            freTop = sharedPreference.getString(MainActivity.EXTFREQUENT, "");
        }


//        Bundle extras = getIntent().getExtras();
//        if(extras !=null)
//        {
//            avg = extras.getString(MainActivity.AVGFILESIZE);
//            freTop = extras.getString(MainActivity.EXTFREQUENT);
//            top = extras.getString(MainActivity.TENBIG);
//        }

        listTop10.setText(top);
        averageFileSize.setText(avg);
        top5Frequent.setText(freTop);
    }

}
