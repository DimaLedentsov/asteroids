package com.dimka228.asteroids.ai;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dimka228.asteroids.objects.interfaces.Updateable;

public class AIManager implements Updateable{
    private Deque< AITask> actions;

    private boolean isProcessing;

    public AIManager(){
        actions = new ArrayDeque<>();

        isProcessing = true;
    }
    public void update(){
        if(!isProcessing) return;
        AITask task =  actions.peek();
        if(task==null) return;

        //if (restart) firstAction();
        
        //if(current == null) return;

        if(task.isEnabled()) task.run();
        else actions.poll();
    }
   


    public void addTask(AITask t){
        actions.push(t);
       
    }

    public Deque<AITask> getTasks(){
        return actions;
    }

}
