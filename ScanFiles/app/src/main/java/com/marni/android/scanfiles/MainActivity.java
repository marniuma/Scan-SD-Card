package com.marni.android.scanfiles;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String TENBIG = "tenBig";
    public static final String AVGFILESIZE = "avgFileSize";
    public static final String EXTFREQUENT = "extFrequent";
    public String tenBig;
    public String avgFileSize;
    public String extFrequent;

    //private NotificationCompat.Builder mCompat;

    private int vissible = 0;

//Collections.reverseOrder()

    private Button startButton;
    private Button stopButton;
    private Button showResults;
    private Button share;
    private ProgressBar mProgressBar;
    private Intent intent, nIntent, mShareIntent;
    private PendingIntent pIntent;
    private Notification noti;
    private NotificationManager nm;
    private ShareActionProvider mShareActionProvider;


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
        share = (Button) findViewById(R.id.share);

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

        /* Notification
        //mCompat = new NotificationCompat.Builder(this);
*/




        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setText("Scan Started");
                mProgressBar.setVisibility(View.VISIBLE);
                startButton.setEnabled(false);
                mProgressBar.setEnabled(true);
                stopButton.setVisibility(View.VISIBLE);
                nIntent = new Intent();
                pIntent = PendingIntent.getActivity(MainActivity.this, 0, nIntent, 0);
               noti = new Notification.Builder(MainActivity.this)
                        .setTicker("Title")
                        .setContentTitle("C Title")
                        .setContentText("Scan Started")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent).getNotification();

//                noti.flags = Notification.FLAG_AUTO_CANCEL;
                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,noti);

                startService(intent);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startButton.setEnabled(true);
                mProgressBar.setVisibility(View.INVISIBLE);
                startButton.setText("Start Scan");

                nIntent = new Intent();
                pIntent = PendingIntent.getActivity(MainActivity.this, 0, nIntent, 0);
                noti = new Notification.Builder(MainActivity.this)
                        .setTicker("Title")
                        .setContentTitle("SD CARD Scan")
                        .setContentText("Scan Stoped frocefully")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent).getNotification();

                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,noti);

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


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share();
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
       // getMenuInflater().inflate(R.menu.menu_main, menu);



        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       // MenuItem shareItem

//        if (vissible == 1){
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            MenuItem shareItem = menu.findItem(R.id.action_share).setVisible(true);
//            mShareActionProvider =
//                    (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//            mShareIntent = new Intent(Intent.ACTION_SEND);
//            mShareIntent.setAction(Intent.ACTION_SEND);
//            mShareIntent.setType("text/plain");
//            mShareIntent.putExtra(Intent.EXTRA_TEXT, "From me to you, this text is new.");
//
//            // Connect the dots: give the ShareActionProvider its Share Intent
//
//            mShareActionProvider.setShareIntent(mShareIntent);
//
//
//        } else if (vissible == 0) {
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            MenuItem shareItem = menu.findItem(R.id.action_share).setVisible(false);
//            mShareActionProvider =
//                    (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
//            mShareIntent = new Intent(Intent.ACTION_SEND);
//            mShareIntent.setAction(Intent.ACTION_SEND);
//            mShareIntent.setType("text/plain");
//            mShareIntent.putExtra(Intent.EXTRA_TEXT, "From me to you, this text is new.");
//
//            // Connect the dots: give the ShareActionProvider its Share Intent
//
//            mShareActionProvider.setShareIntent(mShareIntent);
//
//        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {


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
                vissible = 1;
                startButton.setText("Start Scan");
                stopButton.setVisibility(View.INVISIBLE);
                showResults.setVisibility(View.VISIBLE);
                share.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                nIntent = new Intent();
                pIntent = PendingIntent.getActivity(MainActivity.this, 0, nIntent, 0);
                noti = new Notification.Builder(MainActivity.this)
                        .setTicker("Title")
                        .setContentTitle("SD CARD Scan")
                        .setContentText("Scan Ended")
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent).getNotification();

                nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0,noti);
            }
        }
    };


    private void share() {

        mShareIntent = new Intent(Intent.ACTION_SEND);
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("text/plain");
        String share = "Top Ten file sizes with Name: \n" +tenBig +"\n Average File Size: \n" + avgFileSize
                + "\n Top five extensions with Frequency: \n" + extFrequent;
        mShareIntent.putExtra(Intent.EXTRA_TEXT,  share);
        startActivity(Intent.createChooser(mShareIntent, "Share..."));
        // Connect the dots: give the ShareActionProvider its Share Intent

        //mShareActionProvider.setShareIntent(mShareIntent);

    }


}
