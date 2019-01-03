package com.example.markohudomal.pocketsoccer.game;

import android.os.HandlerThread;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

public class GameThread extends HandlerThread {

    public interface ThreadListener{
        public void secondPassed();
        public void updateFieldState();

        public void endThisGame();
    }
    private ThreadListener threadListener;
    private boolean isRunning = false;
    private long lastTime = 0;
    private long lastTime2 = 0;

    public GameThread(String name, int priority) {
        super(name, priority);
    }


    public void setListener(ThreadListener t)
    {
        threadListener=t;
    }
    @Override
    public void run() {

        //GAME
        lastTime = System.currentTimeMillis();
        lastTime2 = System.currentTimeMillis();

        while (isRunning) {

            //Update on second
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                if (threadListener!=null)
                {
                    threadListener.secondPassed();
                    //Log.d("MY_LOG","listener: second tick, "+(++i));
                }

                lastTime = currentTime;
            }
            //Update on some time
            final long currentTime2 = System.currentTimeMillis();
            if (currentTime2 - lastTime2 >= StaticValues.refreshRate) {
                if (threadListener!=null)
                {
                    threadListener.updateFieldState();
                    //Log.d("MY_LOG","listener: second tick, "+(++i));
                }

                lastTime2 = currentTime2;
            }

        }
        //END PAUSE
        int endRunning=StaticValues.endPause;
        while(endRunning>0)
        {
            //Update on second
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                endRunning--;
                lastTime = currentTime;
            }
        }
        threadListener.endThisGame();
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

}
