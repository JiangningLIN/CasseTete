package com.example.jiangning.cassetete;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import java.util.Random;

import static com.example.jiangning.cassetete.GameView.timerStop;
import static com.example.jiangning.cassetete.GameView1.timerStop1;
import static com.example.jiangning.cassetete.GameView2.timerStop2;
import static com.example.jiangning.cassetete.GameView3.timerStop3;


public class MainActivity extends Activity {
    private GameView mGameView;
    private GameView1 mGameView1;
    private GameView2 mGameView2;
    private GameView3 mGameView3;

    private MyCount mc0;
    public  MyCount mc1;
    private MyCount mc2;
    private MyCount mc3;
    public static long timer=0 ;

    //butto home
    private static final String TAG = "KeyDown";
    private Context context;

    //un nombre de aléatoire
    Random random = new Random();
    int valueRandom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        Button b    = (Button) findViewById(R.id.b);
        Button b0   = (Button) findViewById(R.id.b0);
        Button b1   = (Button) findViewById(R.id.b1);
        Button b2   = (Button) findViewById(R.id.b2);
        Button b3   = (Button) findViewById(R.id.b3);
        Button exit = (Button) findViewById(R.id.exit);

        mc0 = new MyCount(10000, 1000);//10 second
        mc1 = new MyCount(12000, 1000);//12 second
        mc2 = new MyCount(40000, 1000);//40 second
        mc3 = new MyCount(50000, 1000);//50 second


        valueRandom = random.nextInt(3)+1;//pour obtenir un nombre entre 1 et 3

        //click button home
        context             = MainActivity.this;
        IntentFilter filter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //Register a BroadcastReceiver to be run in the main activity thread
        registerReceiver(receiver,filter);

        //click button Random Game
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valueRandom == 1){
                    //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                    timerStop  = false;
                    timerStop1 = false;
                    timerStop2 = false;
                    timerStop3 = false;
                    //charge le fichier main.xml comme vue de l'activité
                    setContentView(R.layout.main_game1);
                    // recuperation de la vue une voie cree à partir de son id
                    mGameView1 = (GameView1)findViewById(R.id.GameView1);
                    //rend visible la vue
                    mGameView1.setVisibility(View.VISIBLE);
                    //Start the countdown
                    mc1.start();
                }else if (valueRandom == 2){
                    //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                    timerStop  = false;
                    timerStop1 = false;
                    timerStop2 = false;
                    timerStop3 = false;
                    //charge le fichier main.xml comme vue de l'activité
                    setContentView(R.layout.main_game2);
                    // recuperation de la vue une voie cree à partir de son id
                    mGameView2 = (GameView2)findViewById(R.id.GameView2);
                    //rend visible la vue
                    mGameView2.setVisibility(View.VISIBLE);
                    //Start the countdown
                    mc2.start();
                }else if (valueRandom ==3){
                    //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                    timerStop  = false;
                    timerStop1 = false;
                    timerStop2 = false;
                    timerStop3 = false;
                    //charge le fichier main.xml comme vue de l'activité
                    setContentView(R.layout.main_game3);
                    // recuperation de la vue une voie cree à partir de son id
                    mGameView3 = (GameView3)findViewById(R.id.GameView3);
                    //rend visible la vue
                    mGameView3.setVisibility(View.VISIBLE);
                    //Start the countdown
                    mc3.start();
                }
            }
        });

        //click button game
       b0.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                timerStop  = false;
                timerStop1 = false;
                timerStop2 = false;
                timerStop3 = false;
                //charge le fichier main.xml comme vue de l'activité
                setContentView(R.layout.main_game0);
                // recuperation de la vue une voie cree à partir de son id
                mGameView = (GameView)findViewById(R.id.GameView);
                //rend visible la vue
                mGameView.setVisibility(View.VISIBLE);
                //Start the countdown
                mc0.start();
            }
        });
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                timerStop  = false;
                timerStop1 = false;
                timerStop2 = false;
                timerStop3 = false;
                //charge le fichier main.xml comme vue de l'activité
                setContentView(R.layout.main_game1);
                // recuperation de la vue une voie cree à partir de son id
                mGameView1 = (GameView1)findViewById(R.id.GameView1);
                //rend visible la vue
                mGameView1.setVisibility(View.VISIBLE);
                //Start the countdown
                mc1.start();
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
                timerStop  = false;
                timerStop1 = false;
                timerStop2 = false;
                timerStop3 = false;
                //charge le fichier main.xml comme vue de l'activité
                setContentView(R.layout.main_game2);
                // recuperation de la vue une voie cree à partir de son id
                mGameView2 = (GameView2)findViewById(R.id.GameView2);
                //rend visible la vue
                mGameView2.setVisibility(View.VISIBLE);
                //Start the countdown
                mc2.start();
            }
        });
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
               // timerWinStop3 = false;
                timerStop  = false;
                timerStop1 = false;
                timerStop2 = false;
                timerStop3 = false;
                //charge le fichier main.xml comme vue de l'activité
                setContentView(R.layout.main_game3);
                // recuperation de la vue une voie cree à partir de son id
                mGameView3 = (GameView3)findViewById(R.id.GameView3);
                //rend visible la vue
                mGameView3.setVisibility(View.VISIBLE);
                //Start the countdown
                mc3.start();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });



    }

    /*CountDownTimer*/
    class MyCount extends CountDownTimer {


        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        /*Callback fired on regular interval.*/
        @Override
        public void onTick(long millisUntilFinished) {
            if(timerStop || timerStop1  || timerStop2  || timerStop3 ){
                cancel(); /*Cancel the countdown*/
            }

            timer = millisUntilFinished / 1000;




        }
         /*Callback fired when the time is up.*/
        @Override
        public void onFinish() {
            timer = 0;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                showDialog();
                break;
            case KeyEvent.KEYCODE_HOME:
                Log.d(TAG, "onKeyDown KEYCODE_HOME");
                break;
            case KeyEvent.KEYCODE_MENU:
                Log.d(TAG, "onKeyDown KEYCODE_MENU");
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    protected void showDialog() {
        //Creates an alert dialog that uses the default alert dialog theme
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("Are you sure you want to quit?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismisses the dialog
                        dialog.dismiss();
                        //Destroy the current activity
                        finish();
                    }
                });
        builder.setNegativeButton("No",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //show the dialog
        builder.create().show();
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d(TAG, "onUserLeaveHint ");
        super.onUserLeaveHint();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause ");
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState ");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy ");
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /*
     *HomeKey monitor event broadcast receiver
     */
    BroadcastReceiver receiver = new BroadcastReceiver(){
        /*This method is called when the BroadcastReceiver is receiving an Intent broadcast.*/
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "HomeKeyEventBroadcastReceiver.onReceive ");
        }
    };

}
