package com.example.markohudomal.pocketsoccer.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface PairDao {
    //INSERT----------------------------------------------------------------------------------------
    @Insert
    void insertPair(Pair pair);
    //GET-------------------------------------------------------------------------------------------
    @Query("SELECT * FROM pair_table LIMIT 1")
    Pair[] getAnyPair();

    @Query("SELECT * FROM pair_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    Pair getPairByNames(String name1,String name2);
    @Query("SELECT * FROM pair_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    LiveData<Pair> getLivePairByNames(String name1,String name2);

    @Query("SELECT * FROM pair_table")
    LiveData<List<Pair>> getAllPairs();

    @Query("SELECT COUNT(*) FROM pair_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    int doesPairExists(String name1,String name2);
    //UPDATE----------------------------------------------------------------------------------------
    @Update
    void updatePair(Pair pair);
    //DELETE----------------------------------------------------------------------------------------
    @Query("DELETE FROM pair_table WHERE (name1 = :name1 AND name2=:name2) OR (name1=:name2 AND name2 =:name1)")
    void deletePairs(String name1,String name2);

    @Query("DELETE FROM pair_table")
    void deleteAllPairs();


    //NotUsed=======================================================================================
    @Query("SELECT * FROM pair_table WHERE id = :id")
    Pair getPairById(int id);
    @Delete
    void deletePair(Pair pair);


}
