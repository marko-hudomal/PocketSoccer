package com.example.markohudomal.pocketsoccer;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.database.model.BundleAwareViewModelFactory;
import com.example.markohudomal.pocketsoccer.database.model.MyViewModel;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.util.Date;

public class GameActivity extends AppCompatActivity{




    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

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
    private int score1=0;
    private int score2=0;
    //------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //All values----------------------------------------------------------------------------------------------
        //SharedPreferences=======================================================================================
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        int set = sharedPreferences.getInt("values_set", -1);
        switch(set)
        {
            //Default values
            case 0:
                settings_fieldType=sharedPreferences.getInt("default_fieldType", StaticValues.Default_fieldType);
                settings_gameEndType=sharedPreferences.getInt("default_gameEndType", StaticValues.Default_gameEndType);
                settings_gameEndVal=sharedPreferences.getInt("default_gameEndVal", StaticValues.Default_gameEndVal);
                settings_gameSpeed=sharedPreferences.getInt("default_gameSpeed", StaticValues.Default_gameSpeed);
                break;
            //Custom values
            case 1:
                settings_fieldType=sharedPreferences.getInt("fieldType", StaticValues.Default_fieldType);
                settings_gameEndType=sharedPreferences.getInt("gameEndType", StaticValues.Default_gameEndType);
                settings_gameEndVal=sharedPreferences.getInt("gameEndVal", StaticValues.Default_gameEndVal);
                settings_gameSpeed=sharedPreferences.getInt("gameSpeed", StaticValues.Default_gameSpeed);
                break;
            //Error states
            default:
                settings_fieldType=StaticValues.Default_fieldType;
                settings_gameEndType=StaticValues.Default_gameEndType;
                settings_gameEndVal=StaticValues.Default_gameEndVal;
                settings_gameSpeed=StaticValues.Default_gameSpeed;
        }
        Log.d("MY_LOG","SP VALUES!---------------------------");
        Log.d("MY_LOG","settings_fieldType: "+settings_fieldType);
        Log.d("MY_LOG","setting_gameEndType: "+settings_gameEndType);
        Log.d("MY_LOG","setting_gameEndVal: "+settings_gameEndVal);
        Log.d("MY_LOG","setting_gameSpeed: "+settings_gameSpeed);
        Log.d("MY_LOG","----------------------------------------");
        //INTENT====================================================================================================
        Intent intent = getIntent();
        name1=intent.getStringExtra("start_name1");
        name2=intent.getStringExtra("start_name2");
        bot1=intent.getBooleanExtra("start_bot1",false);
        bot2=intent.getBooleanExtra("start_bot2",false);
        flag1=intent.getIntExtra("start_flag1",0);
        flag2=intent.getIntExtra("start_flag2",1);

        Log.d("MY_LOG","INTENT VALUES!---------------------------");
        Log.d("MY_LOG","name1: "+name1);
        Log.d("MY_LOG","name2: "+name2);
        Log.d("MY_LOG","bot1: "+bot1);
        Log.d("MY_LOG","bot2: "+bot2);
        Log.d("MY_LOG","flag1: "+flag1);
        Log.d("MY_LOG","flag2: "+flag2);
        Log.d("MY_LOG","----------------------------------------");
        //===========================================================================================================

        //Initalization==================================================================================================================================
        //ModelView
        ViewModelProvider provider = ViewModelProviders.of(this);
        BundleAwareViewModelFactory<MyViewModel> factory = new BundleAwareViewModelFactory<>(savedInstanceState, provider);
        mViewModel = factory.create(MyViewModel.class);




        //==========================================================================================

        //==========================================================================================
    }
    public void TEST_saveVal(View view)
    {
        try {
            score1=Integer.parseInt(((EditText)findViewById(R.id.edit_test_1)).getText().toString());
            score2=Integer.parseInt(((EditText)findViewById(R.id.edit_test_2)).getText().toString());
            Toast.makeText(GameActivity.this, "results saved "+score2, Toast.LENGTH_SHORT).show();
        }catch (Exception e){}
    }
    public void onGameEnd(View view)
    {
        Log.d("MY_LOG","rezultat 1: "+score1);
        Log.d("MY_LOG","rezultat 2: "+score2);

        //Database update!
        updatedDatabaseForThisGame(name1,name2,score1,score2);

        //Start activity StatisticsPair
        Intent intent = new Intent(this,StatisticsDuelActivity.class);
        intent.putExtra("name1",name1);
        intent.putExtra("name2",name2);
        Log.d("MY_LOG","FINISHED GAME: name1: "+name1+", name2: "+name2);
        this.startActivity(intent);
    }

    private void updatedDatabaseForThisGame(String name1,String name2,int score1,int score2)
    {
        Log.d("MY_LOG","here database update starts");
        String whoWon="";
        if (score1>score2)
            whoWon=name1;
        else if (score2>score1)
            whoWon=name2;

        mViewModel.updatePairWin(name1,name2,whoWon);
        mViewModel.insertGame(new Game(name1,name2,score1,score2,new Date()));
        Log.d("MY_LOG","here database update ends");
    }

    public int getBackgorundImageId()
    {
        return StaticValues.field_res[settings_fieldType % StaticValues.field_res.length];
    }
}
