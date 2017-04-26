package com.arca.std.utils.modifiers;

import com.arca.core.scenenode.function.Consumer;
import com.badlogic.gdx.math.Vector3;

public class Vector3Modifier extends Modifier<Vector3> {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public Vector3Modifier(Vector3 pInitialValue, Vector3 pFinalValue, Consumer<Vector3> pValueSetter){
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
