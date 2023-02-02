package com.dimka228.asteroids.objects.weapons;

import java.util.LinkedList;
import java.util.List;

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
import com.dimka228.asteroids.objects.GameObjectImpl;
import com.dimka228.asteroids.objects.interfaces.Alive;
import com.dimka228.asteroids.objects.interfaces.Damageable;
import com.dimka228.asteroids.objects.interfaces.Dieable;
import com.dimka228.asteroids.objects.interfaces.Explodeable;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.objects.interfaces.Ship;
import com.dimka228.asteroids.objects.particles.ExplosionParticle;
import com.dimka228.asteroids.physics.BodyCategories;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;


public class Bullet extends GameObjectImpl implements Dieable, Damageable, Explodeable{
    final float velocity = 5;
    private Ship parent;

    
    final double damage = 0.1;
    private LinkedList<Vector2> points;
    private int counter;
    void init(){
        BodyDef def = new BodyDef();
        def.type = BodyType.DynamicBody;
        
        body = Game.getInstance().getWorld().createBody(def);
        CircleShape c = new CircleShape();
    
        c.setPosition(new Vector2(0,0));
        c.setRadius(0.01f);
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
        points = new LinkedList<>();
    }
    public Bullet(Ship p, float x, float y, float angle){
        super( Type.BULLET);
        init();
        parent = p;
        body.setTransform(x, y, angle);
        body.setLinearVelocity((new Vector2(Vector2.Y)).scl(velocity).rotateRad(angle));
        color = p.getTeam().getColor().cpy();
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
        
        if(points.size()>=1) for(int i=0;i<points.size()-1;i++){
            //drawer.filledCircle(VectorUtils.toView(points.get(i)), p.getRadius()* Game.WORLD_TO_VIEW);
            drawer.line(VectorUtils.toView(points.get(i)), VectorUtils.toView(points.get(i+1)), lineWidth);
        }
        //drawer.filledCircle(pos, p.getRadius()* Game.WORLD_TO_VIEW);
        //drawer.circle(pos.x, pos.y, p.getRadius()* Game.WORLD_TO_VIEW, lineWidth, JoinType.NONE);
    }
    public void update(){
       
        //if(counter%10==0){
            points.addLast(body.getPosition().cpy());
            if(points.size()>=4) points.removeFirst();
       // }
        counter++;
        //body.update();
        
    }
    
    public void collide(GameObject obj){

        if(obj!=parent) {
            die();
            //obj.getBody().applyLinearImpulse(body.getLinearVelocity().scl(0.1f), body.getPosition(), false);
            explode();
            if(obj.getType()==Type.SHIP){
                Ship sh = (Ship)obj;
                if(sh.getTeam()!=parent.getTeam()) applyDamage(sh);
            }
            else if(obj instanceof Alive){
                applyDamage((Alive)obj);
            }
        }
    }

    public void explode(){
        Game.getInstance().addObject(new ExplosionParticle(body.getPosition().x, body.getPosition().y, Color.RED));

    }

    public void die(){
        setStatus(Status.DESTROYED);
        
    }

    public double getDamage(){return damage;}
    


    
}
