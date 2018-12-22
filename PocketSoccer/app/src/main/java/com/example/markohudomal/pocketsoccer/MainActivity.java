package com.example.markohudomal.pocketsoccer;

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
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));
    }
}
