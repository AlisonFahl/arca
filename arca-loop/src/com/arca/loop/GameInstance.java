package com.arca.loop;

import com.arca.core.scenenode.function.Consumer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public abstract class GameInstance extends ApplicationAdapter implements GameCycles{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private GameObject mRootScene;
	
	private float mFixedTimestep, mAccumulatedTimestep;

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public GameInstance(){
		mFixedTimestep = 1 / 32f;
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	public void render() {
		if(hasScene()){
			//Fixed Update
			mAccumulatedTimestep += Gdx.graphics.getRawDeltaTime();
			while(mAccumulatedTimestep >= mFixedTimestep){
				fixedUpdate(mFixedTimestep);
				mAccumulatedTimestep -= mFixedTimestep;
			}
			
			//Update
			update(Gdx.graphics.getRawDeltaTime());
			
			//Render
			render(mAccumulatedTimestep / mFixedTimestep);
		}
	}
	
	@Override
	public void update(final float pDeltaTime) {
		mRootScene.update(pDeltaTime);
		mRootScene.broadcast(new Consumer<GameObject>() {
			@Override
			public void accept(GameObject x) {
				if(!x.isIgnored() && !x.isIgnoringUpdate())
					x.update(pDeltaTime); 
			}
		});
	}
	
	@Override
	public void fixedUpdate(final float pTimestep) {
		mRootScene.fixedUpdate(pTimestep);
		mRootScene.broadcast(new Consumer<GameObject>() {
			@Override
			public void accept(GameObject x) {
				if(!x.isIgnored() && !x.isIgnoringUpdate())
					x.fixedUpdate(pTimestep); 
			}
		});
	}
	
	@Override
	public void render(final float pAlpha) {
		mRootScene.render(pAlpha);
		mRootScene.broadcast(new Consumer<GameObject>() {
			@Override
			public void accept(GameObject x) {
				if(!x.isIgnored() && !x.isIgnoringRender())
					x.render(pAlpha); 
			}
		});
	}
	
	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	protected void setRootScene(GameObject pScene){
		mRootScene = pScene;
	}
	
	protected GameObject getRootScene(){
		return mRootScene;
	}
	
	protected boolean hasScene(){
		return mRootScene != null;
	}
	
	public float getFixedTimestep() {
		return mFixedTimestep;
	}

	public void setFixedTimestep(float pFixedTimestep) {
		mFixedTimestep = pFixedTimestep;
	}
	
	
	/*========================
	/////NESTED CLASSES////////
	========================*/
}
