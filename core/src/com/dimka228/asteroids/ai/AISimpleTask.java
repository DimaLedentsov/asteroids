package com.dimka228.asteroids.ai;

public class AISimpleTask implements AITask{
    private Runnable action;
    protected boolean isEnabled;
    public AISimpleTask(Runnable a){
        action = a;
        isEnabled = true;
    }
    public void run(){
        if(!isEnabled()) return;
        action.run();
    }

    public void setEnabled(boolean f){
        isEnabled = f;
    }

    public boolean isEnabled(){
        return isEnabled;
    }
}
