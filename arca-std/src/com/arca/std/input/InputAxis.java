package com.arca.std.input;

import java.util.ArrayList;

public class InputAxis{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private InputValueGetter mInputValueGetter;
	private float mValue = 0;
	private ArrayList<InputAxisListener> mInputAxisListenerList = new ArrayList<InputAxisListener>();
	
	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public InputAxis(InputValueGetter pInputValueGetter){
		mInputValueGetter = pInputValueGetter;
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public void check() {
		float value = mInputValueGetter.getValue();
		if(mValue != value){
			mValue = value;
			
			for(int i=0; i<mInputAxisListenerList.size(); i++){
				mInputAxisListenerList.get(i).onValueChanged(mValue);
			}
		}
	}
	
	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public ArrayList<InputAxisListener> getListenersList(){
		return mInputAxisListenerList;
	}
	
	public float getValue(){
		return mValue;
	}
	
	/*========================
	/////INNER CLASSES////////
	========================*/

}
