package com.dimka228.asteroids.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.ai.AIConditionalTask;
import com.dimka228.asteroids.ai.AIManager;
import com.dimka228.asteroids.ai.AISimpleTask;
import com.dimka228.asteroids.ai.AITaskWithTimeLimit;
import com.dimka228.asteroids.objects.interfaces.AI;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.utils.VectorUtils;

public class SimpleBot extends AbstractShip implements AI{
    GameObject target;
    AIManager aiManager;
    private int counter;
    public SimpleBot(float x, float y, Teams team){
        super(x, y, team);
        color = new Color(team.getColor());
        ammo =1;
        reload = 0.05f;
        hp = 0.4;
        
        setTarget(team.selectRandomEnemy());

        aiManager = new AIManager();
        aiManager.addTask(new AISimpleTask(this::shootAndFly));
    }

    public void shootAndFly(){
        
        if(target==null || target.getStatus()==Status.DESTROYED|| counter>100){
           selectNearestEnemy();
            counter = 0;
        }else{
            float da = Math.abs(angleTo(target.getBody().getPosition()) - body.getAngle());
            
            //rotateLeft();
            if(distanceTo(target) >10 && da<=MathUtils.PI/3) thrust();
            if(da <=MathUtils.PI/10)shootForward();
            rotateTo(target.getBody().getPosition());
        }
    }
    public void selectNearestEnemy(){
        for(Teams team : Teams.values()){
            if(team!=this.team && team!=Teams.NEUTRAL){
                for(GameObject o : team.getPlayers()){
                    if(o!=null && o.getStatus()!=Status.DESTROYED){
                        if(target== null || target.getStatus()==Status.DESTROYED || distanceTo(o)<distanceTo(target)){
                            target = o;
                        }
                    }
                }
            }
        }
    }
    public void update(){
        super.update();
        aiManager.update();
        counter++;
 
    }

    public void rotateTo(Vector2 pos){
        float a =  angleTo(pos);
        float b = body.getAngle();
        
        //if(a>MathUtils.PI/2) a-=MathUtils.PI/2;
        /*if(Math.abs(a-b)>=MathUtils.PI) rotateRight();
        else*/ if(a>b) rotateRight();
         else if (a<b) rotateLeft();
        //body.setTransform(body.getPosition(), a);
    }
    public void setTarget(GameObject o){
        target = o;
    }
    float angleTo(Vector2 pos){
        float a =  MathUtils.atan2( pos.y-body.getPosition().y , pos.x-body.getPosition().x );
        a -= MathUtils.PI/2;
        if(a < 0){
            a += MathUtils.PI2;
        }
        
        return a;

    }
    float distanceTo(GameObject o){
        return VectorUtils.distance(body.getPosition(), o.getBody().getPosition());
    }
}
