package com.arca.core.properties;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public interface ITransformable {
/*========================
////////CONSTANTS/////////
========================*/

/*=======================
 //////////METHODS////////
 =======================*/
	/**
	 * This method copies a given {@link ITransformable}
	 * 
	 * @param pOther The {@link ITransformable} to be copied
	 */
	public void copy(ITransformable pOther);
	
	/**
	 * Transforms the given {@link Vector3} by the local transformation matrix
	 * @param pVector The {@link Vector3} to be transformed
	 */
	public void transformLocal(Vector3 pVector);
	
	/**
	 * Transforms the given array of {@link Vector3} by the local transformation matrix
	 * @param pVectors The array of {@link Vector3} to be transformed
	 */
	public void transformLocal(Vector3[] pVectors);
	
	/**
	 * Transforms the given {@link Vector3} by the local normal matrix
	 * @param pVector The {@link Vector3} to be transformed
	 */
	public void transformLocalNormal(Vector3 pVector);
	
	/**
	 * Transforms the given array of {@link Vector3} by the local normal matrix
	 * @param pVectors The array of {@link Vector3} to be transformed
	 */
	public void transformLocalNormal(Vector3[] pVectors);
	
	/**
	 * Transforms the given {@link Matrix4} by the local transformation matrix
	 * @param pMatrix The {@link Matrix4} to be transformed
	 */
	public void transformLocal(Matrix4 pMatrix);
	
	/**
	 * Transforms the given array of {@link Matrix4} by the local transformation matrix
	 * @param pMatrices The array of {@link Matrix4} to be transformed
	 */
	public void transformLocal(Matrix4[] pMatrices);
	
	/**
	 * Transforms the given {@link Matrix4} by the local normal matrix
	 * @param pMatrix The {@link Matrix4} to be transformed
	 */
	public void transformLocalNormal(Matrix3 pMatrix);
	
	/**
	 * Transforms the given array of {@link Matrix4} by the local normal matrix
	 * @param pMatrices The {@link Matrix4} to be transformed
	 */
	public void transformLocalNormal(Matrix3[] pMatrices);
	
	
	/**
	 * Transforms the given {@link Vector3} by the world transformation matrix
	 * @param pVector The {@link Vector3} to be transformed
	 */
	public void transformWorld(Vector3 pVector);
	
	/**
	 * Transforms the given array of {@link Vector3} by the world transformation matrix
	 * @param pVectors The array of {@link Vector3} to be transformed
	 */
	public void transformWorld(Vector3[] pVectors);
	
	/**
	 * Transforms the given {@link Vector3} by the world normal matrix
	 * @param pVector The {@link Vector3} to be transformed
	 */
	public void transformWorldNormal(Vector3 pVector);
	
	/**
	 * Transforms the given array of {@link Vector3} by the world normal matrix
	 * @param pVectors The array of {@link Vector3} to be transformed
	 */
	public void transformWorldNormal(Vector3[] pVectors);
	
	/**
	 * Transforms the given {@link Matrix4} by the world transformation matrix
	 * @param pMatrix The {@link Matrix4} to be transformed
	 */
	public void transformWorld(Matrix4 pMatrix);
	
	/**
	 * Transforms the given array of {@link Matrix4} by the world transformation matrix
	 * @param pMatrices The array of {@link Matrix4} to be transformed
	 */
	public void transformWorld(Matrix4[] pMatrices);
	
	/**
	 * Transforms the given {@link Matrix4} by the world normal matrix
	 * @param pMatrix The {@link Matrix4} to be transformed
	 */
	public void transformWorldNormal(Matrix3 pMatrix);
	
	/**
	 * Transforms the given array of {@link Matrix4} by the world normal matrix
	 * @param pMatrices The {@link Matrix4} to be transformed
	 */
	public void transformWorldNormal(Matrix3[] pMatrices);
	
/*========================
 ////GETTERS & SETTERS////
 ========================*/
	public void setLocal(Vector3 pPosition, Vector3 pScale, Quaternion pRotation);
	
	public void setWorld(Vector3 pPosition, Vector3 pScale, Quaternion pRotation);
 
	/**
	 * @return A copy of the local transformation matrix.
	 */
	public Matrix4 getLocalMatrix(Matrix4 pTarget);
	/**
	 * @return A copy of the transformation matrix relative to world coordinates.
	 */
	public Matrix4 getWorldMatrix(Matrix4 pTarget);
	/**
	 * @return A copy of the normal transformation matrix relative to local coordinates.
	 */
	public Matrix3 getLocalNormalMatrix(Matrix3 pTarget);
	/**
	 * @return A copy of the normal transformation matrix relative to world coordinates.
	 */
	public Matrix3 getWorldNormalMatrix(Matrix3 pTarget);
	
	public float getLocalPositionX();
	public float getLocalPositionY();
	public float getLocalPositionZ();
	/**
	 * @param pTarget a {@link Vector3} used store a copy of the values from the local translation.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Vector3 getLocalPosition(Vector3 pTarget);
	public float getWorldPositionX();
	public float getWorldPositionY();
	public float getWorldPositionZ();
	/**
	 * @param pTarget a {@link Vector3} used store a copy of the values from the world translation.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Vector3 getWorldPosition(Vector3 pTarget);
	
	public float getLocalScaleX();
	public float getLocalScaleY();
	public float getLocalScaleZ();
	/**
	 * @param pTarget a {@link Vector3} used store a copy of the values from the local scaling.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Vector3 getLocalScale(Vector3 pTarget);
	public float getWorldScaleX();
	public float getWorldScaleY();
	public float getWorldScaleZ();
	/**
	 * @param pTarget a {@link Vector3} used store a copy of the values from the world scaling.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Vector3 getWorldScale(Vector3 pTarget);
	
	public float getLocalRotationPitch();
	public float getLocalRotationRoll();
	public float getLocalRotationYaw();
	public float getLocalRotationX();
	public float getLocalRotationY();
	public float getLocalRotationZ();
	/**
	 * Gets the local angle, in degrees, around the given axis.
	 * @param pX the x axis component value normalized.
	 * @param pY the y axis component value normalized.
	 * @param pZ the z axis component value normalized.
	 * @return the angle in degrees.
	 */
	public float getLocalRotation(float pX, float pY, float pZ);
	/**
	 * Gets the local angle, in degrees, around the given axis.
	 * @param pAxis the normalized {@link Vector3} axis.
	 * @return the angle in degrees.
	 */
	public float getLocalRotation(Vector3 pAxis);
	/**
	 * @param pTarget a {@link Quaternion} used store a copy of the values from the local rotation.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Quaternion getLocalRotation(Quaternion pTarget);
	
	public float getWorldRotationPitch();
	public float getWorldRotationRoll();
	public float getWorldRotationYaw();
	public float getWorldRotationX();
	public float getWorldRotationY();
	public float getWorldRotationZ();
	/**
	 * Gets the world angle, in degrees, around the given axis.
	 * @param pX the x axis component value normalized.
	 * @param pY the y axis component value normalized.
	 * @param pZ the z axis component value normalized.
	 * @return the angle in degrees.
	 */
	public float getWorldRotation(float pX, float pY, float pZ);
	/**
	 * Gets the world angle, in degrees, around the given axis.
	 * @param pAxis the normalized {@link Vector3} axis.
	 * @return the angle in degrees.
	 */
	public float getWorldRotation(Vector3 pAxis);
	/**
	 * @param pTarget a {@link Quaternion} used store a copy of the values from the world rotation.
	 * @return <b>pTarget</b> for chaining purposes.
	 */
	public Quaternion getWorldRotation(Quaternion pTarget);
	
	public void setLocalPositionX(float pX);
	public void setLocalPositionY(float pY);
	public void setLocalPositionZ(float pZ);
	public void setLocalPosition(float pX, float pY, float pZ);
	/**
	 * @param pVector3 The vector holding the position to be copied.
	 */
	public void setLocalPosition(Vector3 pVector3);
	public void setWorldPositionX(float pX);
	public void setWorldPositionY(float pY);
	public void setWorldPositionZ(float pZ);
	public void setWorldPosition(float pX, float pY, float pZ);
	/**
	 * @param pVector3 The vector holding the position to be copied.
	 */
	public void setWorldPosition(Vector3 pVector3);
	
	public void setLocalScaleX(float pX);
	public void setLocalScaleY(float pY);
	public void setLocalScaleZ(float pZ);
	public void setLocalScale(float pX, float pY, float pZ);
	/**
	 * @param pVector3 The vector holding the scales to be copied.
	 */
	public void setLocalScale(Vector3 pVector3);
	public void setWorldScaleX(float pX);
	public void setWorldScaleY(float pY);
	public void setWorldScaleZ(float pZ);
	public void setWorldScale(float pX, float pY, float pZ);
	/**
	 * @param pVector3 The vector holding the scale to be copied.
	 */
	public void setWorldScale(Vector3 pVector3);

	public void setLocalRotationPitch(float pPitch);
	public void setLocalRotationRoll(float pRoll);
	public void setLocalRotationYaw(float pYaw);
	public void setLocalRotation(float pPitch, float pRoll, float pYaw);
	public void setLocalRotationX(float pAngle);
	public void setLocalRotationY(float pAngle);
	public void setLocalRotationZ(float pAngle);
	public void setLocalRotation(float pX, float pY, float pZ, float pAngle);
	public void setLocalRotation(Vector3 pAxis, float pAngle);
	public void setLocalRotation(Quaternion pRotation);
	
	public void setWorldRotationPitch(float pPitch);
	public void setWorldRotationRoll(float pRoll);
	public void setWorldRotationYaw(float pYaw);
	public void setWorldRotation(float pPitch, float pRoll, float pYaw);
	public void setWorldRotationX(float pAngle);
	public void setWorldRotationY(float pAngle);
	public void setWorldRotationZ(float pAngle);
	public void setWorldRotation(float pX, float pY, float pZ, float pAngle);
	public void setWorldRotation(Vector3 pAxis, float pAngle);
	public void setWorldRotation(Quaternion pRotation);
	
	public Vector3 getRightVector(Vector3 pTarget);
	public Vector3 getUpVector(Vector3 pTarget);
	public Vector3 getForwardVector(Vector3 pTarget);
	
/*========================
 /////INNER INTERFACES/////
 ========================*/

}
