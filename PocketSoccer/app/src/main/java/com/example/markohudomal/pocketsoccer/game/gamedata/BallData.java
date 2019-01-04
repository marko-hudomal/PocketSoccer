package com.example.markohudomal.pocketsoccer.game.gamedata;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

public class BallData {

    public BallData(boolean selected, float radius, float mFigurePosition_x, float mFigurePosition_y, boolean moving, float d_speed, float vectorX, float vectorY) {
        this.selected = selected;
        this.radius = radius;
        this.mFigurePosition_x = mFigurePosition_x;
        this.mFigurePosition_y = mFigurePosition_y;
        this.moving = moving;
        this.d_speed = d_speed;
        this.vectorX = vectorX;
        this.vectorY = vectorY;
    }

    public boolean selected=false;

    public float radius;
    public float mFigurePosition_x;
    public float mFigurePosition_y;

    public boolean moving;
    public float d_speed;
    public float vectorX;
    public float vectorY;

    @Override
    public String toString() {
        return "BallData{" +
                "selected=" + selected +
                ", radius=" + radius +
                ", mFigurePosition_x=" + mFigurePosition_x +
                ", mFigurePosition_y=" + mFigurePosition_y +
                ", moving=" + moving +
                ", d_speed=" + d_speed +
                ", vectorX=" + vectorX +
                ", vectorY=" + vectorY +
                '}';
    }
}
