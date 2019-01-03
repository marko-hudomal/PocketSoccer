package com.example.markohudomal.pocketsoccer.extras;

import android.media.MediaPlayer;

import com.example.markohudomal.pocketsoccer.R;

public class StaticValues {
    public static int Default_fieldType=0;//Grass field
    public static int Default_gameEndType=0;//Time
    public static int Default_gameEndVal=30;//30 sec
    public static int Default_gameSpeed=1;//Speed 1x

    public static String COLOR_WINNER1="#ffa700";
    public static String COLOR_WINNER2="#ffa700";

    //fieldType
    public static int field_res[]={R.drawable.field_grass,R.drawable.field_concrete,R.drawable.field_wood};
    //flagType
    public static int flag_res[]={R.drawable.flag_serbia,R.drawable.flag_lithuania,R.drawable.flag_italy,R.drawable.flag_france,R.drawable.flag_russia};



    //Game
    public static int ball_res[] = {R.drawable.ball_serbia,R.drawable.ball_lithuania,R.drawable.ball_italy,R.drawable.ball_france,R.drawable.ball_russia};
    public static int SecondsTurn=5;
    public static String colorWhoPlays="#ffc04c";
    public static int endPause=3;

    public static float speedChange=0.1f;
    public static int refreshRate=10;
    public static float scaleValue=18.0f;
    public static float limitSpeed=40;


    public static int playerSizeScale=6;
    public static int footballSizeScale=10;

    public static float myVectorDecipation=0.75f;
    public static float otherVectorDecipation=0.25f;
}
