package com.example.markohudomal.pocketsoccer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    //fieldType
    private static int field_res_iter;

    //gameEndType
    private static int radio_res[]={R.id.radio_time,R.id.radio_score};

    //Views------------------------------
    //fieldType
    private ImageSwitcher imageField;
    //gameEnd
    private RadioGroup radioGroup;
    private int progress_last=0;
    private TextView seekBarEndGameText;
    private SeekBar seekBarEndGame;
    //gameSpeed
    private TextView seekBarSpeedText;
    private SeekBar seekBarSpeed;
    //-----------------------------------

    //MediaPlayer------------------------
    public MediaPlayer click;
    public MediaPlayer crowd;
    //-----------------------------------
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Media sounds
        click = MediaPlayer.create(this, R.raw.click3);
        crowd = MediaPlayer.create(this,R.raw.crowd);


        //ImageSwitcher-----------------------------------------------------------------------------
        imageField = findViewById(R.id.imageField);
        imageField.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        imageField.setInAnimation(in);
        imageField.setOutAnimation(out);

        //GameEnd-----------------------------------------------------------------------------------
        radioGroup=findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_time:
                        seekBarEndGameText.setText(convertProgressToTime(progress_last)+" sec");
                        seekBarEndGame.setThumb(getDrawable(R.drawable.ic_timer));
                        break;
                    case R.id.radio_score:
                        seekBarEndGameText.setText(convertProgressToScore(progress_last)+" goals");
                        seekBarEndGame.setThumb(getDrawable(R.drawable.ic_ball));
                        break;
                    default:
                        Toast.makeText(SettingsActivity.this, "Oh no! : "+checkedId, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        seekBarEndGameText=findViewById(R.id.text_seekBarGameEnd);
        seekBarEndGame=findViewById(R.id.seekBarGameEnd);
        seekBarEndGame.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    if (radioGroup.getCheckedRadioButtonId()==R.id.radio_time)
                    {
                        seekBarEndGameText.setText(convertProgressToTime(progress)+" sec");
                        progress_last=progress;
                    }else
                    {
                        seekBarEndGameText.setText(convertProgressToScore(progress)+" goals");
                        progress_last=progress;
                    }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //Game speed--------------------------------------------------------------------------------
        seekBarSpeedText=findViewById(R.id.text_seekBarSpeed);
        seekBarSpeed=findViewById(R.id.seekBarSpeed);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekBarSpeedText.setText((progress+1)+"x");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarEndGame.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));
        seekBarSpeed.startAnimation(AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left));
        //========================================================================================================
        //SharedPreferences=======================================================================================
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        int set = sharedPreferences.getInt("values_set", -1);
        int fieldType;
        int gameEndType;
        int gameEndVal;
        int gameSpeed;
        switch(set)
        {
            //Default values
            case 0:
                fieldType=sharedPreferences.getInt("default_fieldType", StaticValues.Default_fieldType);
                gameEndType=sharedPreferences.getInt("default_gameEndType", StaticValues.Default_gameEndType);
                gameEndVal=sharedPreferences.getInt("default_gameEndVal", StaticValues.Default_gameEndVal);
                gameSpeed=sharedPreferences.getInt("default_gameSpeed", StaticValues.Default_gameSpeed);
                Log.d("MY_LOG","def game speed restore: "+gameSpeed);
                break;
            //Custom values
            case 1:
                fieldType=sharedPreferences.getInt("fieldType", StaticValues.Default_fieldType);
                gameEndType=sharedPreferences.getInt("gameEndType", StaticValues.Default_gameEndType);
                gameEndVal=sharedPreferences.getInt("gameEndVal", StaticValues.Default_gameEndVal);
                gameSpeed=sharedPreferences.getInt("gameSpeed", StaticValues.Default_gameSpeed);
                break;
            default:
                fieldType=StaticValues.Default_fieldType;
                gameEndType=StaticValues.Default_gameEndType;
                gameEndVal=StaticValues.Default_gameEndVal;
                gameSpeed=StaticValues.Default_gameSpeed;
        }
        //SET VIEWS--------------------------------------------

        //fieldType
        field_res_iter=fieldType;
        imageField.setImageResource(StaticValues.field_res[field_res_iter]);

        //gameEnd
        radioGroup.check(radio_res[gameEndType]);
        if (gameEndType==0)
        {
            progress_last=convertTimeToProgress(gameEndVal);
        }
        else if (gameEndType==1)
        {
            progress_last=convertScoreToProgress(gameEndVal);
        }
        seekBarEndGame.setProgress(progress_last);

        //gameSpeed
        seekBarSpeed.setProgress(gameSpeed-1);
        seekBarSpeedText.setText(gameSpeed+"x");
        //-----------------------------------------------------
        //========================================================================================================
    }//onCreate




    public void onFabLeftClick(View view)
    {

        field_res_iter=(field_res_iter+StaticValues.field_res.length-1) % StaticValues.field_res.length;
        imageField.setImageResource(StaticValues.field_res[field_res_iter]);

    }
    public void onFabRightClick(View view)
    {

        field_res_iter=(field_res_iter+1) % StaticValues.field_res.length;
        imageField.setImageResource(StaticValues.field_res[field_res_iter]);
    }


    @SuppressLint("ApplySharedPref")
    public void onSaveClick(View view){
        //animation
        click.start();
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));

        //Saving settings
        editor = sharedPreferences.edit();
        editor.putInt("fieldType", field_res_iter);
        switch(radioGroup.getCheckedRadioButtonId())
        {
            case R.id.radio_time:
                editor.putInt("gameEndVal", convertProgressToTime(seekBarEndGame.getProgress()));
                Log.d("MY_LOG","Saved time: "+convertProgressToTime(seekBarEndGame.getProgress()));
                editor.putInt("gameEndType", 0);
                break;
            case R.id.radio_score:
                editor.putInt("gameEndVal", convertProgressToScore(seekBarEndGame.getProgress()));
                Log.d("MY_LOG","Saved score: "+convertProgressToScore(seekBarEndGame.getProgress()));
                editor.putInt("gameEndType", 1);
                break;
        }
        editor.putInt("gameSpeed", seekBarSpeed.getProgress()+1);
        editor.putInt("values_set",1);
        editor.commit();

        //Finish intent
        Intent intent = new Intent();
        intent.putExtra("return_message","Settings saved.");
        setResult(RESULT_OK,intent);
        finish();
    }

    @SuppressLint("ApplySharedPref")
    public void onResetClick(View view){
        //animation
        click.start();
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));

        //Restoring settings
        editor = sharedPreferences.edit();
        editor.putInt("values_set",0);
        editor.commit();

        //Finish intent
        Intent intent = new Intent();
        intent.putExtra("return_message","Settings set to default.");
        setResult(RESULT_OK,intent);
        finish();
    }


    public static int convertTimeToProgress(int time)
    {
        return time-10;
    }
    public static int convertProgressToTime(int progress)
    {
        return progress+10;
    }
    public static int convertScoreToProgress(int score)
    {
        return (score-1)*11;
    }
    public static int convertProgressToScore(int progress)
    {
        return (progress/11)+1;
    }
}
