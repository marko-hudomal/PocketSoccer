package com.example.markohudomal.pocketsoccer.game.gamedata;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.example.markohudomal.pocketsoccer.extras.StaticValues;

import java.util.ArrayList;

public class Ball{

    private GameData myGameData;
    private boolean selected=false;

    public float radius;
    public RectF mFigureHolder;
    public PointF mFigurePosition;

    boolean moving=false;
    float d_speed=StaticValues.speedChange;
    public float vectorX=-1;
    public float vectorY=-1;

    public Ball(GameData myGameData, float size,PointF mFigurePosition) {
        this.myGameData=myGameData;
        this.mFigurePosition = mFigurePosition;
        radius=size;


        this.mFigureHolder = new RectF();
        float halfWidth = radius / 2 ;
        float halfHeight = radius / 2;

        mFigurePosition.x = myGameData.limitX(mFigurePosition.x, halfWidth);
        mFigurePosition.y = myGameData.limitY(mFigurePosition.y, halfHeight);

        mFigureHolder.left = mFigurePosition.x - halfWidth;
        mFigureHolder.right = mFigurePosition.x + halfWidth;
        mFigureHolder.top = mFigurePosition.y - halfHeight;
        mFigureHolder.bottom = mFigurePosition.y + halfHeight;
    }

    public BallData extractData()
    {
        //Log.d("MY_LOG","real position: "+mFigurePosition.x+"-"+mFigurePosition.y);
        return new BallData(selected,radius,mFigurePosition.x,mFigurePosition.y,moving,d_speed,vectorX,vectorY);
    }
    public void setData(BallData ballData)
    {
        selected=ballData.selected;
        radius=ballData.radius;
        mFigurePosition=new PointF(ballData.mFigurePosition_x,ballData.mFigurePosition_y);
        moving=ballData.moving;
        d_speed=ballData.d_speed;
        vectorX=ballData.vectorX;
        vectorY=ballData.vectorY;
    }
    public float distance(Ball p)
    {
        float dx=p.mFigurePosition.x-this.mFigurePosition.x;
        float dy=p.mFigurePosition.y-this.mFigurePosition.y;

        return (float)Math.sqrt(dx*dx+dy*dy);
    }
    public boolean hitOtherBallVector()
    {
        boolean result=false;
        ArrayList<Ball> current=null;
        for(int k=-1;k<2;k++)
        {
            switch(k)
            {
                case -1:current=null;break;
                case 0:current=myGameData.getPlayer1_balls();break;
                case 1:current=myGameData.getPlayer2_balls();break;
            }
            int max;
            Ball temp;
            if (current==null)
                max=1;
            else
                max=current.size();

            for(int i=0;i<max;i++)
            {
                if (k==-1)
                    temp=myGameData.getFootball();
                else
                    temp = current.get(i);
                //If not same ball
                if (temp!=this)
                {
                    //If hit
                    if (distance(temp)<=(this.radius/2)+(temp.radius/2))
                    {
                        result=true;

                        float pom_x=this.mFigurePosition.x-temp.mFigurePosition.x;
                        float pom_y=this.mFigurePosition.y-temp.mFigurePosition.y;

                        float resultant_y;
                        float resultant_x;
                        if (pom_y==0)
                        {
                            //Log.d("MY_LOG","deljenje nulom ispravljen problem");
                            float jacina=(float)Math.sqrt((vectorX)*(vectorX)+(vectorY)*(vectorY));

                            resultant_y=0;
                            resultant_x=jacina;

                        }else
                        {
                            float odnos=Math.abs(pom_x/pom_y);
                            float jacina=(float)Math.sqrt((vectorX)*(vectorX)+(vectorY)*(vectorY));

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

                        //this.mFigurePosition.x-=this.vectorX;
                        //this.mFigurePosition.y-=this.vectorY;
                        this.vectorX+=StaticValues.myVectorDecipation*resultant_x;
                        this.vectorY+=StaticValues.myVectorDecipation*resultant_y;
                        //this.setMoving(true);
                        temp.vectorX-=StaticValues.otherVectorDecipation*resultant_x;
                        temp.vectorY-=StaticValues.otherVectorDecipation*resultant_y;
                        //temp.setMoving(true);
                    }
                }
            }

        }
        return result;
    }
    public boolean hitWallVector()
    {
        float halfWidth = radius / 2 ;
        float halfHeight = radius / 2;
        boolean result=false;

        //LEFT HIT
        if (getFigurePosition().x-halfWidth<=myGameData.getFieldConstraint().left)
        {
            vectorX=-vectorX;
            result=true;
        }
        //RIGHT HIT
        if (getFigurePosition().x+halfWidth>=myGameData.getFieldConstraint().right)
        {
            vectorX=-vectorX;
            result=true;
        }
        //TOP HIT
        if (getFigurePosition().y-halfHeight<=myGameData.getFieldConstraint().top)
        {
            vectorY=-vectorY;
            result=true;

        }
        //BOTTOM HIT
        if (getFigurePosition().y+halfHeight>=myGameData.getFieldConstraint().bottom)
        {
            vectorY=-vectorY;
            result=true;
        }
        return result;
    }
    public boolean hitGoalVector(){
        float half = radius / 2 ;
        boolean result=false;
        //LEFT GOAL TOP TOP||BOTTOM

            //GOAL1----------------------------------------------------------------------------------
            //Top Bar
            RectF rect1_T_TB = new RectF(0,myGameData.getmGoal1Constraint().top-half,myGameData.getmGoal1Constraint().right+0,myGameData.getmGoal1Constraint().top+half);
            if (insideBounds(getFigurePosition(),rect1_T_TB) || insideCircle(new PointF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().top),half,getFigurePosition())){
//                if (vectorY>0){
//                    getFigurePosition().y=myGameData.getmGoal1Constraint().top-half;
//                }else{
//                    getFigurePosition().y=myGameData.getmGoal1Constraint().top+half;
//                }
                vectorY=-vectorY;
            }
            RectF rect1_T_LR = new RectF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().top-half,myGameData.getmGoal1Constraint().right+half,myGameData.getmGoal1Constraint().top+half);
            if (vectorX<0 && insideCircle(new PointF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().top),half,getFigurePosition())){
                //Log.d("Wall_Log","g1Hit1.2");
                vectorX=-vectorX;
            }
            //Bottom Bar
            RectF rect1_B_TB = new RectF(0,myGameData.getmGoal1Constraint().bottom-half,myGameData.getmGoal1Constraint().right+0,myGameData.getmGoal1Constraint().bottom+half);
            if (insideBounds(getFigurePosition(),rect1_B_TB) || insideCircle(new PointF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().bottom),half,getFigurePosition())){
//                if (vectorY>0){
//                    getFigurePosition().y=myGameData.getmGoal1Constraint().bottom-half;
//                }else{
//                    getFigurePosition().y=myGameData.getmGoal1Constraint().bottom+half;
//                }
                vectorY=-vectorY;
            }
            RectF rect1_B_LR = new RectF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().bottom-half,myGameData.getmGoal1Constraint().right+half,myGameData.getmGoal1Constraint().bottom+half);
            if (vectorX<0 && insideCircle(new PointF(myGameData.getmGoal1Constraint().right,myGameData.getmGoal1Constraint().bottom),half,getFigurePosition())){
                //Log.d("Wall_Log","g1Hit2.2");
                vectorX=-vectorX;
            }
            //---------------------------------------------------------------------------------------

        //GOAL2----------------------------------------------------------------------------------
        //Top Bar
        RectF rect2_T_TB = new RectF(myGameData.getmGoal2Constraint().left-half,myGameData.getmGoal2Constraint().top-half,myGameData.getmGoal2Constraint().right,myGameData.getmGoal2Constraint().top+half);
        if (insideBounds(getFigurePosition(),rect2_T_TB) || insideCircle(new PointF(myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().top),half,getFigurePosition())){
            //Log.d("Wall_Log","g2Hit1.1");
            vectorY=-vectorY;
        }
        RectF rect2_T_LR = new RectF(myGameData.getmGoal2Constraint().left-half,myGameData.getmGoal2Constraint().top-half,myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().top+half);
        if (vectorX>0 && insideCircle(new PointF(myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().top),half,getFigurePosition())){
            //Log.d("Wall_Log","g2Hit1.2");
            vectorX=-vectorX;
        }
        //Bottom Bar
        RectF rect2_B_TB = new RectF(myGameData.getmGoal2Constraint().left-half,myGameData.getmGoal2Constraint().bottom-half,myGameData.getmGoal2Constraint().right,myGameData.getmGoal2Constraint().bottom+half);
        if (insideBounds(getFigurePosition(),rect2_B_TB) || insideCircle(new PointF(myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().bottom),half,getFigurePosition())){
            //Log.d("Wall_Log","g2Hit2.1");
            vectorY=-vectorY;
        }
        RectF rect2_B_LR = new RectF(myGameData.getmGoal2Constraint().left-half,myGameData.getmGoal2Constraint().bottom-half,myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().bottom+half);
        if (vectorX>0 && insideCircle(new PointF(myGameData.getmGoal2Constraint().left,myGameData.getmGoal2Constraint().bottom),half,getFigurePosition())){
            //Log.d("Wall_Log","g2Hit2.2");
            vectorX=-vectorX;
        }
        //---------------------------------------------------------------------------------------
        return result;
    }
    public void simpleMove()
    {
        //Should Move
        if (vectorX!=0 || vectorY!=0){

            float halfWidth = radius/2 ;
            float halfHeight = radius/2;

            getFigurePosition().x += vectorX;
            getFigurePosition().y += vectorY;
            mFigurePosition.x = myGameData.limitX(mFigurePosition.x, halfWidth);
            mFigurePosition.y = myGameData.limitY(mFigurePosition.y, halfHeight);

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
                {
                    vectorX=0;
                }
            }else
            {
                if (vectorX>=0)
                {
                    vectorX=0;
                }
            }
            if (pos_y)
            {
                if (vectorY<0)
                {
                    vectorY=0;
                }
            }else
            {
                if (vectorY>0)
                {
                    vectorY=0;
                }
            }

        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

        getFigurePosition().x = myGameData.limitX(x, halfWidth);
        getFigurePosition().y = myGameData.limitY(y, halfHeight);

        getFigureHolder().left = getFigurePosition().x - halfWidth;
        getFigureHolder().right = getFigurePosition().x + halfWidth;
        getFigureHolder().top = getFigurePosition().y - halfHeight;
        getFigureHolder().bottom = getFigurePosition().y + halfHeight;
    }
    public float getSpeed()
    {
        return (float)Math.sqrt((vectorX)*(vectorX)+(vectorY)*(vectorY));
    }

    public void scaleMyVector(float scale)
    {
        vectorX/=scale;
        vectorY/=scale;
    }
    public void limitMyVector(float limit)
    {
        if (getSpeed()>limit)
        {
            //DELJENJE SA NULOM GRESKA--------------------------------------------------------------
            if (vectorY==0){
                if (vectorX<0)
                {
                    vectorX=(-limit);
                }else
                {
                    vectorX=(limit);
                }
                return;
            }
            if (vectorX==0){
                if (vectorY<0)
                {
                    vectorY=(-limit);
                }else
                {
                    vectorY=(limit);
                }
                return;
            }
            //--------------------------------------------------------------------------------------

            float n = vectorX/vectorY;

            float new_y=(float) Math.sqrt( (limit*limit)/ (n*n+1) );
            float new_x=Math.abs(n*new_y);
            if (vectorX<0)
            {
                vectorX= (-new_x);
            }else
            {
                vectorX= (new_x);
            }
            if (vectorY<0)
            {
                vectorY=(-new_y);
            }else
            {
                vectorY=(new_y);
            }
        }
    }
    public int insideGoal(){
        if (mFigureHolder.right<=myGameData.getmGoal1Constraint().right && mFigureHolder.top>=myGameData.getmGoal1Constraint().top && mFigureHolder.bottom<=myGameData.getmGoal1Constraint().bottom)
            return 0;
        if (mFigureHolder.left>=myGameData.getmGoal2Constraint().left && mFigureHolder.top>=myGameData.getmGoal1Constraint().top && mFigureHolder.bottom<=myGameData.getmGoal1Constraint().bottom)
            return 1;

        return -1;
    }
    public static boolean insideBounds(PointF point,RectF rect){
        if (rect.top<=point.y && rect.bottom>=point.y && rect.left<=point.x  && rect.right>=point.x){
            return true;
        }else{
            return false;
        }
    }
    public static boolean insideCircle(PointF point, float dist,PointF circlePoint){
        float dx=point.x-circlePoint.x;
        float dy=point.y-circlePoint.y;
        if (dist>Math.sqrt(dx*dx+dy*dy)){
            return true;
        }else{
            return false;
        }
    }
}
