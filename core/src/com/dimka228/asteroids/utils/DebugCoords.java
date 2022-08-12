package com.dimka228.asteroids.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class DebugCoords {
    static ShapeRenderer sr = new ShapeRenderer();
    public static void draw(Vector2 coords, Camera camera){
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeType.Line);
        sr.setColor(Color.RED);
        sr.circle(coords.x, coords.y, 2);
        sr.end();
    }
    public static void draw(Vector2 coords, Camera camera, float radius){
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeType.Line);
        sr.setColor(Color.RED);
        sr.circle(coords.x, coords.y, radius);
        sr.end();
    }

    public static void draw(Vector2 coords, Camera camera, float w,float h){
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeType.Line);
        sr.setColor(Color.RED);
        sr.rect(coords.x, coords.y, w,h);
        sr.end();
    }
}
