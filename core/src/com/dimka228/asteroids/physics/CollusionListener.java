package com.dimka228.asteroids.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dimka228.asteroids.Game;
import com.dimka228.asteroids.objects.Bullet;
import com.dimka228.asteroids.objects.interfaces.GameObject;
import com.dimka228.asteroids.objects.particles.ExplosionParticle;
import com.dimka228.asteroids.utils.VectorUtils;

public class CollusionListener implements ContactListener {

    public CollusionListener() {

    }

    @Override
    public void beginContact(Contact contact) {
        // TODO Auto-generated method stub
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        GameObject o1 = (GameObject)a.getUserData();
        GameObject o2 = (GameObject)b.getUserData();
        o1.collide(o2);
        o2.collide(o1);
    
        Vector2 p = contact.getWorldManifold().getPoints()[0];
        Vector2 c = VectorUtils.toView(contact.getWorldManifold().getPoints()[0]);
        //Game.getInstance().getDrawer().circle(c.x,c.y, 5);
        
    }

    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // TODO Auto-generated method stub

    }
}
