package com.dimka228.asteroids.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class PositionConverter {
    public static Vector2 toScreen(Vector2 pos, Camera cam, float WIDTH, float HEIGHT){
        //return new Vector2(cam.position.x-WIDTH/2-pos.x, cam.position.y-HEIGHT/2-pos.y);
       Vector3 p =   cam.project(new Vector3(pos, 0));
       
       return new Vector2(p.x,p.y);
    }
}
