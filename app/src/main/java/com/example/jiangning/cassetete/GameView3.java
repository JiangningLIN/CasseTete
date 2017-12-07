package com.example.jiangning.cassetete;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

import static com.example.jiangning.cassetete.MainActivity.timer;
import static com.example.jiangning.cassetete.MainActivity.musicLoop;
import static com.example.jiangning.cassetete.MainActivity.playing;
import static com.example.jiangning.cassetete.MainActivity.hitTmp;

/**
 * Created by Jiangning on 20/11/2017.
 */

public class GameView3 extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    // Declaration des images
    private Bitmap videClair;
    private Bitmap vide;
    private Bitmap block;
    private Bitmap block1;
    private Bitmap block2;
    private Bitmap block3;
    private Bitmap block4;
    private Bitmap block5;
    private Bitmap block6;
    private Bitmap block7;
    private Bitmap win;
    private Bitmap gameover;
    private Bitmap back;


    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources mRes;
    private Context mContext;

    // tableau modelisant la carte du jeu
    int[][] carte;


    // ancres pour pouvoir centrer la carte du jeu
    int carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int carteLeftAnchor;                  // coordonnées en X du point d'ancrage de notre carte

    // taille de la carte
    static final int carteWidth = 13;
    static final int carteHeight = 13;
    static final int carteTileSize = 62; //60ok

    // constante modelisant les differentes types de cases
    static final int CST_block       = 0;
    static final int CST_vide_clair  = 1;
    static final int CST_vide        = 2;
    static final int CST_block1      = 3;
    static final int CST_block2      = 4;
    static final int CST_block3      = 5;
    static final int CST_block4      = 6;
    static final int CST_block5      = 7;
    static final int CST_block6      = 8;
    static final int CST_block7      = 9;

    //les éléments pour le method onThouch
    int xDown;
    int yDown;
    int xUp;
    int yUp;
    int xMove, yMove;
    boolean initialDown = false;
    int prexMove, preyMove;



    //tableau de reference du terrain
    int[][] ref = { //13*13
            {CST_block, CST_block, CST_block, CST_block, CST_block,      CST_block,      CST_block,      CST_block,       CST_block, CST_block,CST_block, CST_block, CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide,       CST_vide,       CST_vide,        CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_vide,  CST_vide,  CST_vide,  CST_vide,       CST_vide_clair, CST_vide_clair, CST_vide_clair,  CST_vide,  CST_vide, CST_vide,  CST_vide,  CST_block},
            {CST_block, CST_block, CST_block, CST_block, CST_block,      CST_block,      CST_block,      CST_block,       CST_block, CST_block,CST_block, CST_block, CST_block},


    };

    //position de reference des block1s
    int[][] refblock_1s = {
            {1, 1},
            {2, 1},
            {3, 1},
            {1, 2},
            {2, 2}
    };
    //position de reference des block2s
    int[][] refblock_2s = {
            {1, 4},
            {1, 5},
            {1, 6}

    };
    //position de reference des block3s
    int[][] refblock_3s = {
            {2, 7},
            {3, 7},
            {2, 8},
            {3, 8}
    };

    //position de reference des block4s
    int[][] refblock_4s = {
            {1, 9},
            {1, 10},
            {1, 11},
            {2, 11}
    };

    //position de reference des block5s
    int[][] refblock_5s = {
            {10, 1},
            {9, 2},
            {10, 2},
            {10, 3}
    };
    //position de reference des block6s
    int[][] refblock_6s = {
            {9, 5},
            {9, 6},
            {10, 6},
            {10, 7}
    };
    //position de reference des block6s
    int[][] refblock_7s = {
            {10, 8},
            {9, 9},
            {10, 9},
            {9, 10},
            {10, 10},
            {10, 11}
    };

    //postion courant des block1s
    int[][] block_1s = {
            {1, 1},
            {2, 1},
            {3, 1},
            {1, 2},
            {2, 2}
    };
    //postion courant des block_2s
    int[][] block_2s = {
            {1, 4},
            {1, 5},
            {1, 6}

    };
    //postion courant des block3s
    int[][] block_3s = {
            {2, 7},
            {3, 7},
            {2, 8},
            {3, 8}
    };
    //postion courant des block4s
    int[][] block_4s = {
            {1, 9},
            {1, 10},
            {1, 11},
            {2, 11}
    };
    //position de courant des block5s
    int[][] block_5s = {
            {10, 1},
            {9, 2},
            {10, 2},
            {10, 3}
    };
    //position de courant des block6s
    int[][] block_6s = {
            {9, 5},
            {9, 6},
            {10, 6},
            {10, 7}
    };
    //position de courant des block7s
    int[][] block_7s = {
            {10, 8},
            {9, 9},
            {10, 9},
            {9, 10},
            {10, 10},
            {10, 11}
    };

    // thread utiliser pour animer les zones de depot des block
    private boolean in = true;
    private Thread cv_thread;
    SurfaceHolder holder;

    Paint paint;
    Paint paintTimer;
    Paint paintHit;
    public static boolean timerStop3 = false;

    //un nombre de aléatoire
    Random random = new Random();
    int xAleatoire;
    int yAleatoire;
    private boolean firstPaintBlock_1s =true;
    private boolean firstPaintBlock_2s =true;
    private boolean firstPaintBlock_3s =true;
    private boolean firstPaintBlock_4s =true;
    private boolean firstPaintBlock_5s =true;
    private boolean firstPaintBlock_6s =true;
    private boolean firstPaintBlock_7s =true;

    //un nombre de coups
    int hit = 0;


    /**
     * The constructor called from the main JetBoy activity
     *
     * @param context
     * @param attrs
     */
    public GameView3(Context context, AttributeSet attrs) {
        super(context, attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder = getHolder();
        holder.addCallback(this);

        // chargement des images
        mContext    = context;
        mRes        = mContext.getResources();
        block       = BitmapFactory.decodeResource(mRes, R.drawable.block_1);
        videClair   = BitmapFactory.decodeResource(mRes, R.drawable.vide_clair_1);
        vide        = BitmapFactory.decodeResource(mRes, R.drawable.vide_1);
        block1      = BitmapFactory.decodeResource(mRes, R.drawable.block1_1);
        block2      = BitmapFactory.decodeResource(mRes, R.drawable.block2_1);
        block3      = BitmapFactory.decodeResource(mRes, R.drawable.block3_1);
        block4      = BitmapFactory.decodeResource(mRes, R.drawable.block4_1);
        block5      = BitmapFactory.decodeResource(mRes, R.drawable.block5_1);
        block6      = BitmapFactory.decodeResource(mRes, R.drawable.block6_1);
        block7      = BitmapFactory.decodeResource(mRes, R.drawable.block7_1);
        win         = BitmapFactory.decodeResource(mRes, R.drawable.win_2);
        gameover    = BitmapFactory.decodeResource(mRes, R.drawable.gameover);
        back        = BitmapFactory.decodeResource(mRes, R.drawable.back);

        // initialisation des parmametres du jeu
        initparameters();

        // creation du thread
        cv_thread = new Thread(this);
        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel() {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                carte[j][i] = ref[j][i];
            }
        }

    }

    // initialisation du jeu
    public void initparameters() {
        paint = new Paint();
        paint.setColor(0xff0000);

        paintTimer = new Paint();
        paintTimer.setColor(Color.WHITE);

        paintHit = new Paint();
        paintHit.setColor(Color.WHITE);

        paintHit.setStrokeJoin(Paint.Join.ROUND);
        paintHit.setStrokeCap(Paint.Cap.ROUND);
        paintHit.setStrokeWidth(2);
        paintHit.setTextAlign(Paint.Align.LEFT);
        paintHit.setTextSize(35f);

        paintTimer.setStrokeJoin(Paint.Join.ROUND);
        paintTimer.setStrokeCap(Paint.Cap.ROUND);
        paintTimer.setStrokeWidth(2);
        paintTimer.setTextAlign(Paint.Align.LEFT);
        paintTimer.setTextSize(35f);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);
        carte = new int[carteHeight][carteWidth];
        loadlevel();
        carteTopAnchor  = (getHeight() - carteHeight * carteTileSize) / 2;
        carteLeftAnchor = (getWidth() - carteWidth * carteTileSize) / 2;
        for (int i = 0; i < block_1s.length; i++) {
            block_1s[i][1] = refblock_1s[i][1];
            block_1s[i][0] = refblock_1s[i][0];
        }
        for (int i = 0; i < block_2s.length; i++) {
            block_2s[i][1] = refblock_2s[i][1];
            block_2s[i][0] = refblock_2s[i][0];
        }
        for (int i = 0; i < block_3s.length; i++) {
            block_3s[i][1] = refblock_3s[i][1];
            block_3s[i][0] = refblock_3s[i][0];
        }
        for (int i = 0; i < block_4s.length; i++) {
            block_4s[i][1] = refblock_4s[i][1];
            block_4s[i][0] = refblock_4s[i][0];
        }
        for (int i = 0; i < block_5s.length; i++) {
            block_5s[i][1] = refblock_5s[i][1];
            block_5s[i][0] = refblock_5s[i][0];
        }
        for (int i = 0; i < block_6s.length; i++) {
            block_6s[i][1] = refblock_6s[i][1];
            block_6s[i][0] = refblock_6s[i][0];
        }
        for (int i = 0; i < block_7s.length; i++) {
            block_7s[i][1] = refblock_7s[i][1];
            block_7s[i][0] = refblock_7s[i][0];
        }
        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }
    }

    // dessin du gagne si gagne
    private void paintwin(Canvas canvas) {
        canvas.drawBitmap(win, carteLeftAnchor + 3* carteTileSize, carteTopAnchor + 4 * carteTileSize, null);
    }

    //dessin du gameover si timer =0
    private void paintGameOver(Canvas canvas){
        canvas.drawBitmap(gameover, carteLeftAnchor +  2*carteTileSize, carteTopAnchor + 4 * carteTileSize, null);
    }

    //dessin de la fleche retourne
    private void paintBack(Canvas canvas){
        canvas.drawBitmap(back, carteLeftAnchor/4,carteTopAnchor/4, null);
    }

    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        for (int i = 0; i < carteHeight; i++) {
            for (int j = 0; j < carteWidth; j++) {
                switch (carte[i][j]) { /*ici i=y, j=x*/
                    case CST_block:
                        canvas.drawBitmap(block, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_vide_clair:
                        canvas.drawBitmap(videClair, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_vide:
                        canvas.drawBitmap(vide, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block1:
                        canvas.drawBitmap(block1, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block2:
                        canvas.drawBitmap(block2, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block3:
                        canvas.drawBitmap(block3, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block4:
                        canvas.drawBitmap(block4, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block5:
                        canvas.drawBitmap(block5, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block6:
                        canvas.drawBitmap(block6, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                    case CST_block7:
                        canvas.drawBitmap(block7, carteLeftAnchor + j * carteTileSize, carteTopAnchor + i * carteTileSize, null);
                        break;
                }
            }
        }
    }

    //dessin des block1s
    private void paintblock_1s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_1s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(1, 1, xAleatoire, yAleatoire);
            firstPaintBlock_1s = false;
        }
        for (int i = 0; i < block_1s.length; i++) {
            canvas.drawBitmap(block1, carteLeftAnchor + block_1s[i][0] * carteTileSize, carteTopAnchor + block_1s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block2s
    private void paintblock_2s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_2s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(1, 4, xAleatoire, yAleatoire);
            firstPaintBlock_2s = false;
        }
        for (int i = 0; i < block_2s.length; i++) {
            canvas.drawBitmap(block2, carteLeftAnchor + block_2s[i][0] * carteTileSize, carteTopAnchor + block_2s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block3s
    private void paintblock_3s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_3s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(2, 7, xAleatoire, yAleatoire);
            firstPaintBlock_3s = false;
        }
        for (int i = 0; i < block_3s.length; i++) {
            canvas.drawBitmap(block3, carteLeftAnchor + block_3s[i][0] * carteTileSize, carteTopAnchor + block_3s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block4s
    private void paintblock_4s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_4s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(1, 9, xAleatoire, yAleatoire);
            firstPaintBlock_4s = false;
        }
        for (int i = 0; i < block_4s.length; i++) {
            canvas.drawBitmap(block4, carteLeftAnchor + block_4s[i][0] * carteTileSize, carteTopAnchor + block_4s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block5s
    private void paintblock_5s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_5s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(10, 1, xAleatoire, yAleatoire);
            firstPaintBlock_5s = false;
        }
        for (int i = 0; i < block_5s.length; i++) {
            canvas.drawBitmap(block5, carteLeftAnchor + block_5s[i][0] * carteTileSize, carteTopAnchor + block_5s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block6s
    private void paintblock_6s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_6s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(9, 5, xAleatoire, yAleatoire);
            firstPaintBlock_6s = false;
        }
        for (int i = 0; i < block_6s.length; i++) {
            canvas.drawBitmap(block6, carteLeftAnchor + block_6s[i][0] * carteTileSize, carteTopAnchor + block_6s[i][1] * carteTileSize, null);
        }
    }
    //dessin des block7s
    private void paintblock_7s(Canvas canvas) {
        //affiche x,y aléatoirement
        if(firstPaintBlock_7s) {
            xAleatoire = random.nextInt(12);
            yAleatoire = random.nextInt(12);
            UpdateBlocks(10, 8, xAleatoire, yAleatoire);
            firstPaintBlock_7s = false;
        }
        for (int i = 0; i < block_7s.length; i++) {
            canvas.drawBitmap(block7, carteLeftAnchor + block_7s[i][0] * carteTileSize, carteTopAnchor + block_7s[i][1] * carteTileSize, null);
        }
    }
    //permet d'identifier si la partie est gagnee
    private boolean isWon() {
        for (int i = 0; i < block_1s.length; i++) {
            if (!IsCell(block_1s[i][0], block_1s[i][1], CST_vide_clair))
                return false;
        }

        for (int i = 0; i < block_2s.length; i++) {
            if (!IsCell(block_2s[i][0], block_2s[i][1], CST_vide_clair))
                return false;
        }

        for (int i = 0; i < block_3s.length; i++) {
            if (!IsCell(block_3s[i][0], block_3s[i][1], CST_vide_clair))
                return false;
        }

        for (int i = 0; i < block_4s.length; i++) {
            if (!IsCell(block_4s[i][0], block_4s[i][1], CST_vide_clair))
                return false;
        }

        for (int i = 0; i < block_5s.length; i++) {
            if (!IsCell(block_5s[i][0], block_5s[i][1], CST_vide_clair))
                return false;
        }
        for (int i = 0; i < block_6s.length; i++) {
            if (!IsCell(block_6s[i][0], block_6s[i][1], CST_vide_clair))
                return false;
        }

        for (int i = 0; i < block_7s.length; i++) {
            if (!IsCell(block_7s[i][0], block_7s[i][1], CST_vide_clair))
                return false;
        }
        timerStop3 = true;
        return true;
    }

    //permet d'identifier si la partie est gameover
    private boolean isGameOver(){
        if(timer ==0 && !isWon())
            return true;
        return false;
    }

    //controle de la valeur de la fleche retoure
    private boolean IsBack(int x, int y){
        if( (x >= carteLeftAnchor/4) && (x <= (back.getWidth()+carteLeftAnchor/4))
                &&
                (y >= carteTopAnchor/4) && (y <= (back.getHeight()+carteTopAnchor/4)) )
            return true;

        return false;
    }

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau ou gameouver et des blocks et de la  fleche et affiche timer et hit)
    private void nDraw(Canvas canvas) {
        canvas.drawRGB(44, 44, 44);
        canvas.drawText("Timer : " + timer, carteLeftAnchor, carteTopAnchor -20, paintTimer);
        canvas.drawText("Hit :" + hit,carteLeftAnchor*2+10, carteTopAnchor -20, paintHit);
        paintBack(canvas);
        if (isWon()) {
            paintcarte(canvas);
            paintwin(canvas);
        } else if(isGameOver()){
            paintcarte(canvas);
            paintGameOver(canvas);
        }else{
            paintcarte(canvas);
            paintblock_1s(canvas);
            paintblock_2s(canvas);
            paintblock_3s(canvas);
            paintblock_4s(canvas);
            paintblock_5s(canvas);
            paintblock_6s(canvas);
            paintblock_7s(canvas);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("-> FCT <-", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged " + width + " - " + height);
        initparameters();
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    @Override
    public void run() {
        Canvas c = null;
        while (in) {
            try {
                cv_thread.sleep(10);//pause
                try {
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
                in = false;
            }
        }
    }

    // verification que nous sommes dans le tableau
    private boolean IsOut(int x, int y) {
        if ((x < 1) || (x > carteWidth - 2)) {

            return true;
        }
        if ((y < 1) || (y > carteHeight - 2)) {
            return true;
        }
        return false;
    }
    //verification que block1s sont dans le tableau
    private boolean Block1sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_1s.length; i++) {
            int xPossible = new_x + block_1s[i][0] - x;
            int yPossible = new_y + block_1s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block2s sont dans le tableau
    private boolean Block2sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_2s.length; i++) {
            int xPossible = new_x + block_2s[i][0] - x;
            int yPossible = new_y + block_2s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block3s sont dans le tableau
    private boolean Block3sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_3s.length; i++) {
            int xPossible = new_x + block_3s[i][0] - x;
            int yPossible = new_y + block_3s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block4s sont dans le tableau
    private boolean Block4sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_4s.length; i++) {
            int xPossible = new_x + block_4s[i][0] - x;
            int yPossible = new_y + block_4s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block5s sont dans le tableau
    private boolean Block5sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_5s.length; i++) {
            int xPossible = new_x + block_5s[i][0] - x;
            int yPossible = new_y + block_5s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block6s sont dans le tableau
    private boolean Block6sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_6s.length; i++) {
            int xPossible = new_x + block_6s[i][0] - x;
            int yPossible = new_y + block_6s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }
    //verification que block7s sont dans le tableau
    private boolean Block7sIsOut(int x, int y, int new_x, int new_y){

        for (int i = 0; i < block_7s.length; i++) {
            int xPossible = new_x + block_7s[i][0] - x;
            int yPossible = new_y + block_7s[i][1] - y;
            if ((xPossible < 1) || (xPossible > carteWidth - 2)) {

                return true;
            }
            if ((yPossible < 1) || (yPossible > carteHeight - 2)) {
                return true;
            }

        }
        return false;
    }

    //controle de la valeur d'une cellule
    private boolean IsCell(int x, int y, int mask) {
        if (carte[y][x] == mask) { /*la tableau de carte true_y = x, true_x = y*/
            return true;
        }
        return false;
    }

    // controle si nous avons un block1 dans la case
    private boolean IsBlock1(int x, int y) {
        for (int i = 0; i < block_1s.length; i++) {
            if ((block_1s[i][0] == x) && (block_1s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block2 dans la case
    private boolean IsBlock2(int x, int y) {
        for (int i = 0; i < block_2s.length; i++) {
            if ((block_2s[i][0] == x) && (block_2s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block3 dans la case
    private boolean IsBlock3(int x, int y) {
        for (int i = 0; i < block_3s.length; i++) {
            if ((block_3s[i][0] == x) && (block_3s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block4 dans la case
    private boolean IsBlock4(int x, int y) {
        for (int i = 0; i < block_4s.length; i++) {
            if ((block_4s[i][0] == x) && (block_4s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block5 dans la case
    private boolean IsBlock5(int x, int y) {
        for (int i = 0; i < block_5s.length; i++) {
            if ((block_5s[i][0] == x) && (block_5s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block6 dans la case
    private boolean IsBlock6(int x, int y) {
        for (int i = 0; i < block_6s.length; i++) {
            if ((block_6s[i][0] == x) && (block_6s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }
    // controle si nous avons un block7 dans la case
    private boolean IsBlock7(int x, int y) {
        for (int i = 0; i < block_7s.length; i++) {
            if ((block_7s[i][0] == x) && (block_7s[i][1] == y)) {
                return true;
            }
        }
        return false;
    }

    //teste si block1s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock1s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_1s.length; i++) {
            xPossible = new_x + block_1s[i][0] - x;
            yPossible = new_y + block_1s[i][1] - y;
            if (IsBlock2(xPossible, yPossible) || IsBlock3(xPossible, yPossible) ||IsBlock4(xPossible, yPossible) ||
                    IsBlock5(xPossible, yPossible) || IsBlock6(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }

    //teste si block2s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock2s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_2s.length; i++) {
            xPossible = new_x + block_2s[i][0] - x;
            yPossible = new_y + block_2s[i][1] - y;
            if (IsBlock1(xPossible, yPossible) || IsBlock3(xPossible, yPossible) || IsBlock4(xPossible, yPossible) ||
                    IsBlock5(xPossible, yPossible) || IsBlock6(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }
    //teste si block3s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock3s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_3s.length; i++) {
            xPossible = new_x + block_3s[i][0] - x;
            yPossible = new_y + block_3s[i][1] - y;
            if (IsBlock2(xPossible, yPossible) || IsBlock1(xPossible, yPossible) || IsBlock4(xPossible, yPossible) ||
                    IsBlock5(xPossible, yPossible) || IsBlock6(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }
    //teste si block4s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock4s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_4s.length; i++) {
            xPossible = new_x + block_4s[i][0] - x;
            yPossible = new_y + block_4s[i][1] - y;
            if (IsBlock2(xPossible, yPossible) || IsBlock1(xPossible, yPossible) || IsBlock3(xPossible, yPossible) ||
                    IsBlock5(xPossible, yPossible) || IsBlock6(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }
    //teste si block5s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock5s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_5s.length; i++) {
            xPossible = new_x + block_5s[i][0] - x;
            yPossible = new_y + block_5s[i][1] - y;
            if (IsBlock1(xPossible, yPossible) || IsBlock2(xPossible, yPossible) || IsBlock3(xPossible, yPossible) ||
                    IsBlock4(xPossible, yPossible) || IsBlock6(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }
    //teste si block6s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock6s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_6s.length; i++) {
            xPossible = new_x + block_6s[i][0] - x;
            yPossible = new_y + block_6s[i][1] - y;
            if (IsBlock1(xPossible, yPossible) || IsBlock2(xPossible, yPossible) || IsBlock3(xPossible, yPossible) ||
                    IsBlock4(xPossible, yPossible) || IsBlock5(xPossible, yPossible) || IsBlock7(xPossible, yPossible))
                return false;
        }
        return true;
    }
    //teste si block7s peuvent traverser, évite d'être couvert
    private boolean IspossibleBlock7s(int x, int y, int new_x, int new_y){
        int xPossible, yPossible;
        for (int i = 0; i < block_7s.length; i++) {
            xPossible = new_x + block_7s[i][0] - x;
            yPossible = new_y + block_7s[i][1] - y;
            if (IsBlock1(xPossible, yPossible) || IsBlock2(xPossible, yPossible) || IsBlock3(xPossible, yPossible) ||
                    IsBlock4(xPossible, yPossible) || IsBlock5(xPossible, yPossible) || IsBlock6(xPossible, yPossible))
                return false;
        }
        return true;
    }

    //met à jour la postion des block1s
    private void UpdateBlock1s(int x, int y, int new_x, int new_y) {
        int dif_x, dif_y;
        for (int i = 0; i < block_1s.length; i++) {

            dif_x = block_1s[i][0] - x;
            dif_y = block_1s[i][1] - y;
            block_1s[i][0] = new_x + dif_x;
            block_1s[i][1] = new_y + dif_y;

        }

    }

    //met à jour la postion des block2s
    private void UpdateBlock2s(int x, int y, int new_x, int new_y) {
        int dif_x, dif_y;
        for (int i = 0; i < block_2s.length; i++) {
            dif_x = block_2s[i][0] - x;
            dif_y = block_2s[i][1] - y;
            block_2s[i][0] = new_x + dif_x;
            block_2s[i][1] = new_y + dif_y;
        }
    }

    //met à jour la postion des block3s
    private void UpdateBlock3s(int x, int y, int new_x, int new_y) {
        int dif_x, dif_y;
        for (int i = 0; i < block_3s.length; i++) {
            dif_x = block_3s[i][0] - x;
            dif_y = block_3s[i][1] - y;
            block_3s[i][0] = new_x + dif_x;
            block_3s[i][1] = new_y + dif_y;
        }
    }

    //met à jour la postion des block4s
    private void UpdateBlock4s(int x, int y, int new_x, int new_y) {
        int dif_x=0;
        int dif_y=0;
        for (int i = 0; i < block_4s.length; i++) {
            dif_x = block_4s[i][0] - x;
            dif_y = block_4s[i][1] - y;
            block_4s[i][0] = new_x + dif_x;
            block_4s[i][1] = new_y + dif_y;
        }
    }
    //met à jour la postion des block5s
    private void UpdateBlock5s(int x, int y, int new_x, int new_y) {
        int dif_x=0;
        int dif_y=0;
        for (int i = 0; i < block_5s.length; i++) {
            dif_x = block_5s[i][0] - x;
            dif_y = block_5s[i][1] - y;
            block_5s[i][0] = new_x + dif_x;
            block_5s[i][1] = new_y + dif_y;
        }
    }
    //met à jour la postion des block6s
    private void UpdateBlock6s(int x, int y, int new_x, int new_y) {
        int dif_x, dif_y;
        for (int i = 0; i < block_6s.length; i++) {
            dif_x = block_6s[i][0] - x;
            dif_y = block_6s[i][1] - y;
            block_6s[i][0] = new_x + dif_x;
            block_6s[i][1] = new_y + dif_y;
        }
    }
    //met à jour la postion des block7s
    private void UpdateBlock7s(int x, int y, int new_x, int new_y) {
        int dif_x, dif_y;
        for (int i = 0; i < block_7s.length; i++) {
            dif_x = block_7s[i][0] - x;
            dif_y = block_7s[i][1] - y;
            block_7s[i][0] = new_x + dif_x;
            block_7s[i][1] = new_y + dif_y;
        }
    }



    //met a jour la positon des blocks
    private void UpdateBlocks(int x, int y, int new_x, int new_y) {
        if (!IsOut(x, y)) {
            if (IsBlock1(x,y) && IspossibleBlock1s(x, y,new_x,new_y)  && !Block1sIsOut(x,y,new_x,new_y) )
                UpdateBlock1s(x, y, new_x, new_y);
            if (IsBlock2(x, y) &&  IspossibleBlock2s(x, y,new_x,new_y) && !Block2sIsOut(x,y,new_x,new_y))
                UpdateBlock2s(x, y, new_x, new_y);
            if (IsBlock3(x, y) && IspossibleBlock3s(x, y,new_x,new_y) && !Block3sIsOut(x,y,new_x,new_y) )
                UpdateBlock3s(x, y, new_x, new_y);
            if (IsBlock4(x, y) && IspossibleBlock4s(x, y,new_x,new_y) && !Block4sIsOut(x,y,new_x,new_y) )
                UpdateBlock4s(x, y, new_x, new_y);
            if (IsBlock5(x, y) && IspossibleBlock5s(x, y,new_x,new_y) && !Block5sIsOut(x,y,new_x,new_y) )
                UpdateBlock5s(x, y, new_x, new_y);
            if (IsBlock6(x, y) && IspossibleBlock6s(x, y,new_x,new_y) && !Block6sIsOut(x,y,new_x,new_y) )
                UpdateBlock6s(x, y, new_x, new_y);
            if (IsBlock7(x, y) && IspossibleBlock7s(x, y,new_x,new_y) && !Block7sIsOut(x,y,new_x,new_y) )
                UpdateBlock7s(x, y, new_x, new_y);
        }
    }

    // x dans la carte
    public int xInCard(int x) {
        return (x - carteLeftAnchor) / carteTileSize;
    }

    // y dans la carte
    public int yInCard(int y) {
        return (y - carteTopAnchor) / carteTileSize;
    }

    //fonction permettant de retourner MainActivity (menu de jeu)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i("test", "HIIIIIIIIIII");
            timerStop3 = true;
            //pour le music
            if(playing)
                musicLoop = true;
            //retourne le Menu
            Intent intent = new Intent(getContext(), MainActivity.class);
            /*his clears the activity stack and opens your main activity*/
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            getContext().startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouche(int action, MotionEvent event) {

        Log.i("-> FCT <-", "66666666666666666!!");
        if (action == MotionEvent.ACTION_DOWN) {
            initialDown = true;
            xDown = (int) event.getX();
            yDown = (int) event.getY();


        } else if (action == MotionEvent.ACTION_MOVE) {
            xMove = (int) event.getX();
            yMove = (int) event.getY();
            if (initialDown) {
                UpdateBlocks(xInCard(xDown), yInCard(yDown), xInCard(xMove), yInCard(yMove));
                initialDown = false;
            } else {
                UpdateBlocks(xInCard(prexMove), yInCard(preyMove), xInCard(xMove), yInCard(yMove));
            }
            prexMove = xMove;
            preyMove = yMove;

        } else if (action == MotionEvent.ACTION_UP) {
            xUp = (int) event.getX();
            yUp = (int) event.getY();
            UpdateBlocks(xInCard(xMove), yInCard(yMove), xInCard(xUp), yInCard(yUp));

            if(IsBlock1(xInCard(xUp), yInCard(yUp)) || IsBlock2(xInCard(xUp), yInCard(yUp))
                    || IsBlock3(xInCard(xUp), yInCard(yUp)) || IsBlock4(xInCard(xUp), yInCard(yUp))
                    || IsBlock5(xInCard(xUp), yInCard(yUp)) || IsBlock6(xInCard(xUp), yInCard(yUp))
                    || IsBlock7(xInCard(xUp), yInCard(yUp))) {
                hit++;
            }
        }
        //si gagne, MotionEvant.Action_UP ca marche pas dans le jeu
        if (isWon()){
            hit ++;
            hitTmp = hit;
        }

        return true;

    }

    //fonction permettant de recuperer les evenements tactiles
    @Override
    public boolean onTouchEvent(MotionEvent event) {
         /*affiche la postion de toucher*/
        int x           = xInCard((int) event.getX());
        int y           = yInCard((int) event.getY());
        int x_original  = (int)event.getX();
        int y_original  = (int)event.getY();

        if (!IsOut(x, y) && !isWon() && !isGameOver()) {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                onTouche(MotionEvent.ACTION_DOWN, event);
            } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                onTouche(MotionEvent.ACTION_MOVE, event);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                onTouche(MotionEvent.ACTION_UP, event);
            }
            return true;
        }
        //vérification de la fleche back
        if(IsBack(x_original,y_original)){
            onKeyDown(KeyEvent.KEYCODE_BACK,null);
        }
        Log.i("-> FCT <-", "onTouchEvent: " + event.getX());
        return super.onTouchEvent(event);
    }
}