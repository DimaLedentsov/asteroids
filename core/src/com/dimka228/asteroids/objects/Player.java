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
import com.dimka228.asteroids.physics.RigidBody;
import com.dimka228.asteroids.utils.CollisionChecker;
import com.dimka228.asteroids.utils.Random;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class Player extends GameObjectImpl implements Dieable{
    public Player(Game game) {
        // super(coords, velocity, accel, texture, type)
        super(game,
                Type.PLAYER);
        body = new RigidBody(new float[]{20,-10,0,50,-20,-10},
            new Vector2(), 
            new Vector2(), 
            0, 
            new Vector2(100, 100),
            0, 
        2);
        
        layer = 2;
        
    }
    public void update(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){

        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            float mod = 0.05f;
            
            Vector2 force = new Vector2(Vector2.Y).scl(mod);//VectorUtils.fromAngle(angle, mod);
            //System.out.println(angle);
            force.rotateDeg(body.getAngle());
            //System.out.println(force.angleDeg());
            body.applyForce(force);
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            
        }
       
        else{
            body.setAcceleration(new Vector2());
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            body.setRotation(-3f);
            body.updateAngle();
            body.setRotation(0);       
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            body.setRotation(3f);
            body.updateAngle();
            body.setRotation(0);    
        }

        //if(Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.S)) angleVel = 0;
        body.update();
        
    }
    public void render(){

        ShapeDrawer drawer = game.getDrawer();
        PolygonBatch sb = game.getRenderer();
        sb.begin();
        
        drawer.setColor(Color.WHITE);
        drawer.polygon(body.getShape().getTransformedVertices());
        Rectangle r = body.getShape().getBoundingRectangle();
        drawer.setColor(Color.RED);
        drawer.rectangle(r.x, r.y, r.width, r.height);
        drawer.setColor(Color.GREEN);
        drawer.circle(body.getPosition().x, body.getPosition().y, 5);
        sb.end();
    }

    public void collide(Collideable obj){
    
	    float dx = body.getPosition().x - obj.getBody().getPosition().x;
	    float dy = body.getPosition().y - obj.getBody().getPosition().y;///////////////
        
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
    
        
        final Vector2 vel = body.getVelocity();
        vel.setAngleRad(angle);
        float minV = 1;
        if(vel.len()<minV){
            vel.setLength(minV);
        }

        Vector2 v = CollisionChecker.getCollusionPoint(body.getShape(), obj.getBody().getShape());
        if(v==null) return;
        /*Vector2 v1 = new Vector2(obj.getPosition().x-pos.x, obj.getPosition().y-pos.y);
        Vector2 v2 = new Vector2(obj.getPosition().x-v.x, obj.getPosition().y-v.y);
        float xp = v1.x*v2.y - v1.x*v2.y;
*/

        float xp = (obj.getBody().getPosition().x - body.getPosition().x)*(v.y - body.getPosition().y) - (v.x - body.getPosition().x)*(obj.getBody().getPosition().x - body.getPosition().y);
        //float xp = pos.x*obj.getPosition().y - pos.y*obj.getPosition().x;
        body.setRotation(xp>0?3:-3);
        System.out.println(xp);
        /*ShapeRenderer sb = game.getRenderer();
        
        sb.begin(ShapeRenderer.ShapeType.Line);
        sb.setColor(Color.RED);
        sb.circle(v.x, v.y, 10);
        sb.line(pos, obj.getPosition());
        sb.end();*/
	    while(CollisionChecker.collides(body.getShape(), obj.getBody().getShape())){
	    	Vector2 np = body.getPosition().add(new Vector2(dirx, diry));
            body.setPosition(np);
	    }
        
    }


    public void die(){
        setStatus(Status.DYING);
        
    }



    
}
