package com.example.markohudomal.pocketsoccer;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private static int field_res[]={R.drawable.field_grass,R.drawable.field_concrete,R.drawable.field_wood};
    private static int field_res_iter=0;


    //Views
    private ImageSwitcher imageField;

    private RadioGroup radioGroup;
    private int progress_last=0;
    private TextView seekBarEndGameText;
    private SeekBar seekBarEndGame;

    private TextView seekBarSpeedText;
    private SeekBar seekBarSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        //ImageSwitcher
        imageField = findViewById(R.id.imageField);
        imageField.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });
        imageField.setImageResource(field_res[field_res_iter]);
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        imageField.setInAnimation(in);
        imageField.setOutAnimation(out);

        //GameEnd
        radioGroup=findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NewApi")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radio_time:
                        Toast.makeText(SettingsActivity.this, "Time", Toast.LENGTH_SHORT).show();
                        seekBarEndGame.setThumb(getDrawable(R.drawable.ic_timer));

                        seekBarEndGameText.setText((progress_last+10)+" sec");
                        break;
                    case R.id.radio_score:
                        Toast.makeText(SettingsActivity.this, "Score", Toast.LENGTH_SHORT).show();
                        seekBarEndGameText.setText((progress_last/11+1)+" goals");
                        seekBarEndGame.setThumb(getDrawable(R.drawable.ic_ball));
                        break;
                    default:
                        Toast.makeText(SettingsActivity.this, "Oh no!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        seekBarEndGameText=findViewById(R.id.text_seekBarGameEnd);
        seekBarEndGame=findViewById(R.id.seekBarGameEnd);
        seekBarEndGame.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    progress_last=progress;
                    if (radioGroup.getCheckedRadioButtonId()==R.id.radio_time)
                    {
                        seekBarEndGameText.setText((progress+10)+" sec");
                    }else
                    {
                        seekBarEndGameText.setText((progress/11+1)+" goals");
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //Game speed
        seekBarSpeedText=findViewById(R.id.text_seekBarSpeed);
        seekBarSpeed=findViewById(R.id.seekBarSpeed);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    seekBarSpeedText.setText((progress+1)+"x");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onFabLeftClick(View view)
    {

        field_res_iter=(field_res_iter+field_res.length-1) % field_res.length;
        imageField.setImageResource(field_res[field_res_iter]);

    }
    public void onFabRightClick(View view)
    {

        field_res_iter=(field_res_iter+1) % field_res.length;
        imageField.setImageResource(field_res[field_res_iter]);
    }

    public void setTimeProgress(int val){
        radioGroup.check(R.id.radio_time);
        seekBarEndGameText.setText(val +" sec");
        seekBarEndGame.setProgress((val-10));
        //icon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            seekBarEndGame.setThumb(getDrawable(R.drawable.ic_timer));
        }
    }
    public void setScoreProgress(int val){
        radioGroup.check(R.id.radio_score);
        seekBarEndGameText.setText(val +" goals");
        seekBarEndGame.setProgress((val-1)*11);
        //icon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            seekBarEndGame.setThumb(getDrawable(R.drawable.ic_ball));
        }
    }
}
