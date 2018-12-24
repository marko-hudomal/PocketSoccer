package com.example.markohudomal.pocketsoccer.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "pair_table")
public class Pair {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name1;
    private String name2;
    private int wins1;
    private int wins2;

    public Pair(String name1, String name2, int wins1, int wins2) {
        this.name1 = name1;
        this.name2 = name2;
        this.wins1 = wins1;
        this.wins2 = wins2;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getWins1() {
        return wins1;
    }

    public void setWins1(int wins1) {
        this.wins1 = wins1;
    }

    public int getWins2() {
        return wins2;
    }

    public void setWins2(int wins2) {
        this.wins2 = wins2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
