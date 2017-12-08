package com.example.jiangning.cassetete;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
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
import static com.example.jiangning.cassetete.GameView.threadisalive;
import static com.example.jiangning.cassetete.GameView.hitretour;
import static com.example.jiangning.cassetete.GameView.hit;

public class MainActivity extends Activity {
    private GameView  mGameView;
    private GameView1 mGameView1;
    private GameView2 mGameView2;
    private GameView3 mGameView3;

    private MediaPlayer mediaPlayer;
    public static boolean musicLoop = false;
    public static boolean playing   = false;
    private boolean play  = false;
    private boolean pause = false;

    private MyCount mc0;
    private MyCount mc1;
    private MyCount mc2;
    private MyCount mc3;
    public static long timer=0 ;

    //butto home
    private static final String TAG = "KeyDown";
    private Context context;

    //un nombre de aléatoire
    private Random random = new Random();
    private int valueRandom;

    //scores
    public static int hitTmp = 0;
    public int totalScore    = 2000;
    public static int bestScore =0;

    //continue
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);

        Button m    = (Button) findViewById(R.id.music);
        Button bs   = (Button) findViewById(R.id.score);
        Button ab   = (Button) findViewById(R.id.propose);
        Button b    = (Button) findViewById(R.id.b);
        Button b0   = (Button) findViewById(R.id.b0);
        Button b1   = (Button) findViewById(R.id.b1);
        Button b2   = (Button) findViewById(R.id.b2);
        Button b3   = (Button) findViewById(R.id.b3);
        Button exit = (Button) findViewById(R.id.exit);
        Button c    = (Button) findViewById(R.id.conti);


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

        //click button music
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMusic();

            }
        });

        //click button About
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAbout();
            }
        });
         //click button Continue
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (threadisalive) {
                    Log.i("threadGameView", "True");
                    hit = hitretour;
                    setContentView(R.layout.main_game0);
                }
            }
        });
        //click button best Best Score
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogBestScore();
            }
        });
        //click button Random Game
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valueRandom == 1){
                    setGame(1, R.layout.main_game1, mc1);
                }else if (valueRandom == 2){
                    setGame(2, R.layout.main_game2, mc2);
                }else if (valueRandom ==3){
                    setGame(3, R.layout.main_game3, mc3);
                }

            }
        });

        //click button game
       b0.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setGame(0, R.layout.main_game0, mc0);
            }
        });
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setGame(1, R.layout.main_game1, mc1);
            }
        });
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setGame(2, R.layout.main_game2, mc2);
            }
        });
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                setGame(3, R.layout.main_game3, mc3);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogExit();
            }
        });



    }

    //fonction permettant de choisir GameView et accèder
    protected void setGame(int level,int reslayoutID, MyCount myCount ){
        //evite la premier fois gagner pour timerWinStop egale true toujours et timer ne change pas
        timerStarts();
        //charge le fichier main_.xml comme vue de l'activité
        setContentView(reslayoutID);
        //trouve le gameview
        switch (level){
            case 0:
                // recuperation de la vue une voie cree à partir de son id
                mGameView = (GameView)findViewById(R.id.GameView);
                //rend visible la vue
                mGameView.setVisibility(View.VISIBLE);
                break ;
            case 1:
                mGameView1 = (GameView1)findViewById(R.id.GameView1);
                mGameView1.setVisibility(View.VISIBLE);
                break;
            case 2:
                mGameView2 = (GameView2)findViewById(R.id.GameView2);
                mGameView2.setVisibility(View.VISIBLE);
                break;
            case 3:
                mGameView3 = (GameView3)findViewById(R.id.GameView3);
                mGameView3.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        };
        //Start the countdown
        myCount.start();
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

   // fonction permettant de quitter le jeu
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Log.d(TAG, "onKeyDown KEYCODE_BACK");
                showDialogExit();
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

    //activer/déactiver the music
    protected void showDialogMusic(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choice");
        builder.setIcon(R.drawable.icon1);
        final CharSequence[] choiceList = {
               getResources().getString(R.string.Playing),
                getResources().getString(R.string.Pause)};

        builder.setSingleChoiceItems(choiceList,
                -1, // does not select anything
                new DialogInterface.OnClickListener() {
                   @Override
                    public void onClick(DialogInterface dialog, int index){
                       switch (index){
                           case 0:
                               play = true;
                               break;
                           case 1:
                               pause = true;
                               break;
                           default:
                               break;
                       }

                    }
        });
        builder.setPositiveButton("No",//gauche
                new DialogInterface.OnClickListener() { // gauche
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("Ok",
                new android.content.DialogInterface.OnClickListener() { //droite
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(play) {
                            playing = true;
                            dialog.dismiss();
                            mediaPlayer.start();
                            play = false;
                        }else if(pause){
                            playing = false;
                            dialog.dismiss();
                            mediaPlayer.pause();
                            pause = false;
                        }
                    }
                });
        //show the dialog
        builder.create().show();
    }

    //Dialog à propose
    protected void showDialogAbout() {
        //Creates an alert dialog that uses the default alert dialog theme
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("A propos de CASSE TETE");
        builder.setIcon(R.drawable.about);
        builder.setMessage("\t\t\t\t\t\tCASSE TETE 0.0.0\n\n"+
                "\t\tJiangning Lin, Ruddy Stephenson\n"+
                "\t\t\t\t\t\t\t\t 08.12.2017");
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismisses the dialog
                        dialog.dismiss();
                    }
                });
        //show the dialog
        builder.create().show();
    }

    //Dialog à propose
    protected void showDialogBestScore() {
        //Creates an alert dialog that uses the default alert dialog theme
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Best Scores");
        builder.setIcon(R.drawable.about);
        String str = "" + bestScore;
        builder.setMessage("\t\t\t\t\t\tBest Scores: "+str);
        builder.setPositiveButton("Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismisses the dialog
                        dialog.dismiss();
                    }
                });
        //show the dialog
        builder.create().show();
    }

    //fonction permettant de afficher de dialog et quitter le jeu
    protected void showDialogExit() {
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
    //compare les scores
    public int getBestScores(){
        int scoretmp = totalScore - hitTmp*20;
          if (scoretmp > bestScore && hitTmp != 0)
              bestScore = scoretmp;
        return bestScore;
    }

    //pour commencer les temps
    public void timerStarts(){
        timerStop  = false;
        timerStop1 = false;
        timerStop2 = false;
        timerStop3 = false;
    }

    @Override
    protected void onUserLeaveHint() {
        Log.d(TAG, "onUserLeaveHint ");
        super.onUserLeaveHint();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //sauvegarde de bestScore
        bestScore = getBestScores();
        super.onPause();
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        // methode permettant de activer le music quand touche le boutton de retour
        if (mediaPlayer != null && musicLoop) {
            mediaPlayer.start();
            musicLoop = false;
        }
        super.onResume();  // Always call the superclass method first
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
