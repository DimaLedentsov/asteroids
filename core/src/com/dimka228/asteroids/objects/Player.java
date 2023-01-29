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
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.objects.interfaces.Ship;
import com.dimka228.asteroids.objects.interfaces.Shootable;
import com.dimka228.asteroids.objects.particles.ThrustParticle;
import com.dimka228.asteroids.physics.BodyCategories;
import com.dimka228.asteroids.utils.Random;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class Player extends AbstractPlayer{


    public Player(float x, float y, Teams team) {
 
        // super(coords, velocity, accel, texture, type)
        super(x,y,team);
        color = Color.ORANGE.cpy();
        hp = 10;
        
    }
    public void update(){
        
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            thrust();
        }
        else if(Gdx.input.isKeyPressed(Input.Keys.S)){
            
        }
       
        else{
            //body.setAcceleration(new Vector2());
        }
        
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //body.rotate(-3f);
            //body.setRotation(0);
            rotateLeft();//body.setTransform(body.getPosition(), body.getAngle()-0.1f);
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //body.rotate(3f); 
            //body.setRotation(0);
            rotateRight();//body.setTransform(body.getPosition(), body.getAngle()+0.1f);
        } else {
            stopRotation();
        }

    
        /*if(Gdx.input.isKeyPressed(Input.Keys.D)){
            //body.rotate(-3f);
            //body.setRotation(0);
            if(!isRotating) {
                body.setAngularVelocity(-0.1f);//body.setTransform(body.getPosition(), body.getAngle()-0.1f);
                isRotating=true;
            }
        } else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            //body.rotate(3f); 
            //body.setRotation(0);
            if(!isRotating) {
                body.setAngularVelocity(0.1f);//body.setTransform(body.getPosition(), body.getAngle()+0.1f);
                isRotating=true;
            }
        } else {
            if(isRotating) body.setAngularVelocity(0);
            isRotating = false;
        }*/

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            shootForward();
        }
        super.update();

        //if(Gdx.input.isKeyPressed(Input.Keys.W)||Gdx.input.isKeyPressed(Input.Keys.S)) angleVel = 0;
        //body.update();
        //body.setRotationAcceleration(0);
        
    }

    

}
