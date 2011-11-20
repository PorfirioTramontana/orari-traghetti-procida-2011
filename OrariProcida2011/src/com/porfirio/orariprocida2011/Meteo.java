package com.porfirio.orariprocida2011;

public class Meteo {
	private double windBeaufort;
	private int windDirection;
	
	public Meteo (double wb,int wd){
		setWindBeaufort(wb);
		setWindDirection(wd);
	}
	
	public Meteo(){
		windBeaufort=0.0;
		windDirection=0;
	}

	public void setWindBeaufort(double windBeaufort) {
		this.windBeaufort = windBeaufort;
	}

	public double getWindBeaufort() {
		return windBeaufort;
	}

	public void setWindDirection(int windDirection) {
		this.windDirection = windDirection;
	}

	public int getWindDirection() {
		return windDirection;
	}
}
