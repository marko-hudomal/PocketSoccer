package com.example.markohudomal.pocketsoccer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    public static final int START_GAME = 201;
    public static final int SETTINGS = 202;
    public static final int STATISTICS = 203;

    private ImageView imageLogo;
    private CardView cardMenu;

    public MediaPlayer click;
    public MediaPlayer crowd;

    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Image and CardView Menu Animations
        imageLogo=findViewById(R.id.imageLogo);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        imageLogo.setAnimation(in);
        cardMenu=findViewById(R.id.cardMenu);
        cardMenu.setAnimation(in);

        //Media sounds
        click = MediaPlayer.create(this, R.raw.click3);
        crowd = MediaPlayer.create(this,R.raw.crowd);

        //SharedPreferences
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        int set = sharedPreferences.getInt("values_set", -1);
        switch(set)
        {
            //Init default values
            case -1:
                editor = sharedPreferences.edit();
                editor.putInt("default_fieldType", StaticValues.Default_fieldType);
                editor.putInt("default_gameEndType", StaticValues.Default_gameEndType);
                editor.putInt("default_gameEndVal", StaticValues.Default_gameEndVal);
                editor.putInt("default_gameSpeed", StaticValues.Default_gameSpeed);
                editor.putInt("values_set",0);
                editor.commit();
                break;
            //Default values
            case 0:
                break;
            //Custom values
            case 1:
                break;
        }


    }

    public void onItemClick(View view)
    {
        //Disabled button
        if (view.getAlpha()==1.0)
        {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));
            click.start();
        }
        else
        {
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_shake));
            return;
        }

        //Go next
        Intent intent = new Intent();

        switch (view.getId())
        {
            case R.id.card_start:
                intent.setClass(this,StartActivity.class);
                startActivityForResult(intent,START_GAME);
                break;
            case R.id.card_resume:
                intent.setClass(this,MainActivity.class);
                startActivityForResult(intent,START_GAME);
                break;
            case R.id.card_statistics:
                intent.setClass(this,StatisticsActivity.class);
                startActivityForResult(intent,STATISTICS);
                break;
            case R.id.card_settings:
                intent.setClass(this,SettingsActivity.class);
                startActivityForResult(intent,SETTINGS);
                break;
        }

    }
    public void onImageClick(View view)
    {
        crowd.start();
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_shake));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        {
            switch(requestCode)
            {
                case SETTINGS:
                    String message = data.getStringExtra("return_message");
                    Snackbar.make(findViewById(R.id.imageLogo), message, Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
