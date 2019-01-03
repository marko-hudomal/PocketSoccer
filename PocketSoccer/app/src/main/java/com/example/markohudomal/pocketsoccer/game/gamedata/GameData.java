package com.example.markohudomal.pocketsoccer.game.gamedata;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.markohudomal.pocketsoccer.GameActivity;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.util.ArrayList;

public class GameData {

    //Positions
    private RectF mFieldConstraint;
    private RectF mScoreBoardConstraint;
    private RectF mGoal1Constraint;
    private RectF mGoal2Constraint;

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
    private boolean gameOver=false;

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
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)/6,constraint.bottom*((float)0.25))));
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)/6,constraint.bottom*((float)0.75))));
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF(constraint.right/3,constraint.bottom*((float)0.5))));
        //Player2
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.25))));
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.75))));
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF(constraint.right*(((float)2)/3),constraint.bottom*((float)0.5))));
        //Football
        football = new Ball(this,(constraint.bottom-constraint.top)/StaticValues.footballSizeScale,new PointF((constraint.right)/2,constraint.bottom/2));

    }
    public void setScoreBoardConstraint(RectF constraint){
        mScoreBoardConstraint=constraint;
    }
    public void setGoalConstraints(RectF constraint1,RectF constraint2){
        mGoal1Constraint=constraint1;
        mGoal2Constraint=constraint2;
    }
    public void initBalls(){
        RectF constraint=mFieldConstraint;
        while(player1_balls.size()>0)
            player1_balls.remove(0);
        while(player2_balls.size()>0)
            player2_balls.remove(0);
        //Player1
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)/6,constraint.bottom*((float)0.25))));
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)/6,constraint.bottom*((float)0.75))));
        player1_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF(constraint.right/3,constraint.bottom*((float)0.5))));
        //Player2
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.25))));
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF((constraint.right)*(((float)5)/6),constraint.bottom*((float)0.75))));
        player2_balls.add(new Ball(this,(constraint.bottom-constraint.top)/StaticValues.playerSizeScale,new PointF(constraint.right*(((float)2)/3),constraint.bottom*((float)0.5))));
        //Football
        football = new Ball(this,(constraint.bottom-constraint.top)/StaticValues.footballSizeScale,new PointF((constraint.right)/2,constraint.bottom/2));
    }
    public boolean updateFieldState()
    {
        ArrayList<Ball> current=null;
        for(int k=0;k<2;k++)
        {
            switch (k) {
                case 0:current = getPlayer1_balls();break;
                case 1:current = getPlayer2_balls();break;
            }
            for (int i = 0; i < current.size(); i++) {
                Ball temp = current.get(i);
                temp.simpleMove();
                temp.hitWallVector();
                temp.hitOtherBallVector();
            }
        }
        football.simpleMove();
        if (football.hitWallVector())
            gameActivity.bounce.start();
        football.hitOtherBallVector();
        if (football.insideGoal()==0){
            goals1++;
            initBalls();
            gameActivity.crowd.start();
            if (!isTimeGame() && ((endVal==goals1) || (endVal==goals2)))
            {
                gameOver=true;
                timeOver();
            }
        }else if (football.insideGoal()==1){
            goals2++;
            initBalls();
            gameActivity.crowd.start();
            gameActivity.crowd.start();
            if (!isTimeGame() && ((endVal==goals1) || (endVal==goals2)))
            {
                gameOver=true;
                timeOver();
            }
        }
        return true;
    }



    //----------------------------------------------------------------------------------------------


    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public RectF getmGoal1Constraint() {
        return mGoal1Constraint;
    }

    public void setmGoal1Constraint(RectF mGoal1Constraint) {
        this.mGoal1Constraint = mGoal1Constraint;
    }

    public RectF getmGoal2Constraint() {
        return mGoal2Constraint;
    }

    public void setmGoal2Constraint(RectF mGoal2Constraint) {
        this.mGoal2Constraint = mGoal2Constraint;
    }

    public RectF getScoreBoardConstraint() {
        return mScoreBoardConstraint;
    }

    public float limitY(float y, float offset) {
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

    public float limitX(float x, float offset) {
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


    public void nextPlayer(){
        player_turn=1-player_turn;
        seconds_turn=StaticValues.SecondsTurn;
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

    //==============================================================================================
    //==============================================================================================

}
