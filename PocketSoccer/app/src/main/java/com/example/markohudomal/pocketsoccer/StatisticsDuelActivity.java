package com.example.markohudomal.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.database.Pair;
import com.example.markohudomal.pocketsoccer.database.model.BundleAwareViewModelFactory;
import com.example.markohudomal.pocketsoccer.database.model.MyViewModel;
import com.example.markohudomal.pocketsoccer.database.view.GameAdapter;
import com.example.markohudomal.pocketsoccer.database.view.PairAdapter;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.util.List;

public class StatisticsDuelActivity extends AppCompatActivity {

    private GameAdapter mAdapter;
    private MyViewModel mViewModel;

    private String name1;
    private String name2;


    //Views
    private ConstraintLayout layerBottom;
    private TextView textBottomName1;
    private TextView textBottomName2;
    private TextView textBottomScore;
    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_duel);

        Intent intent = getIntent();
        name1=intent.getStringExtra("name1");
        name2=intent.getStringExtra("name2");
        //wins1=intent.getIntExtra("wins1",0);
        //wins2=intent.getIntExtra("wins2",0);
        this.setTitle(name1+" vs "+name2);

        //Views
        layerBottom = findViewById(R.id.constraintLayoutBottom);
        textBottomName1=findViewById(R.id.textView1Bottom);
        textBottomName2=findViewById(R.id.textView2Bottom);
        textBottomScore=findViewById(R.id.textViewScoreBottom);

        //RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new GameAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ViewModel
        ViewModelProvider provider = ViewModelProviders.of(this);
        BundleAwareViewModelFactory<MyViewModel> factory = new BundleAwareViewModelFactory<>(savedInstanceState, provider);
        mViewModel = factory.create(MyViewModel.class);
//      ViewModelProviders.of(this).get(MyViewModel.class);

        mViewModel.getGamesByNames(name1,name2).observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable final List<Game> games) {
                mAdapter.setGames(games);
            }
        });
        mViewModel.getPairByNames(name1,name2).observe(this, new Observer<Pair>() {
            @Override
            public void onChanged(@Nullable Pair pair) {
                //set Bottom layer
                if (pair!=null)
                {
                    textBottomName1.setText(pair.getName1());
                    textBottomName2.setText(pair.getName2());
                    textBottomScore.setText(pair.getWins1()+" : "+pair.getWins2());

                    if (pair.getWins1()>pair.getWins2())
                    {
                        textBottomName1.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER1));
                        textBottomName2.setTextColor(Color.WHITE);
                    }else if (pair.getWins2()>pair.getWins1()){
                        textBottomName2.setTextColor(Color.parseColor(StaticValues.COLOR_WINNER1));
                        textBottomName1.setTextColor(Color.WHITE);
                    }

                }else
                {
                    textBottomName1.setText("");
                    textBottomName2.setText("");
                    textBottomScore.setText("    ");
                }
            }
        });

    }//onCreate


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_all) {
            mViewModel.deletePairs(name1,name2);
            mViewModel.deleteGames(name1,name2);
            //layerBottom.setVisibility(View.GONE);
            layerBottom.setAlpha((float)0.2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
