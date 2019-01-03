package com.example.markohudomal.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;

import com.example.markohudomal.myapplication.gamedata.Ball;
import com.example.markohudomal.myapplication.gamedata.GameData;


public class CustomImageView extends android.support.v7.widget.AppCompatImageView implements GameThread.ThreadListener {


    private int mDisplayDensity;


    private GameData mGameData;
    //private Bitmap mFigure;
    private Bitmap player1;
    private Bitmap player2;
    private Bitmap football;
    private Bitmap scoreBoard;
    private Bitmap nets;

    private Paint mTempPaint;
    private Paint mAlphaPaint;
    private Rect mTempRect;

    private GameActivity gameActivity;
    private boolean gameOver=false;

    private GameThread mThread;
    private int endPause;
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initCustomImageView(GameActivity gameActivity) {

        this.gameActivity=gameActivity;
        endPause=StaticValues.endPause;
        gameOver=false;
        mGameData = new GameData(gameActivity);

        mTempPaint = new Paint();
        mAlphaPaint= new Paint();
        mTempRect = new Rect();

        player1= BitmapFactory.decodeResource(getResources(), StaticValues.ball_res[gameActivity.getFlag1()]);
        player2= BitmapFactory.decodeResource(getResources(), StaticValues.ball_res[gameActivity.getFlag2()]);
        scoreBoard = BitmapFactory.decodeResource(getResources(),R.drawable.score_board_plus);
        football =BitmapFactory.decodeResource(getResources(),R.drawable.ball_play);
        nets = BitmapFactory.decodeResource(getResources(),R.drawable.nets);

        mThread=new GameThread("game_thread",Process.THREAD_PRIORITY_BACKGROUND);
        mThread.setListener(this);
        mThread.setRunning(true);
    }
    public void startThread()
    {
        mThread.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTempPaint.setAlpha(1000);
        mTempPaint.setColor(Color.CYAN);
        mTempPaint.setStrokeWidth(2);
        mTempPaint.setTextSize(50);

        mAlphaPaint.setColor(Color.parseColor(StaticValues.colorWhoPlays));
        mAlphaPaint.setStrokeWidth(2);
        mAlphaPaint.setAlpha(100);


        getDrawingRect(mTempRect);
        canvas.getClipBounds(mTempRect);

        //BALLS-------------------------------------------------------------------------------------
        for(int i=0;i<mGameData.getPlayer1_balls().size();i++)
        {
            Ball temp = mGameData.getPlayer1_balls().get(i);
            //canvas.drawCircle(temp.getFigureHolder().left, temp.getFigureHolder().top,(mTempRect.bottom-mTempRect.top)/15, mTempPaint);

            canvas.drawBitmap(player1, null, temp.getFigureHolder(), mTempPaint);
            if (temp.isSelected()) {
                canvas.drawCircle(temp.getFigurePosition().x,temp.getFigurePosition().y,temp.radius/2+5,mAlphaPaint);
            }


            canvas.drawLine(temp.getFigurePosition().x,temp.getFigurePosition().y,temp.getFigurePosition().x+temp.vectorX*5,temp.getFigurePosition().y+temp.vectorY*5,mTempPaint);
        }
        for(int i=0;i<mGameData.getPlayer2_balls().size();i++)
        {
            Ball temp = mGameData.getPlayer2_balls().get(i);
            //canvas.drawCircle(temp.getFigureHolder().left, temp.getFigureHolder().top,(mTempRect.bottom-mTempRect.top)/15, mTempPaint);

            canvas.drawBitmap(player2, null, temp.getFigureHolder(), mTempPaint);
            if (temp.isSelected()) {
                canvas.drawCircle(temp.getFigurePosition().x,temp.getFigurePosition().y,temp.radius/2+5,mAlphaPaint);
            }
            canvas.drawLine(temp.getFigurePosition().x,temp.getFigurePosition().y,temp.getFigurePosition().x+temp.vectorX*5,temp.getFigurePosition().y+temp.vectorY*5,mTempPaint);
        }
        canvas.drawBitmap(football, null, mGameData.getFootball().getFigureHolder(), mTempPaint);
        //------------------------------------------------------------------------------------------

        //SCORE BOARD
        mTempPaint.setColor(Color.WHITE);
        mTempPaint.setAlpha(450);
        float middleY=(mGameData.getScoreBoardConstraint().right+mGameData.getScoreBoardConstraint().left)/2;
        float middleX=(mGameData.getScoreBoardConstraint().bottom+mGameData.getScoreBoardConstraint().top)*(float)0.70;

        //ScoreBoard
        canvas.drawBitmap(scoreBoard, null, mGameData.getScoreBoardConstraint(), mTempPaint);
        //Goals
        canvas.drawText(""+mGameData.getGoals1(), middleY-20-(new String(""+mGameData.getGoals1()).length())*30, middleX, mTempPaint);
        canvas.drawText(""+mGameData.getGoals2(),middleY+18, middleX, mTempPaint);

        //Names
        //----------------------------------------------------------------------------------------------------
        if (mGameData.getPlayer_turn()==0)
            mTempPaint.setColor(Color.parseColor(StaticValues.colorWhoPlays));
        else
            mTempPaint.setColor(Color.WHITE);

        canvas.drawText(mGameData.getName1(), middleY-150-mGameData.getName1().length()*26, middleX, mTempPaint);
        if (mGameData.getPlayer_turn()==1)
            mTempPaint.setColor(Color.parseColor(StaticValues.colorWhoPlays));
        else
            mTempPaint.setColor(Color.WHITE);
        canvas.drawText(mGameData.getName2(),middleY+150, middleX, mTempPaint);
        //----------------------------------------------------------------------------------------------------

        //ScoreBoard Time/Score UP
        //----------------------------------------------------------------------------------------------------
        mTempPaint.setColor(Color.WHITE);
        if (mGameData.isTimeGame())
        {
            //Seconds game
            mTempPaint.setAlpha(200);
            mTempPaint.setTextSize(25);
            canvas.drawText("time left: "+mGameData.getEndVal(),middleY*(float)0.92,35,mTempPaint);
        }
        //----------------------------------------------------------------------------------------------------
        //Seconds turn
        mTempPaint.setAlpha(100);
        mTempPaint.setTextSize(250);
        canvas.drawText(mGameData.getSeconds_turn()+"",mTempRect.right/2-72,mTempRect.bottom/2+80,mTempPaint);


        //Nets
        mTempPaint.setAlpha(1000);
        canvas.drawBitmap(nets, null, mGameData.getFieldConstraint(), mTempPaint);
    }


    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        updateSize(width, height);
    }

    protected void updateSize(int width, int height) {

        Rect rect = new Rect();
        getDrawingRect(rect);
        mGameData.setFieldConstraint( new RectF(rect.left,rect.top,rect.right,rect.bottom));
        mGameData.setScoreBoardConstraint( new RectF(rect.right*(float)0.20,2,rect.right*(float)0.80,rect.bottom*(float)0.16));
        mGameData.setGoalConstraints(new RectF(rect.left,(rect.bottom+rect.top)*0.36f,(rect.left + rect.right)*0.061f,(rect.bottom+rect.top)*0.64f),new RectF((rect.left + rect.right)*0.939f,(rect.bottom+rect.top)*0.36f,rect.right,(rect.bottom+rect.top)*0.64f));

        Bitmap background = BitmapFactory.decodeResource(getResources(), StaticValues.field_res[gameActivity.getSettings_fieldType()]);
        setImageBitmap(Bitmap.createScaledBitmap(background, rect.right-rect.left, rect.bottom-rect.top, false));

        startThread();
    }




    public void setDisplayDensity(int densityDpi) {
        this.mDisplayDensity = densityDpi;
    }

    public GameData getGameData() {
        return mGameData;
    }

    @Override
    public void secondPassed() {
           if ( mGameData.isTimeGame())
           {
               if (mGameData.decEndVal())
               {
                   gameOver=true;
                   mThread.setRunning(false);
                   mGameData.timeOver();
                   return;
               }
           }
           mGameData.decSecTurn();

           invalidate();
    }

    @Override
    public void updateFieldState() {
        if (mGameData.updateFieldState())
            invalidate();
    }


    @Override
    public void endThisGame() {
        gameActivity.endThisGame();
    }


}
