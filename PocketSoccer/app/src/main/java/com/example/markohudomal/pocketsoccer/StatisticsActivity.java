package com.example.markohudomal.pocketsoccer;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.markohudomal.pocketsoccer.database.Order;
import com.example.markohudomal.pocketsoccer.database.model.BundleAwareViewModelFactory;
import com.example.markohudomal.pocketsoccer.database.model.MyViewModel;
import com.example.markohudomal.pocketsoccer.database.view.OrderAdapter;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private OrderAdapter mAdapter;
    private MyViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);



        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new OrderAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewModelProvider provider = ViewModelProviders.of(this);
        BundleAwareViewModelFactory<MyViewModel> factory = new BundleAwareViewModelFactory<>(savedInstanceState, provider);
        mViewModel = factory.create(MyViewModel.class);
//      ViewModelProviders.of(this).get(MyViewModel.class);
        mViewModel.getAllOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(@Nullable final List<Order> orders) {
                mAdapter.setOrders(orders);
            }
        });
    }
}
