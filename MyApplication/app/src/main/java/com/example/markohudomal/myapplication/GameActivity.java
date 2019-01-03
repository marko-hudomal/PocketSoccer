package com.example.markohudomal.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends Activity implements OnTouchListener, Controller.ViewInterface {

    private Controller mController;
    //Views
    private CustomImageView mCustomImageView;
    private TextView endMessage;


    //SETTINGS VALUES--------------------------
    private int settings_fieldType=0;
    private int settings_gameEndType=0;
    private int settings_gameEndVal=180;
    private int settings_gameSpeed=1;

    private String name1="Player1";
    private String name2="Player2";
    private boolean bot1;
    private boolean bot2;
    private int flag1=0;
    private int flag2=1;
    //------------------------------------------

    //GAME VALUES-------------------------------
    private int score1=0;
    private int score2=0;
    //------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //CustomImageView
        mCustomImageView = findViewById(R.id.custom_image_view);
        mCustomImageView.initCustomImageView(this);
        mCustomImageView.setOnTouchListener(this);

        //TextView
        endMessage=findViewById(R.id.endMessage);
        endMessage.setVisibility(View.GONE);

        mController = new Controller(this, mCustomImageView.getGameData());


        // Gustina ekrana
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mCustomImageView.setDisplayDensity(metrics.densityDpi);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x;
        float y;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                mController.onFirstTouchDown(x, y);
                //Log.d("MY_LOG","click_down: ["+ x+","+y+"]");
                break;


            case MotionEvent.ACTION_UP:
                x = event.getX();
                y = event.getY();
                mController.onAllTouchesUp(x, y);
                //Log.d("MY_LOG","click_up: ["+ x+","+y+"]");
                break;
        }
        return true;
    }

    @Override
    public void updateView() {
        mCustomImageView.invalidate();
    }

    public void setEndMessage(final int player_wins)
    {
        endMessage.post(new Runnable() {
            @Override
            public void run() {
                endMessage.setVisibility(View.VISIBLE);
                switch(player_wins)
                {
                    case -1:
                        endMessage.setText("Game draw!");
                        break;
                    case 0:
                        endMessage.setText(name1+" wins!");
                        break;
                    case 1:
                        endMessage.setText(name2+" wins!");
                        break;
                }
            }
        });


    }

    public void endThisGame()
    {

        //Toast.makeText(this, "game is over!", Toast.LENGTH_SHORT).show();
        finish();
    }
    //=============================================================================================

    private GameThread mThread;

    public int getSettings_fieldType() {
        return settings_fieldType;
    }

    public void setSettings_fieldType(int settings_fieldType) {
        this.settings_fieldType = settings_fieldType;
    }

    public int getSettings_gameEndType() {
        return settings_gameEndType;
    }

    public void setSettings_gameEndType(int settings_gameEndType) {
        this.settings_gameEndType = settings_gameEndType;
    }

    public int getSettings_gameEndVal() {
        return settings_gameEndVal;
    }

    public void setSettings_gameEndVal(int settings_gameEndVal) {
        this.settings_gameEndVal = settings_gameEndVal;
    }

    public int getSettings_gameSpeed() {
        return settings_gameSpeed;
    }

    public void setSettings_gameSpeed(int settings_gameSpeed) {
        this.settings_gameSpeed = settings_gameSpeed;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public boolean isBot1() {
        return bot1;
    }

    public void setBot1(boolean bot1) {
        this.bot1 = bot1;
    }

    public boolean isBot2() {
        return bot2;
    }

    public void setBot2(boolean bot2) {
        this.bot2 = bot2;
    }

    public int getFlag1() {
        return flag1;
    }

    public void setFlag1(int flag1) {
        this.flag1 = flag1;
    }

    public int getFlag2() {
        return flag2;
    }

    public void setFlag2(int flag2) {
        this.flag2 = flag2;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

}
