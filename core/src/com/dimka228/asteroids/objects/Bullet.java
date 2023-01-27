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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.logic.Forceable;
import com.dimka228.asteroids.objects.interfaces.Alive;
import com.dimka228.asteroids.objects.interfaces.Damageable;
import com.dimka228.asteroids.objects.interfaces.Dieable;
import com.dimka228.asteroids.objects.interfaces.Explodeable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.objects.particles.ExplosionParticle;
import com.dimka228.asteroids.physics.BodyCategories;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Bullet extends GameObjectImpl implements Dieable, Damageable, Explodeable{
    final float velocity = 5;
    private GameObject parent;

    final double damage = 0.1;
    void init(){
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        
        body = Game.getInstance().getWorld().createBody(def);
        CircleShape c = new CircleShape();
    
        c.setPosition(new Vector2(0,0));
        c.setRadius(0.3f);
        FixtureDef fix = new FixtureDef();
        fix.filter.categoryBits= BodyCategories.SCENERY;
        fix.filter.maskBits=BodyCategories.SCENERY;
        fix.shape=c;
        fix.density=0.1f;

        body.createFixture(fix);
        c.dispose(); 
        
        body.setUserData(this);
        layer = 2;      
        color = Color.RED;
        lineWidth = 2;
    }
    public Bullet(GameObject p, float x, float y, float angle){
        super( Type.BULLET);
        init();
        parent = p;
        body.setTransform(x, y, angle);
        body.setLinearVelocity((new Vector2(Vector2.Y)).scl(velocity).rotateRad(angle));
    }
    public Bullet() {
        // super(coords, velocity, accel, texture, type)
    
        super( Type.BULLET);
        init();  
        
    }
    @Override 
    public void render(){
        ShapeDrawer drawer = Game.getInstance().getDrawer();
  

        CircleShape p = (CircleShape)body.getFixtureList().get(0).getShape();
        drawer.setColor(color);
        Vector2 pos = VectorUtils.toView(body.getPosition());
        drawer.filledCircle(pos, p.getRadius()* Game.WORLD_TO_VIEW);
        //drawer.circle(pos.x, pos.y, p.getRadius()* Game.WORLD_TO_VIEW, lineWidth, JoinType.NONE);
    }
    public void update(){
       
        
        //body.update();
        
    }
    
    public void collide(GameObject obj){

        if(obj!=parent) {
            die();
            //obj.getBody().applyLinearImpulse(body.getLinearVelocity().scl(0.1f), body.getPosition(), false);
            explode();
            if(obj instanceof Alive){
                applyDamage((Alive)obj);
            }
        }
    }

    public void explode(){
        Game.getInstance().addObject(new ExplosionParticle(body.getPosition().x, body.getPosition().y));

    }

    public void die(){
        setStatus(Status.DESTROYED);
        
    }

    public double getDamage(){return damage;}
    


    
}
