package com.arca.std.utils.timer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class TimeLine {
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private ArrayList<KeyFrame> mKeyFrames = new ArrayList<KeyFrame>();

	/*========================
	///////CONSTRUCTORS///////
	========================*/

	/*=======================
	//////////METHODS////////
	=======================*/
	public void setProgress(float pProgress){
		int targetKeyFrame = -1;
		
		for (int i = 0; i < mKeyFrames.size(); i++) {
			KeyFrame keyFrame = mKeyFrames.get(i);
			if(keyFrame.mTime <= pProgress){
				targetKeyFrame = i;
				continue;
			}
			else{
				break;
			}
		}
		
		if(targetKeyFrame >= 0){
			KeyFrame keyFrame = mKeyFrames.get(targetKeyFrame);
			float alpha = 0;
			if(targetKeyFrame > 0){
				KeyFrame preKeyFrame = mKeyFrames.get(targetKeyFrame - 1);
				float deltaTime = keyFrame.mTime - preKeyFrame.mTime;
				float relativeProgress = pProgress - preKeyFrame.mTime;
				alpha = deltaTime / relativeProgress;
			}
			else{
				alpha = keyFrame.mTime / pProgress;
			}
			
			keyFrame.mAction.accept(alpha);
		}
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/

	/*========================
	/////NESTED CLASSES////////
	========================*/
	public static class KeyFrame {
		/*========================
		////FIELDS & CONSTANTS////
		========================*/
		private float mTime;
		private Consumer<Float> mAction;

		/*========================
		///////CONSTRUCTORS///////
		========================*/
		public KeyFrame(float pTime, Consumer<Float> pAction){
			mTime = pTime;
			mAction = pAction;
		}

		/*=======================
		//////////METHODS////////
		=======================*/

		/*========================
		 ////GETTERS & SETTERS////
		========================*/

		/*========================
		/////NESTED CLASSES////////
		========================*/

	}
}
