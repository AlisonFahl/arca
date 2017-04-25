package com.arca.std.input;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public class InputAction{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private InputStateGetter mInputStateGetter;
	private InputState mState = InputState.FREE;
	private ArrayList<InputActionListener> mInputActionListenerList = new ArrayList<InputActionListener>();
	
	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public InputAction(InputStateGetter pInputStateGetter){
		mInputStateGetter = pInputStateGetter;
	}
	
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public void check() {
		if(mInputStateGetter.isPressed()){
			if(mState == InputState.FREE){
				mState = InputState.PRESSED;
				
				for(int i=0; i<mInputActionListenerList.size(); i++){
					mInputActionListenerList.get(i).onPressed();
				}
			}
		}
		else if(mState == InputState.PRESSED){
			mState = InputState.FREE;
			
			for(int i=0; i<mInputActionListenerList.size(); i++){
				mInputActionListenerList.get(i).onReleased();
			}
		}
	}
	
	
	
	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public ArrayList<InputActionListener> getListenersList(){
		return mInputActionListenerList;
	}
	
	/*========================
	/////INNER CLASSES////////
	========================*/
	public enum InputState{
		FREE, PRESSED/*, DISABLED*/
	}
}
