package com.example.markohudomal.myapplication.gamedata;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.markohudomal.myapplication.GameActivity;
import com.example.markohudomal.myapplication.StaticValues;

import java.util.ArrayList;

public class GameData {

    //Positions
    private RectF mFieldConstraint;
    private RectF mScoreBoardConstraint;
    private float player_radius;
    private float football_radius;

    private Ball football;
    private ArrayList<Ball> player1_balls = new ArrayList<>();
    private ArrayList<Ball> player2_balls= new ArrayList<>();

    //Settings
    private GameActivity gameActivity;

    //Goals,Time
    private int player_turn=0;
    private int goals1=0;
    private int goals2=0;
    private int seconds_turn;//StaticValues
    private int endVal;//Settings

    public GameData(GameActivity gameActivity) {
        mFieldConstraint = null;
        this.gameActivity=gameActivity;
        seconds_turn=StaticValues.SecondsTurn;
        endVal=gameActivity.getSettings_gameEndVal();
    }

    public void setFieldConstraint(RectF constraint) {
        mFieldConstraint = constraint;
        //Positions and radius
        //Player1
        player1_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF((constraint.right)/6,constraint.bottom*((float)0.25))));
        player1_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF((constraint.right)/6,constraint.bottom*((float)0.75))));
        player1_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF(constraint.right/3,constraint.bottom*((float)0.5))));
        //Player2
        player2_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.25))));
        player2_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.75))));
        player2_balls.add(new Ball((constraint.bottom-constraint.top)/6,new PointF(constraint.right*(((float)2)/3),constraint.bottom*((float)0.5))));
        //Football
        football = new Ball((constraint.bottom-constraint.top)/8,new PointF((constraint.right)/2,constraint.bottom/2));
    }
    public void setScoreBoardConstraint(RectF constraint){
        mScoreBoardConstraint=constraint;
    }

    public RectF getScoreBoardConstraint() {
        return mScoreBoardConstraint;
    }

    private float limitY(float y, float offset) {
        if (mFieldConstraint == null) {
            return y;
        }
        if (y - offset < mFieldConstraint.top) {
            return mFieldConstraint.top + offset;
        } else if (y + offset > mFieldConstraint.bottom) {
            return mFieldConstraint.bottom - offset;
        } else {
            return y;
        }
    }

    private float limitX(float x, float offset) {
        if (mFieldConstraint == null) {
            return x;
        }
        if (x - offset < mFieldConstraint.left) {
            return mFieldConstraint.left + offset;
        } else if (x + offset > mFieldConstraint.right) {
            return mFieldConstraint.right - offset;
        } else {
            return x;
        }
    }

    public boolean decEndVal()
    {
        if (endVal==0)
        {
            return true;
        }
        endVal--;
        return false;
    }
    public void decSecTurn()
    {
        seconds_turn--;
        if (seconds_turn<0)
        {
            seconds_turn=StaticValues.SecondsTurn;
            player_turn=1-player_turn;
        }

    }
    public ArrayList<Ball> getPlayer1_balls() {
        return player1_balls;
    }
    public ArrayList<Ball> getPlayer2_balls() {
        return player2_balls;
    }


    public void timeOver()
    {
        if (goals1>goals2)
        {
            gameActivity.setEndMessage(0);
        }else
            if (goals2>goals1){
            gameActivity.setEndMessage(1);
            }else
            {
                gameActivity.setEndMessage(-1);
            }
    }

    public boolean updateFieldState()
    {
        for(int i=0;i<player1_balls.size();i++)
        {
            Ball temp=player1_balls.get(i);
            temp.simpleMove();
            temp.hitWallVector();
            //temp.hitOtherBallVector();
        }
        for(int i=0;i<player2_balls.size();i++)
        {
            Ball temp=player2_balls.get(i);
            temp.simpleMove();
            temp.hitWallVector();
            //temp.hitOtherBallVector();
        }

        return true;
    }

    public int getPlayer_turn() {
        return player_turn;
    }

    public void setPlayer_turn(int player_turn) {
        this.player_turn = player_turn;
    }

    public int getEndVal() {
        return endVal;
    }

    public Ball getFootball() {
        return football;
    }

    public void setFootball(Ball football) {
        this.football = football;
    }

    public int getGoals1() {
        return goals1;
    }

    public void setGoals1(int goals1) {
        this.goals1 = goals1;
    }

    public int getGoals2() {
        return goals2;
    }

    public void setGoals2(int goals2) {
        this.goals2 = goals2;
    }

    public int getSeconds_turn() {
        return seconds_turn;
    }

    public void setSeconds_turn(int seconds_turn) {
        this.seconds_turn = seconds_turn;
    }

    public String getName1() {
        return gameActivity.getName1();
    }

    public String getName2() {
        return gameActivity.getName2();
    }

    public boolean isTimeGame()
    {
        return gameActivity.getSettings_gameEndType()==0;
    }
    public RectF getFieldConstraint() {
        return mFieldConstraint;
    }
    public void setPlayer1_balls(ArrayList<Ball> player1_balls) { this.player1_balls = player1_balls; }
    public void setPlayer2_balls(ArrayList<Ball> player2_balls) { this.player2_balls = player2_balls; }
    //==============================================================================================
    //==============================================================================================
    public class Ball{
        public float radius;
        public RectF mFigureHolder;
        public PointF mFigurePosition;

        boolean moving=false;
        float d_speed=20;
        public float vectorX=-1;
        public float vectorY=-1;

        public Ball(float size,PointF mFigurePosition) {
            this.mFigurePosition = mFigurePosition;
            radius=size;


            this.mFigureHolder = new RectF();
            float halfWidth = radius / 2 ;
            float halfHeight = radius / 2;

            mFigurePosition.x = limitX(mFigurePosition.x, halfWidth);
            mFigurePosition.y = limitY(mFigurePosition.y, halfHeight);

            mFigureHolder.left = mFigurePosition.x - halfWidth;
            mFigureHolder.right = mFigurePosition.x + halfWidth;
            mFigureHolder.top = mFigurePosition.y - halfHeight;
            mFigureHolder.bottom = mFigurePosition.y + halfHeight;
        }

        public void hitWallVector()
        {
            float halfWidth = radius / 2 ;
            float halfHeight = radius / 2;

            //LEFT HIT
            if (getFigurePosition().x-halfWidth<=mFieldConstraint.left)
            {
                vectorX=-vectorX;
            }
            //RIGHT HIT
            if (getFigurePosition().x+halfWidth>=mFieldConstraint.right)
            {
                vectorX=-vectorX;
            }
            //TOP HIT
            if (getFigurePosition().y-halfHeight<=mFieldConstraint.top)
            {
                vectorY=-vectorY;
            }
            //BOTTOM HIT
            if (getFigurePosition().y+halfHeight>=mFieldConstraint.bottom)
            {
                vectorY=-vectorY;
            }
        }
        public void simpleMove()
        {
            //Should Move
            if (moving){

                float halfWidth = radius/2 ;
                float halfHeight = radius/2;
                getFigurePosition().x += vectorX;
                getFigurePosition().y += vectorY;
                mFigurePosition.x = limitX(mFigurePosition.x, halfWidth);
                mFigurePosition.y = limitY(mFigurePosition.y, halfHeight);

                getFigureHolder().left = getFigurePosition().x - halfWidth;
                getFigureHolder().right = getFigurePosition().x + halfWidth;
                getFigureHolder().top = getFigurePosition().y - halfHeight;
                getFigureHolder().bottom = getFigurePosition().y + halfHeight;

                boolean pos_x = (vectorX>0);
                boolean pos_y = (vectorY>0);

                if (Math.abs(vectorX)>Math.abs(vectorY)){
                    if (pos_x)
                        vectorX-=d_speed;
                    else
                        if(!pos_x)
                            vectorX+=d_speed;

                    if (pos_y)
                        vectorY-=d_speed*(Math.abs(vectorY)/Math.abs(vectorX));
                    else
                        if (!pos_y)
                            vectorY+=d_speed*(Math.abs(vectorY)/Math.abs(vectorX));
                }else
                {
                    if (vectorY>0)
                        vectorY-=d_speed;
                    else
                        vectorY+=d_speed;
                    if (vectorX>0)
                        vectorX-=d_speed*(Math.abs(vectorX)/Math.abs(vectorY));
                    else
                        vectorX+=d_speed*(Math.abs(vectorX)/Math.abs(vectorY));
                }

                if (pos_x)
                {
                    if (vectorX<=0)
                        moving=false;
                }else
                {
                    if (vectorX>=0)
                        moving=false;
                }
                if (pos_y)
                {
                    if (vectorY<0)
                        moving=false;
                }else
                {
                    if (vectorY>0)
                        moving=false;
                }

            }
        }

        public boolean isMoving() {
            return moving;
        }

        public void setMoving(boolean moving) {
            this.moving = moving;
        }

        public RectF getmFigureHolder() {
            return mFigureHolder;
        }

        public void setmFigureHolder(RectF mFigureHolder) {
            this.mFigureHolder = mFigureHolder;
        }

        public PointF getmFigurePosition() {
            return mFigurePosition;
        }

        public void setmFigurePosition(PointF mFigurePosition) {
            this.mFigurePosition = mFigurePosition;
        }

        public RectF getFigureHolder() {
            return mFigureHolder;
        }

        public void setFigureHolder(RectF mFigureHolder) {
            this.mFigureHolder = mFigureHolder;
        }

        public PointF getFigurePosition() {
            return mFigurePosition;
        }

        public void setFigurePosition(PointF mFigurePosition) {
            this.mFigurePosition = mFigurePosition;
        }

        public void move(float x, float y)
        {
            float halfWidth = radius/2 ;
            float halfHeight = radius/2;

            getFigurePosition().x = limitX(x, halfWidth);
            getFigurePosition().y = limitY(y, halfHeight);

            getFigureHolder().left = getFigurePosition().x - halfWidth;
            getFigureHolder().right = getFigurePosition().x + halfWidth;
            getFigureHolder().top = getFigurePosition().y - halfHeight;
            getFigureHolder().bottom = getFigurePosition().y + halfHeight;
        }
        public float getSpeed()
        {
            return (float)Math.sqrt((vectorX)*(vectorX)+(vectorY)*(vectorY));
        }


    }
    //==============================================================================================
    //==============================================================================================

}
