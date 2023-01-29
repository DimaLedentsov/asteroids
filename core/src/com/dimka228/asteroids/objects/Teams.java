package com.dimka228.asteroids.objects;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;

import com.dimka228.asteroids.objects.interfaces.Ship;

public enum Teams {
    A(Color.ORANGE),
    B(Color.PURPLE),
    C(Color.GREEN),
    NEUTRAL(Color.GRAY);


    private Set<Ship> players;
    private Color color;
    private int points;

    Teams(Color c){
        color = c;
        points = 0;
        players = new HashSet<>();
    }
    public Set<Ship> getPlayers(){
        return players;
    }
    public void addPlayer(Ship p){
        players.add(p);
    }
    public void removePlayer(Ship p){
        players.remove(p);
    }
    public Color getColor(){
        return color;
    }
    public int getPoints(){
        return points;
    }
    public void addPoints(int p){
        points += p;
    }

    public Ship selectRandomEnemy(){
        Random r = new Random();
        Teams[] teams = values();
        
        
        Teams team = teams[r.nextInt(teams.length)];
        while(team == this) team = teams[r.nextInt(teams.length)];
        if(team.getPlayers().size() == 0) return null;
        Set<Ship> enemies = team.getPlayers();
        int i = (int)(Math.random()*enemies.size());
        int j = 0;
        for(Ship s : enemies){
            if(j == i) return s;
            j++;
        }
        return null;
    }
    public Teams selectRandomEnemyTeam(){
        Random r = new Random();
        Teams[] teams = values();
        int teamIndex = (int)(r.nextInt(teams.length));
        
        Teams team = teams[teamIndex];
        while(teams.length == 0 || team == this) team = teams[r.nextInt(teams.length)];
        return team;
    }
    

}
