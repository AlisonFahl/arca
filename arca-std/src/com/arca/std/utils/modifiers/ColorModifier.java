package com.arca.std.utils.modifiers;

import com.arca.core.scenenode.function.Consumer;
import com.badlogic.gdx.graphics.Color;

public class ColorModifier extends Modifier<Color> {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public ColorModifier(Color pInitialValue, Color pFinalValue, Consumer<Color> pValueSetter){
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
