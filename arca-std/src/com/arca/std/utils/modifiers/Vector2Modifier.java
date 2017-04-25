package com.arca.std.utils.modifiers;

import java.util.function.Consumer;

import com.badlogic.gdx.math.Vector2;

public class Vector2Modifier extends Modifier<Vector2> {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public Vector2Modifier(Vector2 pInitialValue, Vector2 pFinalValue, Consumer<Vector2> pValueSetter){
		super(pInitialValue, pFinalValue, pValueSetter);
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	public void modify(float pProgress) {
		mValueSetter.accept(mInitialValue.cpy().lerp(mFinalValue, pProgress));
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	
	
	/*========================
	/////INNER CLASSES////////
	========================*/
}
