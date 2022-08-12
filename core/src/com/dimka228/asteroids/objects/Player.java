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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.utils.Random;
import com.dimka228.asteroids.utils.VectorUtils;


public class Player extends GameObjectImpl implements Dieable, Forceable{
    private float angle;
    private float angleVel;
    protected Vector2 pos;
    protected Vector2 vel;
    protected Vector2 accel;
    public Player(Game game) {
        // super(coords, velocity, accel, texture, type)
        super(game,
                Type.PLAYER);
        pos = new Vector2(100,100);
        vel =  new Vector2(0,0);
        accel = new Vector2(0, 0);
        shape = new Polygon(new float[]{20,-10,0,50,-20,-10});
        layer = 2;
        mass = 2;
        
    }
    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            angle = shape.getRotation();
            float mod = 0.05f;
            
            Vector2 force = new Vector2(Vector2.Y).scl(mod);//VectorUtils.fromAngle(angle, mod);
            //System.out.println(angle);
            force.rotateDeg(angle);
            //System.out.println(force.angleDeg());
            applyForce(force);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            
        }
       
        else{
            accel=new Vector2();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            angleVel = -3f;
            angle += angleVel;  
            angleVel = 0;          
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            angleVel = 3f;
            angle += angleVel;
            angleVel = 0;
        }
        //if(Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.S)) angleVel = 0;
        angle += angleVel;
        shape.setRotation(angle);
        vel.add(accel);
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
    
	    float dx = pos.x - obj.getPosition().x;
	    float dy = pos.y - obj.getPosition().y;///////////////
        
	    float angle = (float) Math.atan2((double)dy,(double)dx);
	    float dirx = (float)Math.cos(angle);
	    float diry = (float)Math.sin(angle);
        vel.rotateDeg(180).scl(0.5f);

        float xp = pos.x*obj.getPosition().x - pos.y*obj.getPosition().y;
        angleVel = xp>0?3:-3;
	    while(Intersector.overlapConvexPolygons(shape, obj.getShape())){
	    	pos = pos.add(new Vector2(dirx*2, diry*2));
            shape.setPosition(pos.x,pos.y);
	    }
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
