package com.dimka228.asteroids.objects.particles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.utils.RandomUtils;
import com.dimka228.asteroids.utils.VectorUtils;

import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class ThrustParticle extends Particle{
    final static float velocity = 0.7f;
    final static float randomAngle = 0.1f;
    private float velD;
    //private float size;
    private float sizeD;
    private float randomAngle(){
        return  RandomUtils.randomBetween(-randomAngle, randomAngle);
    }
    public ThrustParticle(float x, float y, float angle){

        pos = new Vector2(x, y);
        vel = VectorUtils.neg(new Vector2(Vector2.Y)).rotateRad(angle + randomAngle()).scl(MathUtils.random(0.1f, velocity));
        size=MathUtils.random(0.1f,0.5f);
        sizeD=0.02f;
        velD = 0.1f;
    }

    
    public void update(){
       super.update();
        size-=sizeD;
        vel.scl(1-velD);
        if(size<=0) setStatus(Status.DESTROYED);
    }
    public void render(){
        ShapeDrawer drawer = Game.getInstance().getDrawer();
  
        
        drawer.setColor(color);
        Vector2 render = VectorUtils.toView(pos);

        drawer.circle(render.x,render.y, size*Game.WORLD_TO_VIEW, lineWidth, JoinType.NONE);
    }

}
