package com.dimka228.asteroids.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.utils.VectorUtils;


public class Asteroid extends GameObjectImpl implements Dieable, Forceable{
    private float angle;
    private float angleVel;
    protected Vector2 pos;
    protected Vector2 vel;
    protected Vector2 accel;
    public Asteroid(Game game) {
        // super(coords, velocity, accel, texture, type)
        super(game,
                Type.ENEMY);
        pos = new Vector2(400,400);
        vel =  new Vector2(0,0);
        accel = new Vector2(0, 0);
        shape = new Polygon(new float[]{20,-10,0,50,-20,-10});
        layer = 2;
        mass = 2;
        
    }
    public void update(){
       
        //System.out.println(accel.toString());

        pos.add(vel);
        shape.setPosition(pos.x,pos.y);
        
    }
    public void render(){

        ShapeRenderer sb = game.getRenderer();
        sb.begin(ShapeRenderer.ShapeType.Line);
        sb.setColor(Color.WHITE);
        sb.polygon(shape.getTransformedVertices());
        Rectangle r = shape.getBoundingRectangle();
        sb.setColor(Color.RED);
        sb.rect(r.x, r.y, r.width, r.height);
        sb.end();
    }

    public void collide(Collideable obj){
        die();
    }

    public void die(){
        setStatus(Status.DYING);
        
    }

    public void applyForce(Vector2 f){
        accel.set(f.scl(mass));
       
    }
    public Vector2 getVelocity(){
        return vel;
    }
    public Vector2 getPosition(){
        return pos;
    }

    
}
