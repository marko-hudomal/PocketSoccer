package com.example.markohudomal.pocketsoccer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private ImageView imageLogo;
    private CardView cardMenu;

    public MediaPlayer click;
    public MediaPlayer crowd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageLogo=findViewById(R.id.imageLogo);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        imageLogo.setAnimation(in);
        cardMenu=findViewById(R.id.cardMenu);
        cardMenu.setAnimation(in);

         click = MediaPlayer.create(this, R.raw.click3);
         crowd = MediaPlayer.create(this,R.raw.crowd);
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
            return;

        //Go next
        Intent intent = new Intent();

        switch (view.getId())
        {
            case R.id.card_start:
                intent.setClass(this,MainActivity.class);
                break;
            case R.id.card_resume:
                intent.setClass(this,MainActivity.class);
                break;
            case R.id.card_statistics:
                intent.setClass(this,StatisticsActivity.class);
                break;
            case R.id.card_settings:
                intent.setClass(this,SettingsActivity.class);
                break;
        }
        startActivity(intent);
    }
    public void onImageClick(View view)
    {
        crowd.start();
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_shake));
    }
}
