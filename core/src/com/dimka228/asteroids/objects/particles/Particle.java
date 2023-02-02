package com.dimka228.asteroids.objects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.dimka228.asteroids.objects.AbstractGameObject;
import com.dimka228.asteroids.objects.interfaces.GameObject;

public class Particle extends AbstractGameObject{
    public Particle(){
        super(GameObject.Type.PARTICLE);
        lifeTime = 1000000;
    }
    
    protected Vector2 pos;
    protected Vector2 vel;
    protected int lifeTime;
    protected float size;
    public int getLifeTime() {
        return this.lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }


    public Vector2 getPos() {
        return this.pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getVel() {
        return this.vel;
    }

    public void setVel(Vector2 vel) {
        this.vel = vel;
    }


    //protected Vector2
    
    public void render(){

    }
    public void collide(GameObject o){}
    public void update(){
        pos.add(vel);
        if(color.a<=0 || size<=0) setStatus(Status.DESTROYED);
        
    }
    public void destroy(){}
    public Body getBody(){return null;}
    public Vector2 getPosition(){return pos;}
}
