package com.dimka228.asteroids.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dimka228.asteroids.objects.interfaces.Updateable;

public class AIManager implements Updateable{
    private List< AITask> actions;
    private AITask current;
    private boolean isProcessing;
    private int index;
    public AIManager(){
        actions = new ArrayList<>();
        index = 0;
        isProcessing = true;
        current = null;
    }
    public void update(){
        if(!isProcessing) return;
        if(actions.size()==0 || current == null) return;
        //if (restart) firstAction();
        selectAction();
        //if(current == null) return;

        if(current.isEnabled()) current.run();
        nextAction();

    }
    private void selectAction(){
        if(index>=actions.size()) index = 0;
        current = actions.get(index);
    }
    public void selectAction(int i){
        index = i;
        current = actions.get(index);
    }
    public void nextAction(){
        index+=1;
    }
    public void prevAction(){
        index-=1;
    }
    public int getCurrentIndex(){
        return index;
    }
    public void setCurrentIndex(int i){
        index = i;
    }

    public void addTask(AITask t){
        actions.add(t);
        if(actions.size()==1) current = t;
    }


}
