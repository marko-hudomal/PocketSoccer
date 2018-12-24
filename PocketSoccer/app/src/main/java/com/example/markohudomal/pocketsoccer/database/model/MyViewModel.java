package com.example.markohudomal.pocketsoccer.database.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;


import com.example.markohudomal.pocketsoccer.database.Game;
import com.example.markohudomal.pocketsoccer.database.Pair;
import com.example.markohudomal.pocketsoccer.database.Player;
import com.example.markohudomal.pocketsoccer.database.repository.Repository;

import java.util.List;

public class MyViewModel extends BundleAwareViewModel {
    private Repository mRepository;

    private LiveData<List<Player>> mAllPlayers;
    private LiveData<List<Game>> mAllGames;
    private LiveData<List<Pair>> mAllPairs;

    public MyViewModel(Application application) {
        super(application);
        mRepository = new Repository(application);
        mAllPlayers = mRepository.getAllPlayers();
        mAllGames = mRepository.getAllGames();
        mAllPairs = mRepository.getAllPairs();
    }

    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }
    public LiveData<List<Game>> getAllGames() {
        return mAllGames;
    }



    //Used
    public void insertNewPair(String name1,String name2){mRepository.insertNewPair(name1,name2);}
    public void updatePairWin(String name1,String name2,String whoWon){mRepository.updatePairWin(name1,name2,whoWon);}
    public void insertGame(Game game){mRepository.insertGame(game);}

    public LiveData<Pair> getPairByNames(String name1,String name2){return mRepository.getLivePairByNames(name1,name2);}

    public LiveData<List<Pair>> getAllPairs() {
        return mAllPairs;
    }
    public LiveData<List<Game>> getGamesByNames(String name1,String name2){return mRepository.getGamesByNames(name1,name2);}
    public void deleteAllPlayers() {
        mRepository.deleteAllPlayers();
    }
    public void deleteAllGames() {
        mRepository.deleteAllGames();
    }
    public void deleteAllPairs() {
        mRepository.deleteAllPairs();
    }
    public void deletePairs(String name1,String name2){mRepository.deletePairs(name1,name2);}
    public void deleteGames(String name1,String name2){mRepository.deleteGames(name1,name2);}
    @Override
    public void writeTo(Bundle bundle) {

    }

    @Override
    public void readFrom(Bundle bundle) {

    }
}
