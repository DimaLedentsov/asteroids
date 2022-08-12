package com.dimka228.asteroids.physics;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

public final class Body {
    private Polygon shape;
    private Vector2 vel;
    private Vector2 accel;
    private float angle;
    private Vector2 pos;
    private float angleVel;
    private float m;
    public void update(){
        vel.add(accel);
        angle += angleVel;
        pos.add(vel);
    }
    public Body(float[] vertices, ){

    }
    public Polygon getShape(){
        return shape;
    }
     
    public Vector2 getVelocity(){
        return vel;
    }
    public Vector2 getAcceleration(){
        return accel;
    }

    public float getAngle(){
        return angle;
    }
    public Vector2 getPosition(){
        return pos;
    }

    public float getMass(){
        return m;
    }





}
