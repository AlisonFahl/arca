package com.arca.std.utils.modifiers;

import com.arca.core.scenenode.function.Consumer;

public abstract class Modifier<T>{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	protected Consumer<T> mValueSetter;
	protected T mInitialValue, mFinalValue;

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	protected Modifier(T pInitialValue, T pFinalValue, Consumer<T> pValueSetter) {
		mValueSetter = pValueSetter;
		mInitialValue = pInitialValue;
		mFinalValue = pFinalValue;
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public abstract void modify(float pProgress);

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public Consumer<T> getValueSetter() {
		return mValueSetter;
	}

	public void setValueSetter(Consumer<T> pValueSetter) {
		mValueSetter = pValueSetter;
	}
	
	public T getInitialValue() {
		return mInitialValue;
	}

	public void setInitialValue(T pInitialValue) {
		mInitialValue = pInitialValue;
	}

	public T getFinalValue() {
		return mFinalValue;
	}

	public void setFinalValue(T pFinalValue) {
		mFinalValue = pFinalValue;
	}
	
	/*========================
	/////INNER CLASSES////////
	========================*/
}
