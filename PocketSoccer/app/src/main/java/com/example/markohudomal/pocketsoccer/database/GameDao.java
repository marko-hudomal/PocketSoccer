package com.example.markohudomal.pocketsoccer.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface GameDao {
    @Insert
    void insertGame(Game game);

    @Query("SELECT * FROM game_table")
    LiveData<List<Game>> getAllGames(); // ako je povratna vrednost livedata obezbedjuje se zasebna nit

    @Query("SELECT * FROM game_table LIMIT 1")
    Game[] getAnyGame();

    @Query("SELECT * FROM game_table WHERE name1 = :name1")
    LiveData<List<Game>> getGamesByName1(String name1);
    @Query("SELECT * FROM game_table WHERE name2 = :name2")
    LiveData<List<Game>> getGamesByName2(String name2);
    @Query("SELECT * FROM game_table WHERE name1 = :name OR name2 =:name")
    LiveData<List<Game>> getGamesByName(String name);
    @Query("SELECT * FROM game_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    LiveData<List<Game>> getGamesByPair(String name1,String name2);

    @Query("DELETE FROM game_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    void deleteGames(String name1,String name2);

    @Delete
    void deleteGame(Game game);

    @Query("DELETE FROM game_table")
    void deleteAllGames();
}
