package com.example.markohudomal.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.database.Pair;
import com.example.markohudomal.pocketsoccer.database.model.BundleAwareViewModelFactory;
import com.example.markohudomal.pocketsoccer.database.model.MyViewModel;
import com.example.markohudomal.pocketsoccer.database.view.GameAdapter;
import com.example.markohudomal.pocketsoccer.database.view.PairAdapter;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private PairAdapter mAdapter;
    private MyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new PairAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewModelProvider provider = ViewModelProviders.of(this);
        BundleAwareViewModelFactory<MyViewModel> factory = new BundleAwareViewModelFactory<>(savedInstanceState, provider);
        mViewModel = factory.create(MyViewModel.class);
//      ViewModelProviders.of(this).get(MyViewModel.class);
        mViewModel.getAllPairs().observe(this, new Observer<List<Pair>>() {
            @Override
            public void onChanged(@Nullable final List<Pair> pairs) {
                mAdapter.setPairs(pairs);
            }
        });
    }

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
            mViewModel.deleteAllGames();
            mViewModel.deleteAllPlayers();
            mViewModel.deleteAllPairs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
