package com.arca.std.utils.timer;

import java.util.ArrayList;

public class TimeAction extends Timer {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private ArrayList<TimeLine> mTimeLines;

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public TimeAction(float pDuration) {
		super(pDuration);
	}

	/*=======================
	//////////METHODS////////
	=======================*/
	@Override
	protected void onUpdatedProgress(float pProgress) {
		super.onUpdatedProgress(pProgress);
		
		for (TimeLine timeLine : mTimeLines) {
			timeLine.setProgress(pProgress);
		}
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public ArrayList<TimeLine> getTimeLines() {
		return mTimeLines;
	}

	/*========================
	/////NESTED CLASSES////////
	========================*/

}
