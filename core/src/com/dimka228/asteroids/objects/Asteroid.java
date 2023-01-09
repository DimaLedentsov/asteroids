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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.objects.interfaces.Dieable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.physics.BodyCategories;
import com.dimka228.asteroids.utils.PolygonUtils;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;


public class Asteroid extends GameObjectImpl implements Dieable{
    void init(){
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        
        body = Game.getInstance().getWorld().createBody(def);
        PolygonShape poly = new PolygonShape();

        poly.set(PolygonUtils.generate(6, 3));;
        FixtureDef fix = new FixtureDef();
        fix.filter.categoryBits= BodyCategories.SCENERY;
        fix.filter.maskBits=BodyCategories.SCENERY;
        fix.shape=poly;
        fix.density=0.1f;

        body.createFixture(fix);
        poly.dispose(); 
    
        body.setUserData(this);
        layer = 2;   

        
    }
    public Asteroid(float x, float y){
        super( Type.OBSTACLE);
        init();
        body.setTransform(x, y, 0);
    }

    public void update(){
       
        
        //body.update();
        
    }
    
    public void collide(GameObject obj){
        die();
    }


    public void die(){
        setStatus(Status.DYING);
        
    }

    
    

    
}
