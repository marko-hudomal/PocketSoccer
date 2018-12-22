package com.example.markohudomal.pocketsoccer.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertOrder(Order order);

    @Query("SELECT * FROM order_table")
    LiveData<List<Order>> getAllOrders(); // ako je povratna vrednost livedata obezbedjuje se zasebna nit

    @Query("SELECT * FROM order_table LIMIT 1")
    Order[] getAnyOrder();

    @Query("SELECT * FROM order_table WHERE id = :id")
    Order getOrderById(int id);

    @Delete
    void deleteOrder(Order order);

    @Query("DELETE FROM order_table")
    void deleteAllOrders();
}
