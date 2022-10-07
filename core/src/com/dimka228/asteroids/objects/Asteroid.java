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
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.physics.RigidBody;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class Asteroid extends GameObjectImpl implements Dieable{
    public Asteroid(Game game) {
        // super(coords, velocity, accel, texture, type)
        super(game,
                Type.ENEMY);
        body = new RigidBody(new float[]{
            103.99f,11.000f ,
            36.992f,73.000f ,
            -119.008f,20.000f ,
            -119.008f,-20.000f,
            44.992f,-81.000f  
           
        }, new Vector2(), new Vector2(), 0, new Vector2(400,400), 0.1f, 20);

        layer = 2;        
    }
    public void update(){
       
        System.out.println(body.getVelocity().toString());
        body.update();
        
    }
    public void render(){
        ShapeDrawer drawer = game.getDrawer();
  
        
        drawer.setColor(Color.WHITE);
        
        drawer.polygon(body.getShape().getTransformedVertices());
        Rectangle r = body.getShape().getBoundingRectangle();
        drawer.setColor(Color.RED);
        drawer.rectangle(r.x, r.y, r.width, r.height);
        drawer.setColor(Color.GREEN);
        drawer.circle(body.getPosition().x, body.getPosition().y, 5);
  
    }

    public void collide(Collideable obj){
        die();
    }

    public void die(){
        setStatus(Status.DYING);
        
    }
    

    
}
