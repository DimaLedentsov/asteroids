package com.dimka228.asteroids.objects.interfaces;

public interface Damageable {
    public double getDamage();
    default void applyDamage(Alive o){
        o.takeHp(getDamage());
    }
}
