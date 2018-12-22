package com.example.markohudomal.pocketsoccer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public void onItemClick(View view)
    {
        //Disabled button
        if (view.getAlpha()==1.0)
            view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));
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
}
