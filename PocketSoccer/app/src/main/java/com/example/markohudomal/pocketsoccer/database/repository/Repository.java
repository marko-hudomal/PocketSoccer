package com.example.markohudomal.pocketsoccer.database.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import com.example.markohudomal.pocketsoccer.database.*;
import com.example.markohudomal.pocketsoccer.database.GameDao;
import com.example.markohudomal.pocketsoccer.database.MyRoomDatabase;
import com.example.markohudomal.pocketsoccer.database.PairDao;
import com.example.markohudomal.pocketsoccer.database.Player;
import com.example.markohudomal.pocketsoccer.database.PlayerDao;

import java.util.List;

public class Repository {

    private GameDao mGameDao;
    private PlayerDao mPlayerDao;
    private PairDao mPairDao;

    //Used
    private LiveData<List<Pair>> mAllPairs;
    private LiveData<List<Game>> mDuelGames;

    private LiveData<List<Player>> mAllPlayers;
    private LiveData<List<Game>> mAllGames;


    public Repository(Application application) {
        MyRoomDatabase database = MyRoomDatabase.getDatabase(application);

        mPlayerDao = database.playerDao();
        mGameDao = database.gameDao();
        mPairDao = database.pairDao();

        mAllPlayers = mPlayerDao.getAllPlayers();
        mAllGames = mGameDao.getAllGames();

        //Used
        mAllPairs = mPairDao.getAllPairs();
        mDuelGames = mGameDao.getAllGames();
    }
    //GAME TABLE------------------------------------------------------------------------------------
    //One Game
    public void insertGame(final Game game) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.insertGame(game);
                Log.d("MY_LOG","[3] Game: "+game.getName1()+"-"+game.getName2()+" is inserted");
            }
        }).start();
    }
    public void deleteGame(final Game game) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteGame(game);
            }
        }).start();
    }

    //Some Games
    public LiveData<List<Game>> getGamesByNames(String name1,String name2){

        return mGameDao.getGamesByPair(name1,name2);
    }
    public void deleteGames(final String name1,final String name2)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteGames(name1,name2);
            }
        }).start();
    }

    //All Games
    public LiveData<List<Game>> getAllGames() {
        return mAllGames;
    }
    public void deleteAllGames() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mGameDao.deleteAllGames();
            }
        }).start();
    }


    //PLAYER TABLE----------------------------------------------------------------------------------
    //One Player
    public void insertPlayer(final Player player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayerDao.insertPlayer(player);
            }
        }).start();
    }
    public void deletePlayer(final Player player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayerDao.deletePlayer(player);
            }
        }).start();
    }
    //All Players
    public LiveData<List<Player>> getAllPlayers() {
        return mAllPlayers;
    }
    public void deleteAllPlayers() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPlayerDao.deleteAllPlayers();
            }
        }).start();
    }

    //PAIR TABLE----------------------------------------------------------------------------------
    //One Pair
    public void insertNewPair(final String name1,final String name2)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int num = mPairDao.doesPairExists(name1,name2);
                if (num==0) {
                    mPairDao.insertPair(new Pair(name1, name2, 0, 0));
                    Log.d("MY_LOG","[1] Pair: "+name1+"-"+name2+" is inserted");
                }else
                {
                    Log.d("MY_LOG","[1] Pair: "+name1+"-"+name2+" already exists.");
                }

            }
        }).start();
    }
    public void insertPair(final Pair pair) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPairDao.insertPair(pair);
            }
        }).start();
    }
    public void updatePairWin(final String name1,final String name2,final String whoWon){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int num = mPairDao.doesPairExists(name1,name2);
                if (num==0) {
                    mPairDao.insertPair(new Pair(name1, name2, 0, 0));
                    Log.d("MY_LOG","[1] Pair: "+name1+"-"+name2+" is inserted");
                }else
                {
                    Log.d("MY_LOG","[1] Pair: "+name1+"-"+name2+" already exists.");
                }
                Pair pair = mPairDao.getPairByNames(name1,name2);
                if (whoWon.equals(pair.getName1()))
                {
                    pair.setWins1(pair.getWins1()+1);
                    mPairDao.updatePair(pair);
                    Log.d("MY_LOG","[2] Pair: "+name1+"-"+name2+" is updated with W1");
                }else
                if(whoWon.equals(pair.getName2()))
                {
                    pair.setWins2(pair.getWins2()+1);
                    mPairDao.updatePair(pair);
                    Log.d("MY_LOG","[2] Pair: "+name1+"-"+name2+" is updated with W2");
                }else
                {
                    //Draw result.
                    Log.d("MY_LOG","[2] Pair: "+name1+"-"+name2+" is updated with DRAW:"+whoWon);
                }


            }
        }).start();
    }
    public LiveData<Pair> getLivePairByNames(final String name1,final String name2)
    {
       return mPairDao.getLivePairByNames(name1,name2);
    }
    public void deletePair(final Pair pair) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPairDao.deletePair(pair);
            }
        }).start();
    }
    //Some pairs
    public void deletePairs(final String name1,final String name2)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPairDao.deletePairs(name1,name2);
            }
        }).start();
    }
    //All Pairs
    public LiveData<List<Pair>> getAllPairs() {
        return mAllPairs;
    }
    public void deleteAllPairs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPairDao.deleteAllPairs();
            }
        }).start();
    }

}

