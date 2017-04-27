package com.arca.std.utils.timer;

public class Timer{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private float mDuration, mTime;
	private float mProgress;
	private RepetitionMode mRepetitionMode = RepetitionMode.NONE;
	private TimerEvent mFinishEvent;
	private boolean mReverse;
	
	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public Timer(float pDuration){
		if(pDuration <= 0){
			throw new IllegalArgumentException("pDuration must be greater than zero.");
		}
		mDuration = pDuration;	
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public void updateProgress(float pTimeElapsed) {
		 if(pTimeElapsed != 0){
			float lastTime = mTime, timeLeftover = 0;
			boolean finished = false;
			
			if(mReverse){
				mTime = lastTime - pTimeElapsed;
				if(mTime <= 0){
					finished = true;
					timeLeftover = -mTime;
					mTime = 0;
				}
				else if(mTime > mDuration){
					mTime = mDuration;
				}
			}
			else{
				mTime = lastTime + pTimeElapsed;
				if(mTime >= mDuration){
					finished = true;
					timeLeftover = mTime - mDuration;
					mTime = mDuration;
				}
				else if(mTime < 0){
					mTime = 0;
				}
			}
			
			float deltaTime = lastTime - mTime;
			
			if(deltaTime != 0){
				mProgress = mTime / mDuration;
				onUpdatedProgress(mProgress);
				
				if(finished){
					if(mFinishEvent != null){
						mFinishEvent.onFinished(timeLeftover);
					}
					
					if(mRepetitionMode != null && mRepetitionMode != RepetitionMode.NONE){
						if(mRepetitionMode == RepetitionMode.ALTERNATE){
							mReverse = !mReverse;
						}
						else if(mRepetitionMode == RepetitionMode.LOOP){
							mTime = mReverse ? mDuration : 0;
						}
						
						if(timeLeftover != 0){
							updateProgress(timeLeftover);
						}
					}
				}
			}
		}
	}
	
	public void start(){
		if(mReverse){
			updateProgress(mDuration - mTime);
		}
		else{
			updateProgress(-mTime);
		}
	}
	
	public void finish(){
		if(mReverse){
			updateProgress(-mTime);
		}
		else{
			updateProgress(mDuration - mTime);
		}
	}
	
	protected void onUpdatedProgress(float pProgress){
		
	}
	
	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public RepetitionMode getRepetitionMode() {
		return mRepetitionMode;
	}

	public void setRepetitionMode(RepetitionMode pRepetitionMode) {
		mRepetitionMode = pRepetitionMode;
	}

	public TimerEvent getFinishEvent() {
		return mFinishEvent;
	}

	public void setFinishEvent(TimerEvent pFinishEvent) {
		mFinishEvent = pFinishEvent;
	}

	public boolean isReverse() {
		return mReverse;
	}

	public void setReverse(boolean pReverse) {
		mReverse = pReverse;
	}

	public float getDuration() {
		return mDuration;
	}

	public float getProgress() {
		return mProgress;
	}
	
	public float getTime() {
		return mTime;
	}
	
	public void setTime(float pTime){
		if(pTime < 0 || pTime > mDuration){
			throw new IllegalArgumentException("pTime must be greater than zero and less then the timer duration");
		}
		updateProgress(mReverse ? mTime - pTime : pTime - mTime);
	}
	
	public boolean hasFinished(){
		return mProgress >= 1;
	}
	
	public boolean hasStarted(){
		return mProgress > 0;
	}
	
	/*========================
	/////INNER CLASSES////////
	========================*/
	public static enum RepetitionMode{
		NONE, LOOP, ALTERNATE
	}
}
