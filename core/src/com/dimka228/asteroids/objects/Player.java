package com.dimka228.asteroids.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

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
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.physics.RigidBody;
import com.dimka228.asteroids.utils.CollisionUtils;
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
            body.rotate(-3f);
            body.setRotation(0);
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            body.rotate(3f); 
            body.setRotation(0);
        }
        

        //if(Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.S)) angleVel = 0;
        body.update();
        body.setRotationAcceleration(0);
        
    }
    public void render(){

        ShapeDrawer drawer = game.getDrawer();
        PolygonBatch sb = game.getRenderer();
    
        
        drawer.setColor(Color.WHITE);
        drawer.polygon(body.getShape().getTransformedVertices());
        Rectangle r = body.getShape().getBoundingRectangle();
        drawer.setColor(Color.RED);
        drawer.rectangle(r.x, r.y, r.width, r.height);
        drawer.setColor(Color.GREEN);
        drawer.circle(body.getPosition().x, body.getPosition().y, 5);

    }

    public void collide(Collideable obj){
    
        
    }


    public void die(){
        setStatus(Status.DYING);
        
    }



    
}
