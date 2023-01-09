package com.dimka228.asteroids.utils;

import com.badlogic.gdx.math.MathUtils;

public class PolygonUtils {
    public static float[] generate(float dimmin, float vardim) {
        int npunti = (int)MathUtils.random(3, 8);    //numero punti asteroide
        float[] punti = new float[npunti * 2];   //per due perchè contiene x e y
        float a = (float) (Math.random() * Math.PI * 2);
        for (int i = 0; i < npunti * 2; i += 2) {
            
            float r = (float) (Math.random() * vardim + dimmin); //raggio circonferenza
            float spost = (float) (Math.random() * Math.PI * 2 / (npunti * 10));  //serve per non avere asteroidi troppo circolari
            a += (float) (Math.PI * 2 / npunti) + spost - Math.PI * 2 / (npunti * 10 * 2);
            punti[i] = (float) (Math.cos(a) * r);  //x è il coseno dell angolo
            punti[i + 1] = (float) (Math.sin(a) * r);  //y è il seno dell angolo
        }
        return punti;
    }
}
