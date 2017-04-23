package com.arca.core.scenenode;

import com.arca.core.properties.ITransformable;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

class Transformation implements ITransformable{
	
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	
	//Properties corresponding to the current state of the transformation
	//local
	private Vector3 mLocalTranslation = new Vector3(), mLocalScaling = new Vector3(1, 1, 1);
	private Quaternion mLocalRotation = new Quaternion();
	//world
	private Vector3 mWorldTranslation = new Vector3(), mWorldScaling = new Vector3(1, 1, 1);
	private Quaternion mWorldRotation = new Quaternion();
	
	private Vector3 mRightVector = new Vector3(1, 0, 0), mUpVector = new Vector3(0, 1, 0), mForwardVector = new Vector3(0, 0, 1);
	
	private Matrix4 mLocalTransformation = new Matrix4(), mWorldTransformation = new Matrix4();
	private Matrix3 mLocalNormalTransformation = new Matrix3(), mWorldNormalTransformation = new Matrix3();
	
	private Transformation mParent;

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public Transformation(){
		
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	private void updateLocalTransform(){
		mLocalTransformation.idt();
		
		mLocalTransformation.set(mLocalTranslation, mLocalRotation, mLocalScaling);
		
		mLocalNormalTransformation.set(mLocalTransformation);
		
		updateWorldTransform();
	}
	
	protected void updateWorldTransform(){
		if(hasParent()){
			mWorldTransformation.set(mParent.mWorldTransformation).mul(mLocalTransformation);
		}
		else{
			mWorldTransformation.set(mLocalTransformation);
		}
		
		mWorldNormalTransformation.set(mWorldTransformation);
		
		mWorldTransformation.getTranslation(mWorldTranslation);
		mWorldTransformation.getRotation(mWorldRotation, true);
		mWorldTransformation.getScale(mWorldScaling);
		
		mRightVector.set(mWorldTransformation.val[Matrix4.M00], mWorldTransformation.val[Matrix4.M10], mWorldTransformation.val[Matrix4.M20]).nor();
		mUpVector.set(mWorldTransformation.val[Matrix4.M01], mWorldTransformation.val[Matrix4.M11], mWorldTransformation.val[Matrix4.M21]).nor();
		mForwardVector.set(mWorldTransformation.val[Matrix4.M02], mWorldTransformation.val[Matrix4.M12], mWorldTransformation.val[Matrix4.M22]).nor();
	}

	@Override
	public void copy(ITransformable pOther){
		pOther.getLocalPosition(mLocalTranslation);
		pOther.getLocalScale(mLocalScaling);
		pOther.getLocalRotation(mLocalRotation);
		
		updateLocalTransform();
	}

	@Override
	public void transformLocal(Vector3 pVector){
		pVector.mul(mLocalTransformation);
	}

	@Override
	public void transformLocal(Vector3[] pVectors){
		for(int i=0; i<pVectors.length; i++){
			pVectors[i].mul(mLocalTransformation);
		}
	}
	
	@Override
	public void transformLocalNormal(Vector3 pVector){
		pVector.mul(mLocalNormalTransformation);
	}
	
	@Override
	public void transformLocalNormal(Vector3[] pVectors){
		for(int i=0; i<pVectors.length; i++){
			pVectors[i].mul(mLocalNormalTransformation);
		}
	}

	@Override
	public void transformLocal(Matrix4 pMatrix){
		pMatrix.mul(mLocalTransformation);
	}

	@Override
	public void transformLocal(Matrix4[] pMatrices){
		for(int i=0; i<pMatrices.length; i++){
			pMatrices[i].mul(mLocalTransformation);
		}
	}
	
	@Override
	public void transformLocalNormal(Matrix3 pMatrix) {
		pMatrix.mul(mLocalNormalTransformation);
	}

	@Override
	public void transformLocalNormal(Matrix3[] pMatrices) {
		for(int i=0; i<pMatrices.length; i++){
			pMatrices[i].mul(mLocalNormalTransformation);
		}
	}

	@Override
	public void transformWorld(Vector3 pVector){
		pVector.mul(mWorldTransformation);
	}

	@Override
	public void transformWorld(Vector3[] pVectors){
		for(int i=0; i<pVectors.length; i++){
			pVectors[i].mul(mWorldTransformation);
		}
	}
	
	@Override
	public void transformWorldNormal(Vector3 pVector){
		pVector.mul(mWorldNormalTransformation);
	}

	@Override
	public void transformWorldNormal(Vector3[] pVectors){
		for(int i=0; i<pVectors.length; i++){
			pVectors[i].mul(mWorldNormalTransformation);
		}
	}

	@Override
	public void transformWorld(Matrix4 pMatrix){
		pMatrix.mul(mWorldTransformation);
	}

	@Override
	public void transformWorld(Matrix4[] pMatrices){
		for(int i=0; i<pMatrices.length; i++){
			pMatrices[i].mul(mWorldTransformation);
		}
	}
	
	@Override
	public void transformWorldNormal(Matrix3 pMatrix) {
		pMatrix.mul(mWorldNormalTransformation);
	}

	@Override
	public void transformWorldNormal(Matrix3[] pMatrices) {
		for(int i=0; i<pMatrices.length; i++){
			pMatrices[i].mul(mWorldNormalTransformation);
		}
	}
	
	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public Transformation getParent() {
		return mParent;
	}

	public void setParent(Transformation pParent) {
		mParent = pParent;
		updateWorldTransform();
	}
	
	@Override
	public void setLocal(Vector3 pPosition, Vector3 pScale, Quaternion pRotation){
		mLocalTranslation.set(pPosition);
		mLocalScaling.set(pScale);
		mLocalRotation.set(pRotation);
		updateLocalTransform();
	}
	
	@Override
	public void setWorld(Vector3 pPosition, Vector3 pScale, Quaternion pRotation){
		if(hasParent()){
			Matrix4 worldMatrix = new Matrix4(pPosition, pRotation, pScale).mul(mParent.mWorldTransformation.inv().cpy());
			worldMatrix.getTranslation(mLocalTranslation);
			worldMatrix.getRotation(mLocalRotation);
			worldMatrix.getScale(mLocalScaling);
			updateLocalTransform();
		}
		else{
			setLocal(pPosition, pScale, pRotation);
		}
	}
	
	/**
	 * @return A copy of the transformation matrix.
	 */
	@Override
	public Matrix4 getLocalMatrix(Matrix4 pTarget){
		return pTarget.set(mLocalTransformation);
	}
	
	/**
	 * @return A copy of the transformation matrix relative to world coordinates.
	 */
	@Override
	public Matrix4 getWorldMatrix(Matrix4 pTarget){
		return pTarget.set(mWorldTransformation);
	}
	
	@Override
	public Matrix3 getLocalNormalMatrix(Matrix3 pTarget) {
		return pTarget.set(mLocalNormalTransformation);
	}
	
	@Override
	public Matrix3 getWorldNormalMatrix(Matrix3 pTarget) {
		return pTarget.set(mWorldNormalTransformation);
	}

	@Override
	public float getLocalPositionX(){
		return mLocalTranslation.x;
	}

	@Override
	public float getLocalPositionY(){
		return mLocalTranslation.y;
	}

	@Override
	public float getLocalPositionZ(){
		return mLocalTranslation.z;
	}
	
	/**
	 * @return A copy of the translation vector.
	 */
	@Override
	public Vector3 getLocalPosition(Vector3 pTarget){
		return pTarget.set(mLocalTranslation);
	}

	@Override
	public float getWorldPositionX(){
		return mWorldTranslation.x;
	}

	@Override
	public float getWorldPositionY(){
		return mWorldTranslation.y;
	}

	@Override
	public float getWorldPositionZ(){
		return mWorldTranslation.z;
	}
	
	/**
	 * @return A vector containing the position translated to world coordinates.
	 */
	@Override
	public Vector3 getWorldPosition(Vector3 pTarget){
		return pTarget.set(mWorldTranslation);
	}

	@Override
	public float getLocalScaleX(){
		return mLocalScaling.x;
	}

	@Override
	public float getLocalScaleY(){
		return mLocalScaling.y;
	}

	@Override
	public float getLocalScaleZ(){
		return mLocalScaling.z;
	}
	
	/**
	 * @return a copy of the scaling vector containing the three axis scale values.
	 */
	@Override
	public Vector3 getLocalScale(Vector3 pTarget){
		return pTarget.set(mLocalScaling);
	}

	@Override
	public float getWorldScaleX(){
		return mWorldScaling.x;
	}

	@Override
	public float getWorldScaleY(){
		return mWorldScaling.y;
	}

	@Override
	public float getWorldScaleZ(){
		return mWorldScaling.z;
	}

	@Override
	public Vector3 getWorldScale(Vector3 pTarget){
		return pTarget.set(mWorldScaling);
	}
	
	@Override
	public float getLocalRotationPitch(){
		return mLocalRotation.getPitch();
	}
	
	@Override
	public float getLocalRotationRoll(){
		return mLocalRotation.getRoll();
	}
	
	@Override
	public float getLocalRotationYaw(){
		return mLocalRotation.getYaw();
	}
	
	
	/**
	 * @return A copy of the rotation quaternion containing
	 */
	@Override
	public Quaternion getLocalRotation(Quaternion pTarget){
		return pTarget.set(mLocalRotation);
	}
	
	@Override
	public float getWorldRotationPitch(){
		return mWorldRotation.getPitch();
	}
	
	@Override
	public float getWorldRotationRoll(){
		return mWorldRotation.getRoll();
	}
	
	@Override
	public float getWorldRotationYaw(){
		return mWorldRotation.getYaw();
	}
	
	@Override
	public Quaternion getWorldRotation(Quaternion pTarget){
		return pTarget.set(mWorldRotation);
	}

	private boolean hasParent(){
		return (mParent != null);
	}

	@Override
	public void setLocalPositionX(float pX){
		mLocalTranslation.x = pX;
		updateLocalTransform();
	}

	@Override
	public void setLocalPositionY(float pY){
		mLocalTranslation.y = pY;
		updateLocalTransform();
	}

	@Override
	public void setLocalPositionZ(float pZ){
		mLocalTranslation.z = pZ;
		updateLocalTransform();
	}

	@Override
	public void setLocalPosition(float pX, float pY, float pZ){
		mLocalTranslation.x = pX;
		mLocalTranslation.y = pY;
		mLocalTranslation.z = pZ;
		updateLocalTransform();
	}
	
	/**
	 * @param pVector3 The vector holding the position to be copied.
	 */
	@Override
	public void setLocalPosition(Vector3 pVector3){
		mLocalTranslation.set(pVector3);
		updateLocalTransform();
	}

	@Override
	public void setWorldPositionX(float pX){
		if(hasParent()){
			mLocalTranslation.x = pX;
			mLocalTranslation.mul(mParent.mWorldTransformation.cpy().inv());
			updateLocalTransform();
		}
		else{
			setLocalPositionX(pX);
		}
	}

	@Override
	public void setWorldPositionY(float pY){
		if(hasParent()){
			mLocalTranslation.y = pY;
			mLocalTranslation.mul(mParent.mWorldTransformation.cpy().inv());
			updateLocalTransform();
		}
		else{
			setLocalPositionY(pY);
		}
	}

	@Override
	public void setWorldPositionZ(float pZ){
		if(hasParent()){
			mLocalTranslation.z= pZ;
			mLocalTranslation.mul(mParent.mWorldTransformation.cpy().inv());
			updateLocalTransform();
		}
		else{
			setLocalPositionZ(pZ);
		}
	}

	@Override
	public void setWorldPosition(float pX, float pY, float pZ){
		if(hasParent()){
			mLocalTranslation.set(pX, pY, pZ);
			mLocalTranslation.mul(mParent.mWorldTransformation.cpy().inv());
			updateLocalTransform();
		}
		else{
			setLocalPosition(pX, pY, pZ);
		}
	}
	
	/**
	 * @param pVector3 The vector holding the position to be copied.
	 */
	@Override
	public void setWorldPosition(Vector3 pVector3){
		if(hasParent()){
			mLocalTranslation.set(pVector3);
			mLocalTranslation.mul(mParent.mWorldTransformation.cpy().inv());
			updateLocalTransform();
		}
		else{
			setLocalPosition(pVector3);
		}
	}

	@Override
	public void setLocalScaleX(float pX){
		mLocalScaling.x = pX;
		updateLocalTransform();
	}

	@Override
	public void setLocalScaleY(float pY){
		mLocalScaling.y = pY;
		updateLocalTransform();
	}

	@Override
	public void setLocalScaleZ(float pZ){
		mLocalScaling.z = pZ;
		updateLocalTransform();
	}

	@Override
	public void setLocalScale(float pX, float pY, float pZ){
		mLocalScaling.x = pX;
		mLocalScaling.y = pY;
		mLocalScaling.z = pZ;
		updateLocalTransform();
	}
	
	/**
	 * @param pVector3 The vector holding the scales to be copied.
	 */
	@Override
	public void setLocalScale(Vector3 pVector3){
		mLocalScaling.set(pVector3);
		updateLocalTransform();
	}

	@Override
	public void setWorldScaleX(float pX){
		if(hasParent()){
			float parentScaleX = mParent.mWorldScaling.x;
			if(parentScaleX != 0)
			{
				parentScaleX = (1f / parentScaleX) * pX;
			}
			setLocalScaleX(parentScaleX);
		}
		else{
			setLocalScaleX(pX);
		}
	}

	@Override
	public void setWorldScaleY(float pY){
		if(hasParent()){
			float parentScaleY = mParent.mWorldScaling.y;
			if(parentScaleY != 0)
			{
				parentScaleY = (1f / parentScaleY) * pY;
			}
			setLocalScaleY(parentScaleY);
		}
		else{
			setLocalScaleZ(pY);
		}
	}

	@Override
	public void setWorldScaleZ(float pZ){
		if(hasParent()){
			float parentScaleZ = mParent.mWorldScaling.z;
			if(parentScaleZ != 0)
			{
				parentScaleZ = (1f / parentScaleZ) * pZ;
			}
			setLocalScaleZ(parentScaleZ);
		}
		else{
			setLocalScaleZ(pZ);
		}
	}

	@Override
	public void setWorldScale(float pX, float pY, float pZ){
		if(hasParent()){
			Vector3 parentScale = mParent.mWorldScaling.cpy();
			if(!parentScale.isZero())
			{
				parentScale.set(1f / parentScale.x, 1f / parentScale.y, 1f / parentScale.z).scl(pX, pY, pZ);
			}
			setLocalScale(parentScale);
		}
		else{
			setLocalScale(pX, pY, pZ);
		}
	}
	
	/**
	 * @param pVector3 The vector holding the scale to be copied.
	 */
	@Override
	public void setWorldScale(Vector3 pVector3){
		if(hasParent()){
			Vector3 parentScale = mParent.mWorldScaling.cpy();
			if(!parentScale.isZero())
			{
				parentScale.set(1f / parentScale.x, 1f / parentScale.y, 1f / parentScale.z).scl(pVector3);
			}
			setLocalScale(parentScale);
		}
		else{
			setLocalScale(pVector3);
		}
	}
	
	@Override
	public void setLocalRotationPitch(float pPitch){
		setLocalRotation(pPitch, mLocalRotation.getRoll(), mLocalRotation.getYaw());
	}
	
	@Override
	public void setLocalRotationRoll(float pRoll){
		setLocalRotation(mLocalRotation.getPitch(), pRoll, mLocalRotation.getYaw());
	}
	
	@Override
	public void setLocalRotationYaw(float pYaw){
		setLocalRotation(mLocalRotation.getPitch(), mLocalRotation.getRoll(), pYaw);
	}
	
	@Override
	public void setLocalRotation(float pPitch, float pRoll, float pYaw){
		mLocalRotation.setEulerAngles(pYaw, pPitch, pRoll);
		updateLocalTransform();
	}
	
	/**
	 * @param pQuaternion The rotation holding the angle values in each axis. 
	 * The vector is copied, not referenced.
	 */
	@Override
	public void setLocalRotation(Quaternion pQuaternion){
		mLocalRotation.set(pQuaternion);
		updateLocalTransform();
	}
	
	@Override
	public void setWorldRotationPitch(float pPitch){
		setWorldRotation(pPitch, mWorldRotation.getRoll(), mWorldRotation.getYaw());
	}
	
	@Override
	public void setWorldRotationRoll(float pRoll){
		setWorldRotation(mWorldRotation.getPitch(), pRoll, mWorldRotation.getYaw());
	}
	
	@Override
	public void setWorldRotationYaw(float pYaw){
		setWorldRotation(mWorldRotation.getPitch(), mWorldRotation.getRoll(), pYaw);
	}
	
	@Override
	public void setWorldRotation(float pPitch, float pRoll, float pYaw){
		if(hasParent()){
			setWorldRotation(new Quaternion().setEulerAngles(pYaw, pPitch, pRoll));
		}
		else{
			setLocalRotation(pPitch, pRoll, pYaw);
		}
	}
	
	/**
	 * @param pQuaternion The rotation holding the angles to be copied.
	 */
	@Override
	public void setWorldRotation(Quaternion pQuaternion){
		if(hasParent()){
			Quaternion parentRotation = mParent.mWorldRotation.cpy().conjugate();
			parentRotation.mul(pQuaternion);
			setLocalRotation(parentRotation);
		}
		else{
			setLocalRotation(pQuaternion);
		}
	}

	@Override
	public Vector3 getRightVector(Vector3 pTarget) {
		return pTarget.set(mRightVector);
	}

	@Override
	public Vector3 getUpVector(Vector3 pTarget) {
		return pTarget.set(mUpVector);
	}

	@Override
	public Vector3 getForwardVector(Vector3 pTarget) {
		return pTarget.set(mForwardVector);
	}

	@Override
	public float getLocalRotationX() {
		return mLocalRotation.getAngleAround(1, 0, 0);
	}

	@Override
	public float getLocalRotationY() {
		return mLocalRotation.getAngleAround(0, 1, 0);
	}

	@Override
	public float getLocalRotationZ() {
		return mLocalRotation.getAngleAround(0, 0, 1);
	}

	@Override
	public float getLocalRotation(float pX, float pY, float pZ) {
		return mLocalRotation.getAngleAround(pX, pY, pZ);
	}

	@Override
	public float getLocalRotation(Vector3 pAxis) {
		return mLocalRotation.getAngleAround(pAxis);
	}

	@Override
	public float getWorldRotationX() {
		return mWorldRotation.getAngleAround(1, 0, 0);
	}

	@Override
	public float getWorldRotationY() {
		return mWorldRotation.getAngleAround(0, 1, 0);
	}

	@Override
	public float getWorldRotationZ() {
		return mWorldRotation.getAngleAround(0, 0, 1);
	}

	@Override
	public float getWorldRotation(float pX, float pY, float pZ) {
		return mWorldRotation.getAngleAround(pX, pY, pZ);
	}

	@Override
	public float getWorldRotation(Vector3 pAxis) {
		return mWorldRotation.getAngleAround(pAxis);
	}

	@Override
	public void setLocalRotationX(float pAngle) {
		setLocalRotation(1, 0, 0, pAngle);
	}

	@Override
	public void setLocalRotationY(float pAngle) {
		setLocalRotation(0, 1, 0, pAngle);
	}

	@Override
	public void setLocalRotationZ(float pAngle) {
		setLocalRotation(0, 0, 1, pAngle);
	}

	@Override
	public void setLocalRotation(float pX, float pY, float pZ, float pAngle) {
		mLocalRotation.setFromAxis(pX, pY, pZ, pAngle);
		updateLocalTransform();
	}

	@Override
	public void setLocalRotation(Vector3 pAxis, float pAngle) {
		mLocalRotation.setFromAxis(pAxis, pAngle);
		updateLocalTransform();
	}

	@Override
	public void setWorldRotationX(float pAngle) {
		setWorldRotation(1, 0, 0, pAngle);
	}

	@Override
	public void setWorldRotationY(float pAngle) {
		setWorldRotation(0, 1, 0, pAngle);
	}

	@Override
	public void setWorldRotationZ(float pAngle) {
		setWorldRotation(0, 0, 1, pAngle);
	}

	@Override
	public void setWorldRotation(float pX, float pY, float pZ, float pAngle) {
		setWorldRotation(new Vector3(pX, pY, pZ), pAngle);
	}

	@Override
	public void setWorldRotation(Vector3 pAxis, float pAngle) {
		if(hasParent()){
			setWorldRotation(new Quaternion(pAxis, pAngle));
		}
		else{
			setLocalRotation(pAxis, pAngle);
		}
	}
	
	/*========================
	/////INNER CLASSES////////
	========================*/
}
