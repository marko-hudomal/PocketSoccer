package com.example.markohudomal.pocketsoccer.game;

import android.os.HandlerThread;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

public class GameThread extends HandlerThread {

    public interface ThreadListener{
        public void secondPassed();
        public void updateFieldState();
        public boolean tryPlayTurn(int player);
        public void endThisGame();
        public int getSpeed();
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
        int b=0;

        while (isRunning) {


            //Update on second
            final long currentTime = System.currentTimeMillis();
            if (currentTime - lastTime >= 1000) {
                if (threadListener!=null)
                {
                    threadListener.secondPassed();
                    b++;
                    if (b>=StaticValues.SecondsTurn/2){
                        if (!threadListener.tryPlayTurn(0))
                            threadListener.tryPlayTurn(1);
                        b=0;
                    }
                    //Log.d("MY_LOG","listener: second tick, "+(++i));
                }

                lastTime = currentTime;
            }
            //Update on some time
            final long currentTime2 = System.currentTimeMillis();
            int d=1;
            switch (threadListener.getSpeed())
            {
                case 4: d=1;    break;
                case 3: d=2;    break;
                case 2: d=10;   break;
                case 1: d=20;   break;
            }
            if (currentTime2 - lastTime2 >= (StaticValues.refreshRate*d)) {
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
