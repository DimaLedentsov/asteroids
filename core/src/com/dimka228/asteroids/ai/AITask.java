package com.dimka228.asteroids.ai;

public interface AITask  extends Runnable{
    public boolean isEnabled();
    public void setEnabled(boolean f);
    public void finish();
    public boolean isFinished();
}
