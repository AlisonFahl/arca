package com.arca.std.utils.modifiers;

import java.util.function.Consumer;

import com.badlogic.gdx.math.Quaternion;

public class QuaternionModifier extends Modifier<Quaternion> {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public QuaternionModifier(Quaternion pInitialValue, Quaternion pFinalValue, Consumer<Quaternion> pValueSetter){
		super(pInitialValue, pFinalValue, pValueSetter);
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	public void modify(float pProgress) {
		mValueSetter.accept(mInitialValue.cpy().slerp(mFinalValue, pProgress));
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	
	
	/*========================
	/////INNER CLASSES////////
	========================*/
}
