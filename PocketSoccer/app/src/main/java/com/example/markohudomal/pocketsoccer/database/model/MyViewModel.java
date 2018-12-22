package com.example.markohudomal.pocketsoccer.database.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;


import com.example.markohudomal.pocketsoccer.database.Order;
import com.example.markohudomal.pocketsoccer.database.repository.Repository;

import java.util.List;

public class MyViewModel extends BundleAwareViewModel {
    private Repository mRepository;

    private LiveData<List<Order>> mAllOrders;

    public MyViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllOrders = mRepository.getAllOrders();
    }

    public void insertOrder(Order order) {
        mRepository.insertOrder(order);
    }

    public LiveData<List<Order>> getAllOrders() {
        return mAllOrders;
    }

    public void deleteOrder(Order order) {
        mRepository.deleteOrder(order);
    }

    public void deleteAllOrders() {
        mRepository.deleteAllOrders();
    }

    @Override
    public void writeTo(Bundle bundle) {

    }

    @Override
    public void readFrom(Bundle bundle) {

    }
}
