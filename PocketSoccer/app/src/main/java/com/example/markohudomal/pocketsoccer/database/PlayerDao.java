package com.example.markohudomal.pocketsoccer.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlayerDao {

    @Insert
    void insertPlayer(Player player);

    @Query("SELECT * FROM player_table")
    LiveData<List<Player>> getAllPlayers(); // ako je povratna vrednost livedata obezbedjuje se zasebna nit

    @Query("SELECT * FROM player_table LIMIT 1")
    Player[] getAnyPlayer();

    @Query("SELECT * FROM player_table WHERE name = :name")
    Player getPlayerByName(String name);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM player_table")
    void deleteAllPlayers();
}
