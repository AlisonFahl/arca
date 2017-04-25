package com.arca.loop;


public interface GameCycles {
	/*========================
	////////CONSTANTS/////////
	========================*/
	
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public void update(float pDeltaTime);
	public void fixedUpdate(float pTimestep);
	public void render(float pAlpha);
	
	/*========================
	////GETTERS & SETTERS////
	========================*/
	
	
	/*========================
	/////INNER INTERFACES/////
	========================*/
}
