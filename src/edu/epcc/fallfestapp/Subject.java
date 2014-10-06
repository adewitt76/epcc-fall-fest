package edu.epcc.fallfestapp;

public interface Subject {
	public void registerObs(Observer o);
	public void removeObs(Observer o);
	public void notifyObs();
}
