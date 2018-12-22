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

@Database(entities = {Order.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends RoomDatabase {
    private static MyRoomDatabase singletonInstance;

    public abstract OrderDao orderDao();

    public static MyRoomDatabase getDatabase(final Context context) {
        if (singletonInstance == null) {
            synchronized (MyRoomDatabase.class) {
                if (singletonInstance == null) {
                    singletonInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyRoomDatabase.class,
                            "my_database")
                            .fallbackToDestructiveMigration() // ako je vec pa ima neku bazu popunjenu kako ce da se migriraju podaci
                            //.allow i neki kurac koji dozvoljava da ide iz main thread kveri al ako dugo traje ne vlaja mrtav interfejs
                            .addCallback(sRoomDatabaseCallback) //dodavanje objekta koji moze da se izvrsi
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

        private final OrderDao mDao;
        Date[] dates = {new Date(), new Date(), new Date()};
        String[] names = {"Prva", "Druga", "Treca"};

        PopulateDbAsync(MyRoomDatabase db) {
            mDao = db.orderDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (mDao.getAnyOrder().length < 1) {
                for (int i = 0; i < names.length; i++) {
                    Order order = new Order(names[i], dates[i]);
                    mDao.insertOrder(order);
                }
            }
            return null;
        }
    }
}
