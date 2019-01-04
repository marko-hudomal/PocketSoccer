package com.example.markohudomal.pocketsoccer.game.gamedata;

import android.content.SharedPreferences;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.example.markohudomal.pocketsoccer.GameActivity;
import com.example.markohudomal.pocketsoccer.extras.StaticValues;
import com.google.gson.Gson;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class GameData {
    //SharedPreferences
    private SharedPreferences sharedPreferences;

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


        //SharedPrefference
        sharedPreferences = gameActivity.getSharedPreferences("game_data",MODE_PRIVATE);
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

        if (gameActivity.isResume_game())
            retrieveAll();

        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("paused_game", true);
        Log.d("MY_LOG","editor.save: paused_game");
        editor.commit();
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
                if (temp.hitWallVector())
                    gameActivity.bounce.start();
                if(temp.hitOtherBallVector())
                    gameActivity.bounce.start();
            }
        }
        football.simpleMove();
        if (football.hitWallVector())
            gameActivity.bounce.start();
        if (football.hitOtherBallVector())
            gameActivity.bounce.start();

        if (football.insideGoal()==0){
            goals2++;
            initBalls();
            gameActivity.crowd.start();
            if (!isTimeGame() && ((endVal==goals1) || (endVal==goals2)))
            {
                gameOver=true;
                timeOver();
            }
        }else if (football.insideGoal()==1){
            goals1++;
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


    public synchronized  void nextPlayer(){
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
    public synchronized boolean botPlays(int player)
    {
        if (player_turn!=player) return false;

        int num = (int)(Math.random() * 3);
        Ball ball;
        if (player==0)
            ball= player1_balls.get(num);
        else
            ball= player2_balls.get(num);

        float pom_x=football.mFigurePosition.x-ball.mFigurePosition.x;
        float pom_y=football.mFigurePosition.y-ball.mFigurePosition.y;
        float resultant_y;
        float resultant_x;

        if (pom_y==0)
        {
            //Log.d("MY_LOG","OVDE JE BIO PROBLEM!!!!<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            float jacina = (float)(Math.random() * 25)+15;

            resultant_y=0;
            resultant_x=jacina;
        }else
        {
            float odnos=Math.abs(pom_x/pom_y);
            float jacina = (float)(Math.random() * 25)+15;

            resultant_y=jacina/(float)Math.sqrt(odnos*odnos+1);
            resultant_x=resultant_y*odnos;
        }

        if (pom_x<0)
        {
            resultant_x=(-resultant_x);
        }
        if (pom_y<0)
        {
            resultant_y=(-resultant_y);
        }

        ball.vectorX+=resultant_x;
        ball.vectorY+=resultant_y;

        nextPlayer();
        return true;
    }
    //==============================================================================================
    public synchronized void saveAll(){
        SharedPreferences.Editor editor= sharedPreferences.edit();

        Gson gson = new Gson();
        String json;

        //--------------------------------------
        //0
        Log.d("MY_LOG","SAVED env"+player1_balls.get(0).extractData().toString());
        json= gson.toJson(player1_balls.get(0).extractData());
        editor.putString("p1_ball_0", json);
        //1
        Log.d("MY_LOG","SAVED env"+player1_balls.get(1).extractData().toString());
        json= gson.toJson(player1_balls.get(1).extractData());
        editor.putString("p1_ball_1", json);
        //2
        Log.d("MY_LOG","SAVED env"+player1_balls.get(2).extractData().toString());
        json= gson.toJson(player1_balls.get(2).extractData());
        editor.putString("p1_ball_2", json);

        //--------------------------------------
        //0
        Log.d("MY_LOG","SAVED env"+player2_balls.get(0).extractData().toString());
        json= gson.toJson(player2_balls.get(0).extractData());
        editor.putString("p2_ball_0", json);

        //1
        Log.d("MY_LOG","SAVED env"+player2_balls.get(1).extractData().toString());
        json= gson.toJson(player2_balls.get(1).extractData());
        editor.putString("p2_ball_1", json);

        //2
        Log.d("MY_LOG","SAVED env"+player2_balls.get(2).extractData().toString());
        json= gson.toJson(player2_balls.get(2).extractData());
        editor.putString("p2_ball_2", json);
        //--------------------------------------
        //f

        json= gson.toJson(football.extractData());
        editor.putString("football", json);

        //--------------------------------------
        EnvironmentData environmentData = new EnvironmentData(player_turn,goals1,goals2,seconds_turn,endVal,gameActivity.getName1(),gameActivity.getName2(),gameActivity.isBot1(),gameActivity.isBot2(),gameActivity.getFlag1(),gameActivity.getFlag2(),gameActivity.getSettings_fieldType(),gameActivity.getSettings_gameEndType(),gameActivity.getSettings_gameEndVal(),gameActivity.getSettings_gameSpeed());
        Log.d("MY_LOG","SAVED env"+environmentData.toString());
        json= gson.toJson(environmentData);
        editor.putString("environment_data", json);
        //--------------------------------------

        editor.commit();
    }
    public synchronized void retrieveAll()
    {
        Gson gson = new Gson();
        String json;

        //env
        json= sharedPreferences.getString("environment_data", "");
        EnvironmentData environmentData = gson.fromJson(json, EnvironmentData.class);
        //Log.d("MY_LOG",environmentData.toString());

        player_turn=environmentData.player_turn;
        goals1=environmentData.goals1;
        goals2=environmentData.goals2;
        seconds_turn=environmentData.seconds_turn;//StaticValues
        endVal=environmentData.endVal;//Settings


        gameActivity.setName1(environmentData.name1);
        gameActivity.setName2(environmentData.name2);
        gameActivity.setBot1(environmentData.bot1);
        gameActivity.setBot2(environmentData.bot2);
        gameActivity.setFlag1(environmentData.flag1);
        gameActivity.setFlag2(environmentData.flag2);

        gameActivity.setSettings_fieldType(environmentData.settings_fieldType);
        gameActivity.setSettings_gameEndType(environmentData.settings_gameEndType);
        gameActivity.setSettings_gameEndVal(environmentData.settings_gameEndVal);
        gameActivity.setSettings_gameSpeed(environmentData.settings_gameSpeed);

        //------------------------------------------------------------------------------------------

        while(player1_balls.size()>0)
            player1_balls.remove(0);
        while(player2_balls.size()>0)
            player2_balls.remove(0);

        //p1
        json = sharedPreferences.getString("p1_ball_0", "");
        BallData ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player1_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player1_balls.get(0).setData(ballData);

        json = sharedPreferences.getString("p1_ball_1", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player1_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player1_balls.get(1).setData(ballData);

        json = sharedPreferences.getString("p1_ball_2", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player1_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player1_balls.get(2).setData(ballData);

        //p2
        json = sharedPreferences.getString("p2_ball_0", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player2_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player2_balls.get(0).setData(ballData);

        json = sharedPreferences.getString("p2_ball_1", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player2_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player2_balls.get(1).setData(ballData);

        json = sharedPreferences.getString("p2_ball_2", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        player2_balls.add(new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.playerSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y)));
        player2_balls.get(2).setData(ballData);

        //f
        json = sharedPreferences.getString("football", "");
        ballData=gson.fromJson(json, BallData.class);
        //Log.d("MY_LOG",ballData.toString());
        football = new Ball(this,(mFieldConstraint.bottom-mFieldConstraint.top)/StaticValues.footballSizeScale,new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y));
        football.setData(ballData);

        //----REFRESH
        gameActivity.getmCustomImageView().refreshFlags();
        gameActivity.getmCustomImageView().refreshBackground();
    }
    public void set_resumed_game_saved(){
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean("paused_game", true);
        Log.d("MY_LOG","editor.save: paused_game");
    }
    //==============================================================================================
    //==============================================================================================

}
