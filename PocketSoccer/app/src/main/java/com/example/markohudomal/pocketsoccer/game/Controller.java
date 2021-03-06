package com.example.markohudomal.pocketsoccer.game;


import android.graphics.RectF;
import android.util.Log;


import com.example.markohudomal.pocketsoccer.extras.StaticValues;
import com.example.markohudomal.pocketsoccer.game.gamedata.Ball;
import com.example.markohudomal.pocketsoccer.game.gamedata.GameData;

public class Controller {

    public interface ViewInterface {
        void updateView();
        void playHitSound();
        boolean isBot1();
        boolean isBot2();
    }


    private ViewInterface mViewInterface;

    private GameData mImageData;

    public int ball1_pressed=-1;
    public int ball2_pressed=-1;
    public float x_pressed=-1;
    public float y_pressed=-1;

    public Controller(ViewInterface viewInterface, GameData imageData) {
        mViewInterface = viewInterface;
        mImageData = imageData;

    }


    public void onFirstTouchDown(float x, float y) {
        ball1_pressed=-1;
        ball2_pressed=-1;
        if (mImageData.getPlayer_turn()==0 && !mViewInterface.isBot1())
        {
            for(int i=0;i<mImageData.getPlayer1_balls().size();i++)
            {
                Ball temp = mImageData.getPlayer1_balls().get(i);
                if (insideBounds(x,y,temp.mFigureHolder))
                {
                    ball1_pressed=i;
                    x_pressed=x;
                    y_pressed=y;
                    temp.setSelected(true);
                }
            }
        }else if (mImageData.getPlayer_turn()==1 && !mViewInterface.isBot2())
        {
            for(int i=0;i<mImageData.getPlayer2_balls().size();i++)
            {
                Ball temp = mImageData.getPlayer2_balls().get(i);
                if (insideBounds(x,y,temp.mFigureHolder))
                {
                    ball2_pressed=i;
                    x_pressed=x;
                    y_pressed=y;
                    temp.setSelected(true);
                }
            }
        }



    }

    public void onAllTouchesUp(float x, float y) {
        boolean result = false;
        if (ball1_pressed!=-1)
        {
            Ball temp = mImageData.getPlayer1_balls().get(ball1_pressed);
            if (mImageData.getPlayer_turn()==0 && !mViewInterface.isBot1())
            {
                //temp.setMoving(true);
                temp.vectorX=x-x_pressed;
                temp.vectorY=y-y_pressed;
                temp.scaleMyVector(StaticValues.scaleValue);
                temp.limitMyVector(StaticValues.limitSpeed);

                Log.d("MY_LOG","2speed set="+temp.getSpeed());
                mImageData.nextPlayer();
                //mViewInterface.playHitSound();
                mViewInterface.updateView();
            }
            temp.setSelected(false);
        }
        if (ball2_pressed!=-1)
        {
            Ball temp = mImageData.getPlayer2_balls().get(ball2_pressed);
            if (mImageData.getPlayer_turn()==1&& !mViewInterface.isBot2())
            {
                //temp.setMoving(true);
                temp.vectorX=x-x_pressed;
                temp.vectorY=y-y_pressed;
                temp.scaleMyVector(StaticValues.scaleValue);
                temp.limitMyVector(StaticValues.limitSpeed);

                Log.d("MY_LOG","2speed set="+temp.getSpeed());
                mImageData.nextPlayer();
                //mViewInterface.playHitSound();
                mViewInterface.updateView();
            }
            temp.setSelected(false);
        }
        //Log.d("MY_LOG","X: ["+x_pressed+","+x+"]; Y: ["+y_pressed+","+y+"];");
        //Log.d("MY_LOG","Vector: ["+temp.vectorX+","+temp.vectorY+"]; Speed: "+temp.getSpeed());
    }




    public static boolean insideBounds(float x, float y, RectF bounds)
    {
        boolean x_bool = x<bounds.right && x>bounds.left;
        boolean y_bool = y<bounds.bottom && y>bounds.top;

        //Log.d("MY_LOG","Hit: "+ (x_bool && y_bool));
        return x_bool && y_bool;
    }
}
