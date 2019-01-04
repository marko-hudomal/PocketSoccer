package com.example.markohudomal.pocketsoccer;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.database.model.BundleAwareViewModelFactory;
import com.example.markohudomal.pocketsoccer.database.model.MyViewModel;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;
import com.example.markohudomal.pocketsoccer.game.Controller;
import com.example.markohudomal.pocketsoccer.game.CustomImageView;
import com.example.markohudomal.pocketsoccer.game.GameThread;

import java.util.Date;

public class GameActivity  extends AppCompatActivity implements View.OnTouchListener, Controller.ViewInterface {
    private boolean resume_game;

    private Controller mController;


    //Views
    private CustomImageView mCustomImageView;
    private TextView endMessage;


    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPreferencesResume;

    private MyViewModel mViewModel;

    //SETTINGS VALUES--------------------------
    private int settings_fieldType;
    private int settings_gameEndType;
    private int settings_gameEndVal;
    private int settings_gameSpeed;

    private String name1;
    private String name2;
    private boolean bot1;
    private boolean bot2;
    private int flag1;
    private int flag2;
    //------------------------------------------

    //GAME VALUES-------------------------------
    private int score1 = 0;
    private int score2 = 0;
    //------------------------------------------

    public MediaPlayer click;
    public MediaPlayer crowd;
    public MediaPlayer bounce;
    public MediaPlayer whistle;
    public MediaPlayer golf_hit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Media sounds
        click = MediaPlayer.create(this, R.raw.click3);
        crowd = MediaPlayer.create(this, R.raw.crowd);
        bounce = MediaPlayer.create(this, R.raw.bounce);
        whistle = MediaPlayer.create(this, R.raw.whistle);
        golf_hit = MediaPlayer.create(this, R.raw.golf_hit);

        //All values----------------------------------------------------------------------------------------------
        //SharedPreferences=======================================================================================
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        sharedPreferencesResume = getSharedPreferences("game_data",MODE_PRIVATE);

        int set = sharedPreferences.getInt("values_set", -1);
        switch (set) {
            //Default values
            case 0:
                settings_fieldType = sharedPreferences.getInt("default_fieldType", StaticValues.Default_fieldType);
                settings_gameEndType = sharedPreferences.getInt("default_gameEndType", StaticValues.Default_gameEndType);
                settings_gameEndVal = sharedPreferences.getInt("default_gameEndVal", StaticValues.Default_gameEndVal);
                settings_gameSpeed = sharedPreferences.getInt("default_gameSpeed", StaticValues.Default_gameSpeed);
                break;
            //Custom values
            case 1:
                settings_fieldType = sharedPreferences.getInt("fieldType", StaticValues.Default_fieldType);
                settings_gameEndType = sharedPreferences.getInt("gameEndType", StaticValues.Default_gameEndType);
                settings_gameEndVal = sharedPreferences.getInt("gameEndVal", StaticValues.Default_gameEndVal);
                settings_gameSpeed = sharedPreferences.getInt("gameSpeed", StaticValues.Default_gameSpeed);
                break;
            //Error states
            default:
                settings_fieldType = StaticValues.Default_fieldType;
                settings_gameEndType = StaticValues.Default_gameEndType;
                settings_gameEndVal = StaticValues.Default_gameEndVal;
                settings_gameSpeed = StaticValues.Default_gameSpeed;
        }
        Log.d("MY_LOG", "SP VALUES!---------------------------");
        Log.d("MY_LOG", "settings_fieldType: " + settings_fieldType);
        Log.d("MY_LOG", "setting_gameEndType: " + settings_gameEndType);
        Log.d("MY_LOG", "setting_gameEndVal: " + settings_gameEndVal);
        Log.d("MY_LOG", "setting_gameSpeed: " + settings_gameSpeed);
        Log.d("MY_LOG", "----------------------------------------");
        //INTENT====================================================================================================
        Intent intent = getIntent();
        name1 = intent.getStringExtra("start_name1");
        name2 = intent.getStringExtra("start_name2");
        bot1 = intent.getBooleanExtra("start_bot1", false);
        bot2 = intent.getBooleanExtra("start_bot2", false);
        flag1 = intent.getIntExtra("start_flag1", 0);
        flag2 = intent.getIntExtra("start_flag2", 1);

        Log.d("MY_LOG", "INTENT VALUES!---------------------------");
        Log.d("MY_LOG", "name1: " + name1);
        Log.d("MY_LOG", "name2: " + name2);
        Log.d("MY_LOG", "bot1: " + bot1);
        Log.d("MY_LOG", "bot2: " + bot2);
        Log.d("MY_LOG", "flag1: " + flag1);
        Log.d("MY_LOG", "flag2: " + flag2);
        Log.d("MY_LOG", "----------------------------------------");
        //===========================================================================================================
        resume_game=intent.getBooleanExtra("resume_game",false);
        //===========================================================================================================
        //Initalization==================================================================================================================================
        //ModelView
        ViewModelProvider provider = ViewModelProviders.of(this);
        BundleAwareViewModelFactory<MyViewModel> factory = new BundleAwareViewModelFactory<>(savedInstanceState, provider);
        mViewModel = factory.create(MyViewModel.class);


        //==========================================================================================
        //CustomImageView
        mCustomImageView = findViewById(R.id.custom_image_view);
        mCustomImageView.initCustomImageView(this);
        mCustomImageView.setOnTouchListener(this);

        //TextView
        endMessage = findViewById(R.id.endMessage);
        endMessage.setVisibility(View.GONE);

        mController = new Controller(this, mCustomImageView.getGameData());


        // Gustina ekrana
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mCustomImageView.setDisplayDensity(metrics.densityDpi);

        whistle.start();
        //==========================================================================================
    }

    public void endThisGame() {
        whistle.start();

        editor = sharedPreferencesResume.edit();
        editor.putBoolean("paused_game", false);
        Log.d("MY_LOG","game paused deleted");
        editor.commit();

        score1 = mCustomImageView.getGameData().getGoals1();
        score2 = mCustomImageView.getGameData().getGoals2();

        Log.d("MY_LOG", "rezultat 1: " + score1);
        Log.d("MY_LOG", "rezultat 2: " + score2);

        //Database update!
        updatedDatabaseForThisGame(name1, name2, score1, score2);

        //Start activity StatisticsPair
        Intent intent = new Intent(this, StatisticsDuelActivity.class);
        intent.putExtra("name1", name1);
        intent.putExtra("name2", name2);
        Log.d("MY_LOG", "FINISHED GAME: name1: " + name1 + ", name2: " + name2);
        this.startActivity(intent);
    }

    private void updatedDatabaseForThisGame(String name1, String name2, int score1, int score2) {
        Log.d("MY_LOG", "here database update starts");
        String whoWon = "";
        if (score1 > score2)
            whoWon = name1;
        else if (score2 > score1)
            whoWon = name2;

        mViewModel.updatePairWin(name1, name2, whoWon);
        mViewModel.insertGame(new Game(name1, name2, score1, score2, new Date()));
        Log.d("MY_LOG", "here database update ends");
    }
    public void playHitSound(){
        golf_hit.start();
    }
    public int getBackgorundImageId() {
        return StaticValues.field_res[settings_fieldType % StaticValues.field_res.length];
    }

    //===============================================================================================
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

    public void setEndMessage(final int player_wins) {
        endMessage.post(new Runnable() {
            @Override
            public void run() {
                endMessage.setVisibility(View.VISIBLE);
                switch (player_wins) {
                    case -1:
                        endMessage.setText("Game draw!");
                        break;
                    case 0:
                        endMessage.setText(name1 + " wins!");
                        break;
                    case 1:
                        endMessage.setText(name2 + " wins!");
                        break;
                }
            }
        });


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

    public boolean isBot(int player)
    {
        if (player==0 && bot1) return true;
        if (player==1 && bot2) return true;
        return false;
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

    public boolean isResume_game() {
        return resume_game;
    }

    public void setResume_game(boolean resume_game) {
        this.resume_game = resume_game;
    }

    public CustomImageView getmCustomImageView() {
        return mCustomImageView;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MY_LOG",">>onSaveInstanceState");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MY_LOG",">>onPause");
        mCustomImageView.getGameData().saveAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MY_LOG",">>onResume");
    }
}