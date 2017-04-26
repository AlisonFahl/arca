package com.arca.std.utils.modifiers;

import com.arca.core.scenenode.function.Consumer;
import com.badlogic.gdx.math.MathUtils;

public class FloatModifier extends Modifier<Float> {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public FloatModifier(float pInitialValue, float pFinalValue, Consumer<Float> pValueSetter){
		super(pInitialValue, pFinalValue, pValueSetter);
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	public void modify(float pProgress) {
		mValueSetter.accept(MathUtils.lerp(mInitialValue, mFinalValue, pProgress));
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	
	
	/*========================
	/////INNER CLASSES////////
	========================*/
}
