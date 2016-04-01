package com.marni.android.scanfiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TENBIG = "tenBig";
    public static final String AVGFILESIZE = "avgFileSize";
    public static final String EXTFREQUENT = "extFrequent";
    public String tenBig;
    public String avgFileSize;
    public String extFrequent;

    private int vissible;

//Collections.reverseOrder()

    private Button startButton;
    private Button stopButton;
    private Button showResults;
    private ProgressBar mProgressBar;
    private Intent intent;


    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        startButton = (Button) findViewById(R.id.start_scan);
        stopButton = (Button) findViewById(R.id.stop_scan);
        showResults = (Button) findViewById(R.id.show_results);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(savedInstanceState != null) {
            vissible = savedInstanceState.getInt("vis");
            if(vissible == 0) {
                showResults.setVisibility(View.VISIBLE);
            } else {
                showResults.setVisibility(View.INVISIBLE);
            }
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        intent = new Intent(this, ScanIntentService.class);


        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Scan Started");
                mProgressBar.setVisibility(View.VISIBLE);
                startButton.setEnabled(false);
                mProgressBar.setEnabled(true);
                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startButton.setEnabled(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                startButton.setText("Start Scan");
                stopService(intent);
            }
        });

        showResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(getBaseContext(), Result.class);
                startActivity(intent2);
            }
        });
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        registerReceiver(receiver, new IntentFilter(ScanIntentService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       vissible = showResults.getVisibility();
        outState.putInt("vis",vissible);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startButton.setEnabled(true);
        startButton.setText("Start Scan");
        stopService(intent);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            startButton.setEnabled(true);
            if (bundle != null) {
                tenBig = bundle.getString(ScanIntentService.TOP10);
                extFrequent = bundle.getString(ScanIntentService.FREQUENTEXT);
                avgFileSize = bundle.getString(ScanIntentService.AVERAGE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(TENBIG,tenBig);
                editor.putString(EXTFREQUENT,extFrequent);
                editor.putString(AVGFILESIZE,avgFileSize);
                editor.commit();
                startButton.setText("Start Scan");
                showResults.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    };

}
