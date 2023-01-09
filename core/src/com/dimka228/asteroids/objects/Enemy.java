package com.dimka228.asteroids.objects;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.objects.interfaces.AI;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.utils.VectorUtils;

public class Enemy extends AbstractPlayer implements AI{
    GameObject target;
    public Enemy(float x, float y){
        super(x, y);
        color = new Color(Color.PURPLE);
        ammo =1;
        reload = 0.005f;
        hp = 0.2;
        setTarget(Game.getInstance().getPlayer());
    }
    public void update(){
        super.update();
        float da = Math.abs(angleTo(target.getBody().getPosition()) - body.getAngle());
        if(target==null || target.getStatus()==Status.DESTROYED){
            ///////////////
        }else{
            rotateTo(target.getBody().getPosition());
            //rotateLeft();
            if(distanceTo(target) >20 && da<=MathUtils.PI/3) thrust();
            if(da <=MathUtils.PI/10)shootForward();
        }
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
        float a =  MathUtils.atan2(body.getPosition().y - pos.y, body.getPosition().x - pos.x);
        a += MathUtils.PI/2;
        return a;

    }
    float distanceTo(GameObject o){
        return VectorUtils.distance(body.getPosition(), o.getBody().getPosition());
    }
}
