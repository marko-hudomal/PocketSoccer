package com.example.markohudomal.pocketsoccer.game.gamedata;

public class EnvironmentData {

    public EnvironmentData(int player_turn, int goals1, int goals2, int seconds_turn, int endVal, String name1, String name2, boolean bot1, boolean bot2, int flag1, int flag2, int settings_fieldType, int settings_gameEndType, int settings_gameEndVal, int settings_gameSpeed) {
        this.player_turn = player_turn;
        this.goals1 = goals1;
        this.goals2 = goals2;
        this.seconds_turn = seconds_turn;
        this.endVal = endVal;
        this.name1 = name1;
        this.name2 = name2;
        this.bot1 = bot1;
        this.bot2 = bot2;
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.settings_fieldType = settings_fieldType;
        this.settings_gameEndType = settings_gameEndType;
        this.settings_gameEndVal = settings_gameEndVal;
        this.settings_gameSpeed = settings_gameSpeed;
    }

    //GameData
    public int player_turn;
    public int goals1;
    public int goals2;
    public int seconds_turn;
    public int endVal;

    //GameActivity
    public String name1;
    public String name2;
    public boolean bot1;
    public boolean bot2;
    public int flag1;
    public int flag2;
    public int settings_fieldType;
    public int settings_gameEndType;
    public int settings_gameEndVal;
    public int settings_gameSpeed;

    @Override
    public String toString() {
        return "EnvironmentData{" +
                "player_turn=" + player_turn +
                ", goals1=" + goals1 +
                ", goals2=" + goals2 +
                ", seconds_turn=" + seconds_turn +
                ", endVal=" + endVal +
                ", name1='" + name1 + '\'' +
                ", name2='" + name2 + '\'' +
                ", bot1=" + bot1 +
                ", bot2=" + bot2 +
                ", flag1=" + flag1 +
                ", flag2=" + flag2 +
                ", settings_fieldType=" + settings_fieldType +
                ", settings_gameEndType=" + settings_gameEndType +
                ", settings_gameEndVal=" + settings_gameEndVal +
                ", settings_gameSpeed=" + settings_gameSpeed +
                '}';
    }
}
