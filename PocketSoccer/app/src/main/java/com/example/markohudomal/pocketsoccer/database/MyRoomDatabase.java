package com.example.markohudomal.pocketsoccer.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

@Database(entities = {Player.class,Game.class,Pair.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {
    private static MyRoomDatabase singletonInstance;

    public abstract PlayerDao playerDao();
    public abstract GameDao gameDao();
    public abstract PairDao pairDao();

    public static MyRoomDatabase getDatabase(final Context context) {
        if (singletonInstance == null) {
            synchronized (MyRoomDatabase.class) {
                if (singletonInstance == null) {
                    singletonInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyRoomDatabase.class,
                            "my_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return singletonInstance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(singletonInstance).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final PlayerDao playerDao;
        private final GameDao gameDao;
        private final PairDao pairDao;

        Date[] dates = {new Date(), new Date(), new Date()};
        String[] names = {"Marko", "Uros", "Mihailo"};

        PopulateDbAsync(MyRoomDatabase db) {
            playerDao = db.playerDao();
            gameDao = db.gameDao();
            pairDao = db.pairDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if ((gameDao.getAnyGame().length < 1)&&(pairDao.getAnyPair().length < 1)) {

                //1vs2
                gameDao.insertGame(new Game(names[0],names[1],3,2,new Date()));
                gameDao.insertGame(new Game(names[0],names[1],1,0,new Date()));
                pairDao.insertPair(new Pair(names[0],names[1],2,0));

                //1vs3
                gameDao.insertGame(new Game(names[0],names[2],1,5,new Date()));
                gameDao.insertGame(new Game(names[2],names[0],1,2,new Date()));
                pairDao.insertPair(new Pair(names[0],names[2],1,1));
            }
            return null;
        }
    }
}
