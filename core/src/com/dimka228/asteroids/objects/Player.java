package com.dimka228.asteroids.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.utils.CollisionChecker;
import com.dimka228.asteroids.utils.Random;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


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

        ShapeDrawer drawer = game.getDrawer();
        PolygonBatch sb = game.getRenderer();
        sb.begin();
        
        drawer.setColor(Color.WHITE);
        drawer.polygon(shape.getTransformedVertices());
        Rectangle r = shape.getBoundingRectangle();
        drawer.setColor(Color.RED);
        drawer.rectangle(r.x, r.y, r.width, r.height);
        drawer.setColor(Color.GREEN);
        drawer.circle(pos.x, pos.y, 5);
        sb.end();
    }

    public void collide(Collideable obj){
    
	    float dx = pos.x - obj.getPosition().x;
	    float dy = pos.y - obj.getPosition().y;///////////////
        
	    /*float angle = (float) Math.atan2((double)dy,(double)dx);
	    float dirx = (float)Math.cos(angle);
	    float diry = (float)Math.sin(angle);*/
        float angle = MathUtils.atan2(dy,dx);
        float dirx = MathUtils.cos(angle);
	    float diry = MathUtils.sin(angle);
        /*vel.rotateDeg(180).scl(0.5f);
        if(vel.len()<2){
            vel.scl(2);
        }*/
    
        vel.setAngleRad(angle);
        float minV = 1;
        if(vel.len()<minV){
            vel.setLength(minV);
        }

        Vector2 v = CollisionChecker.getCollusionPoint(shape, obj.getShape());
        if(v==null) return;
        /*Vector2 v1 = new Vector2(obj.getPosition().x-pos.x, obj.getPosition().y-pos.y);
        Vector2 v2 = new Vector2(obj.getPosition().x-v.x, obj.getPosition().y-v.y);
        float xp = v1.x*v2.y - v1.x*v2.y;
*/

        float xp = (obj.getPosition().x - pos.x)*(v.y - pos.y) - (v.x - pos.x)*(obj.getPosition().x - pos.y);
        //float xp = pos.x*obj.getPosition().y - pos.y*obj.getPosition().x;
        angleVel = xp>0?3:-3;
        System.out.println(xp);
        /*ShapeRenderer sb = game.getRenderer();
        
        sb.begin(ShapeRenderer.ShapeType.Line);
        sb.setColor(Color.RED);
        sb.circle(v.x, v.y, 10);
        sb.line(pos, obj.getPosition());
        sb.end();*/
	    while(CollisionChecker.collides(shape, obj.getShape())){
	    	pos = pos.add(new Vector2(dirx, diry));
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
