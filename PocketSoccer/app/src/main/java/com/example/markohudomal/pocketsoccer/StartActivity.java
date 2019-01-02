package com.example.markohudomal.pocketsoccer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

public class StartActivity extends AppCompatActivity {

    //flagType
    private static int flag_res_iter1=0;
    private static int flag_res_iter2=0;


    private ImageSwitcher imageViewFlag1;
    private ImageSwitcher imageViewFlag2;

    private EditText editTextName1;
    private EditText editTextName2;
    private CheckBox checkBoxBot1;
    private CheckBox checkBoxBot2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageViewFlag1=findViewById(R.id.flag1);
        imageViewFlag2=findViewById(R.id.flag2);
        imageViewFlag1.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });
        imageViewFlag2.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,android.R.anim.fade_out);
        imageViewFlag1.setInAnimation(in);
        imageViewFlag1.setOutAnimation(out);
        imageViewFlag2.setInAnimation(in);
        imageViewFlag2.setOutAnimation(out);

        imageViewFlag1.setImageResource(StaticValues.flag_res[flag_res_iter1]);
        imageViewFlag2.setImageResource(StaticValues.flag_res[flag_res_iter2]);

        //EditText and Checkbox init
        editTextName1=findViewById(R.id.editName1);
        editTextName2=findViewById(R.id.editName2);
        checkBoxBot1=findViewById(R.id.checkName1);
        checkBoxBot2=findViewById(R.id.checkName2);
    }

    public void onStartGameClick(View view)
    {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_card_click));
        Intent intent = new Intent(this,GameActivity.class);
        String name1="Player1";
        String name2="Player2";
        if (!editTextName1.getText().toString().equals(""))
        {
            name1=editTextName1.getText().toString();
        }
        if (!editTextName2.getText().toString().equals(""))
        {
            name2=editTextName2.getText().toString();
        }
        intent.putExtra("start_name1",name1);
        intent.putExtra("start_name2",name2);
        intent.putExtra("start_bot1",checkBoxBot1.isChecked());
        intent.putExtra("start_bot2",checkBoxBot2.isChecked());
        intent.putExtra("start_flag1",flag_res_iter1);
        intent.putExtra("start_flag2",flag_res_iter2);
        startActivity(intent);
    }

    public void onFabL1(View view)
    {
        flag_res_iter1=(flag_res_iter1+StaticValues.flag_res.length-1) % StaticValues.flag_res.length;
        imageViewFlag1.setImageResource(StaticValues.flag_res[flag_res_iter1]);
    }
    public void onFabL2(View view)
    {
        flag_res_iter2=(flag_res_iter2+StaticValues.flag_res.length-1) % StaticValues.flag_res.length;
        imageViewFlag2.setImageResource(StaticValues.flag_res[flag_res_iter2]);
    }
    public void onFabR1(View view)
    {
        flag_res_iter1=(flag_res_iter1+1) % StaticValues.flag_res.length;
        imageViewFlag1.setImageResource(StaticValues.flag_res[flag_res_iter1]);
    }
    public void onFabR2(View view)
    {
        flag_res_iter2=(flag_res_iter2+1) % StaticValues.flag_res.length;
        imageViewFlag2.setImageResource(StaticValues.flag_res[flag_res_iter2]);
    }
}
