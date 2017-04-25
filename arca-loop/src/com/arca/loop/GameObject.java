package com.arca.loop;

import com.arca.core.scenenode.SceneNode;

public abstract class GameObject extends SceneNode<GameObject> implements GameCycles{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private boolean mIgnored = false, mIgnoringUpdate = false, mIgnoringRender = false;
	
	/*========================
	///////CONSTRUCTORS///////
	========================*/
	

	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	public void update(float pDeltaTime){
		
	}
	
	@Override
	public void fixedUpdate(float pTimestep){
		
	}
	
	@Override
	public void render(float pAlpha){
		
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public boolean isIgnored() {
		return mIgnored;
	}

	public void setIgnored(boolean pIgnored) {
		mIgnored = pIgnored;
	}

	public boolean isIgnoringUpdate() {
		return mIgnoringUpdate;
	}

	public void setIgnoringUpdate(boolean pIgnoringUpdate) {
		mIgnoringUpdate = pIgnoringUpdate;
	}

	public boolean isIgnoringRender() {
		return mIgnoringRender;
	}

	public void setIgnoringRender(boolean pIgnoringRender) {
		mIgnoringRender = pIgnoringRender;
	}

	/*========================
	/////NESTED CLASSES////////
	========================*/
	
}
