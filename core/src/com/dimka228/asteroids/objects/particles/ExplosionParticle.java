package com.dimka228.asteroids.objects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.utils.RandomUtils;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class ExplosionParticle extends Particle{
    final static float velocity = 0.5f;
    final static float randomAngle = 0.1f;

   // private float size;
    private float sizeD;
    private float alphaD;
    private float velD;
    public ExplosionParticle(float x, float y){

        pos = new Vector2(x, y);
        vel = new Vector2();
        size=0.00001f;
        sizeD=0.025f;

        alphaD = 0.01f;
        color = new Color(Color.RED);
    }
    public ExplosionParticle(Vector2 p,  Vector2 v, float s, float sd, float ad, Color c){

        pos = p.cpy();
        vel = v.cpy();
        size=s;
        sizeD=sd;
        alphaD = ad;
        color = new Color(c);
    }
    public ExplosionParticle(Vector2 p,  Vector2 v,float vd, float s, float sd, float ad, Color c){

        pos = p.cpy();
        vel = v.cpy();
        size=s;
        sizeD=sd;
        velD = vd;
        alphaD = ad;
        color = new Color(c);
    }
    public void update(){
        super.update();
        size+=sizeD;
        color.a -= alphaD;
        if(velD!=0) vel.scl(1-velD);;
       
        if(color.a<=0) setStatus(Status.DESTROYED);
    }
    public void render(){
        ShapeDrawer drawer = Game.getInstance().getDrawer();
  
        
        drawer.setColor(color);
        Vector2 render = VectorUtils.toView(pos);
        drawer.filledCircle(render.x,render.y, size*Game.WORLD_TO_VIEW);
    }

}
