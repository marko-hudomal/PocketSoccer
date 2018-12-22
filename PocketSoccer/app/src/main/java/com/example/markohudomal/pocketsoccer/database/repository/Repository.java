package com.example.markohudomal.pocketsoccer.database.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;


import com.example.markohudomal.pocketsoccer.database.MyRoomDatabase;
import com.example.markohudomal.pocketsoccer.database.Order;
import com.example.markohudomal.pocketsoccer.database.OrderDao;

import java.util.List;

public class Repository {
    private OrderDao mOrderDao;
    private LiveData<List<Order>> mAllOrders;

    public Repository(Application application) {
        MyRoomDatabase database = MyRoomDatabase.getDatabase(application);
        mOrderDao = database.orderDao();
        mAllOrders = mOrderDao.getAllOrders();
    }

    public void insertOrder(final Order order) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOrderDao.insertOrder(order);
            }
        }).start();
    }

    public LiveData<List<Order>> getAllOrders() {
        return mAllOrders;
    }

    public void deleteOrder(final Order order) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOrderDao.deleteOrder(order);
            }
        }).start();
    }

    public void deleteAllOrders() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mOrderDao.deleteAllOrders();
            }
        }).start();
    }
}

