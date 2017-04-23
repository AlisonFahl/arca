package com.arca.core.scenenode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.arca.core.properties.ITransformable;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class SceneNode<T extends SceneNode<T>> implements ITransformable{
	/*========================
	////FIELDS & CONSTANTS////
	========================*/
	private Transformation mTransformation;
	
	private ArrayList<T> mChildrenList;
	private List<T> mImutableChildrenList;
	private T mParent;

	/*========================
	///////CONSTRUCTORS///////
	========================*/
	public SceneNode(){
		mChildrenList = new ArrayList<T>();
		mImutableChildrenList = Collections.unmodifiableList(mChildrenList);
		
		mTransformation = new Transformation(){
			@Override
			protected void updateWorldTransform() {
				super.updateWorldTransform();
				for(int i=0; i<mChildrenList.size(); i++){
					((SceneNode<T>)(mChildrenList.get(i))).mTransformation.updateWorldTransform();
				}
			}
		};
	}
	
	/*=======================
	//////////METHODS////////
	=======================*/
	public void addChild(T pChild){
		SceneNode<T> child = pChild;
		
		if(child.mParent != null){
			((SceneNode<T>)child.mParent).mChildrenList.remove(pChild);
		}
		mChildrenList.add(pChild);
		child.setParent(this);
	}
	
	public void removeChild(T pChild){
		if(!mChildrenList.remove(pChild)){
			throw new IllegalArgumentException("SceneNode child not found");
		}
		((SceneNode<T>)(pChild)).setParent(null);
	}
	
	public void removeAllChildren(){
		for(int i=0; i<mChildrenList.size(); i++){
			((SceneNode<T>)(mChildrenList.get(i))).setParent(null);
		}
		mChildrenList.clear();
	}
	
	/**
	 * Broadcasts an operation in all sub nodes of this {@link SceneNode}
	 * 
	 * @param pApply The operation you want to perform on each element
	 */
	public void broadcast(Consumer<T> pApply){
		if(!mChildrenList.isEmpty()){
			//TODO: do a benchmark comparing ArrayDeque stack vs ArrayList w/ no item removal (just item addition and index incrementation)
			ArrayDeque<T> mNodesStack = new ArrayDeque<T>(mChildrenList);
			
			//loop while there is nodes to be iterated
			while(!mNodesStack.isEmpty()){
				//gets next node of the stack
				T node = mNodesStack.pop();
				//perform operation on this node
				pApply.accept(node);
				//check if the has any child and adds them to the end of the stack
				if(!((SceneNode<T>)node).mChildrenList.isEmpty()){
					mNodesStack.addAll(((SceneNode<T>)node).mChildrenList);
				}
			}
		}
	}
	
	/**
	 * Broadcasts an operation in all sub nodes of this {@link SceneNode} which matches the given type
	 * 
	 * @param pType The class type of object you want to target.
	 * @param pApply The operation you want to perform on each element of the given type
	 */
	public <U> void broadcast(Class<U> pType, Consumer<U> pApply){
		if(!mChildrenList.isEmpty()){
			//TODO: do a benchmark comparing ArrayDeque stack vs ArrayList w/ no item removal (just item addition and index incrementation)
			ArrayDeque<T> mNodesStack = new ArrayDeque<T>(mChildrenList);
			
			//loop while there is nodes to be iterated
			while(!mNodesStack.isEmpty()){
				//gets next node of the stack
				T node = mNodesStack.pop();
				//check if the node belongs to the given type
				if(pType.isInstance(node)){
					@SuppressWarnings("unchecked")
					U instance = (U) node;
					//perform operation on this node
					pApply.accept(instance);
				}
				//check if the has any child and adds them to the end of the stack
				if(!((SceneNode<T>)node).mChildrenList.isEmpty()){
					mNodesStack.addAll(((SceneNode<T>)node).mChildrenList);
				}
			}
		}
	}
	
	/**
	 * Fetches all sub nodes of this {@link SceneNode} that satisfy the given predicate. The results are stored level by level (layer by layer) from this point of the scene tree, in a given {@link ArrayList}.
	 * 
	 * @param pPredicate The predicate used to test
	 * @return pResults: fetched nodes
	 */
	public ArrayList<T> fetch(Predicate<T> pPredicate){
		ArrayList<T> results = new ArrayList<T>();
		
		//1 - add all children from this node
		results.addAll(mChildrenList);
		
		//2 - starts fetching recursively, level by level (layer by layer) of the scene tree
		fetchRecursively(results, pPredicate, mChildrenList);
		
		return results;
	}
	
	/**
	 * Fetches all sub nodes of this {@link SceneNode}. The results are stored level by level (layer by layer) from this point of the scene tree, in a given {@link ArrayList}. 
	 * 
	 * @return pResults: fetched nodes
	 */
	public ArrayList<T> fetchAll(){
		ArrayList<T> results = new ArrayList<T>();
		
		//1 - add all children from this node
		results.addAll(mChildrenList);
		
		//2 - starts fetching recursively, level by level (layer by layer) of the scene tree
		fetchRecursively(results, null, mChildrenList);
		
		return results;
	}
	
	private void fetchRecursively(List<T> pResults, Predicate<T> pPredicate, List<T> pTreeLevel){
		//1 - prepare next level list
		ArrayList<T> nextLevel = new ArrayList<T>();
		
		//2 - add all children from every node in this current level to the results and nextLevel lists
		for(int i=0; i<pTreeLevel.size(); i++){
			SceneNode<T> node = pTreeLevel.get(i);
			
			if(!node.mChildrenList.isEmpty()){
				//if there is no predicate, fetch all
				if(pPredicate == null){
					pResults.addAll(node.mChildrenList);
				}
				//apply predicate on each child node otherwise
				else{
					//pResults.addAll(node.mChildrenList.stream().filter(pPredicate).collect(Collectors.toList()));// Java 8 solution (TODO: check Stream.concat)
					for(int j=0; j<node.mChildrenList.size(); j++){//Default Solution
						T child = node.mChildrenList.get(j);
						if(pPredicate.test(child)){
							pResults.add(child);
						}
					}
				}
				
				nextLevel.addAll(node.mChildrenList);
			}
		}
		
		//3 - if there is any node in the next level, continues the recursion
		if(!nextLevel.isEmpty()){
			fetchRecursively(pResults, pPredicate, nextLevel);
		}
	}

	@Override
	public void copy(ITransformable pOther) {
		mTransformation.copy(pOther);
	}

	@Override
	public void transformLocal(Vector3 pVector) {
		mTransformation.transformLocal(pVector);
	}

	@Override
	public void transformLocal(Vector3[] pVectors) {
		mTransformation.transformLocal(pVectors);
	}

	@Override
	public void transformLocal(Matrix4 pMatrix) {
		mTransformation.transformLocal(pMatrix);
	}

	@Override
	public void transformLocal(Matrix4[] pMatrices) {
		mTransformation.transformLocal(pMatrices);
	}

	@Override
	public void transformWorld(Vector3 pVector) {
		mTransformation.transformWorld(pVector);
	}

	@Override
	public void transformWorld(Vector3[] pVectors) {
		mTransformation.transformWorld(pVectors);
	}

	@Override
	public void transformWorld(Matrix4 pMatrix) {
		mTransformation.transformWorld(pMatrix);
	}

	@Override
	public void transformWorld(Matrix4[] pMatrices) {
		mTransformation.transformWorld(pMatrices);
	}
	
	@Override
	public void transformLocalNormal(Vector3 pVector) {
		mTransformation.transformLocalNormal(pVector);
	}
	
	@Override
	public void transformLocalNormal(Vector3[] pVectors) {
		mTransformation.transformLocalNormal(pVectors);
	}
	
	@Override
	public void transformWorldNormal(Vector3 pVector) {
		mTransformation.transformWorldNormal(pVector);
	}
	
	@Override
	public void transformWorldNormal(Vector3[] pVectors) {
		mTransformation.transformWorldNormal(pVectors);
	}

	/*========================
	 ////GETTERS & SETTERS////
	========================*/
	public T getParent(){
		return mParent;
	}
	
	@SuppressWarnings("unchecked")
	private void setParent(SceneNode<T> pParent){
		mParent = (T)pParent;
		
		if(mParent == null){
			mTransformation.setParent(null);
		}
		else{
			mTransformation.setParent(pParent.mTransformation);
		}
	}
	
	/**
	 * 
	 * @return a read-only reference to the children collection
	 */
	public List<T> getChildren(){
		return mImutableChildrenList;
	}
	
	public boolean hasParent(){
		return (mParent == null);
	}

	@Override
	public Matrix4 getLocalMatrix(Matrix4 pTarget) {
		return mTransformation.getLocalMatrix(pTarget);
	}

	@Override
	public Matrix4 getWorldMatrix(Matrix4 pTarget) {
		return mTransformation.getWorldMatrix(pTarget);
	}

	@Override
	public float getLocalPositionX() {
		return mTransformation.getLocalPositionX();
	}

	@Override
	public float getLocalPositionY() {
		return mTransformation.getLocalPositionY();
	}

	@Override
	public float getLocalPositionZ() {
		return mTransformation.getLocalPositionZ();
	}

	@Override
	public Vector3 getLocalPosition(Vector3 pTarget) {
		return mTransformation.getLocalPosition(pTarget);
	}

	@Override
	public float getWorldPositionX() {
		return mTransformation.getWorldPositionX();
	}

	@Override
	public float getWorldPositionY() {
		return mTransformation.getWorldPositionY();
	}

	@Override
	public float getWorldPositionZ() {
		return mTransformation.getWorldPositionZ();
	}

	@Override
	public Vector3 getWorldPosition(Vector3 pTarget) {
		return mTransformation.getWorldPosition(pTarget);
	}

	@Override
	public float getLocalScaleX() {
		return mTransformation.getLocalScaleX();
	}

	@Override
	public float getLocalScaleY() {
		return mTransformation.getLocalScaleY();
	}

	@Override
	public float getLocalScaleZ() {
		return mTransformation.getLocalScaleZ();
	}

	@Override
	public Vector3 getLocalScale(Vector3 pTarget) {
		return mTransformation.getLocalScale(pTarget);
	}

	@Override
	public float getWorldScaleX() {
		return mTransformation.getWorldScaleX();
	}

	@Override
	public float getWorldScaleY() {
		return mTransformation.getWorldScaleY();
	}

	@Override
	public float getWorldScaleZ() {
		return mTransformation.getWorldScaleZ();
	}

	@Override
	public Vector3 getWorldScale(Vector3 pTarget) {
		return mTransformation.getWorldScale(pTarget);
	}

	@Override
	public Quaternion getLocalRotation(Quaternion pTarget) {
		return mTransformation.getLocalRotation(pTarget);
	}

	@Override
	public Quaternion getWorldRotation(Quaternion pTarget) {
		return mTransformation.getWorldRotation(pTarget);
	}

	@Override
	public void setLocalPositionX(float pX) {
		mTransformation.setLocalPositionX(pX);
	}

	@Override
	public void setLocalPositionY(float pY) {
		mTransformation.setLocalPositionY(pY);
	}

	@Override
	public void setLocalPositionZ(float pZ) {
		mTransformation.setLocalPositionZ(pZ);
	}

	@Override
	public void setLocalPosition(float pX, float pY, float pZ) {
		mTransformation.setLocalPosition(pX, pY, pZ);
	}

	@Override
	public void setLocalPosition(Vector3 pVector3) {
		mTransformation.setLocalPosition(pVector3);
	}

	@Override
	public void setWorldPositionX(float pX) {
		mTransformation.setWorldPositionX(pX);
	}

	@Override
	public void setWorldPositionY(float pY) {
		mTransformation.setWorldPositionY(pY);
	}

	@Override
	public void setWorldPositionZ(float pZ) {
		mTransformation.setWorldPositionZ(pZ);
	}

	@Override
	public void setWorldPosition(float pX, float pY, float pZ) {
		mTransformation.setWorldPosition(pX, pY, pZ);
	}

	@Override
	public void setWorldPosition(Vector3 pVector3) {
		mTransformation.setWorldPosition(pVector3);
	}

	@Override
	public void setLocalScaleX(float pX) {
		mTransformation.setLocalScaleX(pX);
	}

	@Override
	public void setLocalScaleY(float pY) {
		mTransformation.setLocalScaleY(pY);
	}

	@Override
	public void setLocalScaleZ(float pZ) {
		mTransformation.setLocalScaleZ(pZ);
	}

	@Override
	public void setLocalScale(float pX, float pY, float pZ) {
		mTransformation.setLocalScale(pX, pY, pZ);
	}

	@Override
	public void setLocalScale(Vector3 pVector3) {
		mTransformation.setLocalScale(pVector3);
	}

	@Override
	public void setWorldScaleX(float pX) {
		mTransformation.setWorldScaleX(pX);
	}

	@Override
	public void setWorldScaleY(float pY) {
		mTransformation.setWorldScaleY(pY);
	}

	@Override
	public void setWorldScaleZ(float pZ) {
		mTransformation.setWorldScaleZ(pZ);
	}

	@Override
	public void setWorldScale(float pX, float pY, float pZ) {
		mTransformation.setWorldScale(pX, pY, pZ);
	}

	@Override
	public void setWorldScale(Vector3 pVector3) {
		mTransformation.setWorldScale(pVector3);
	}

	@Override
	public void setLocalRotation(Quaternion pQuaternion) {
		mTransformation.setLocalRotation(pQuaternion);
	}

	@Override
	public void setWorldRotation(Quaternion pQuaternion) {
		mTransformation.setWorldRotation(pQuaternion);
	}

	@Override
	public Vector3 getRightVector(Vector3 pTarget) {
		return mTransformation.getRightVector(pTarget);
	}

	@Override
	public Vector3 getUpVector(Vector3 pTarget) {
		return mTransformation.getUpVector(pTarget);
	}
	
	@Override
	public Vector3 getForwardVector(Vector3 pTarget) {
		return mTransformation.getForwardVector(pTarget);
	}

	@Override
	public void transformLocalNormal(Matrix3 pMatrix) {
		mTransformation.transformLocalNormal(pMatrix);
	}

	@Override
	public void transformLocalNormal(Matrix3[] pMatrices) {
		mTransformation.transformLocalNormal(pMatrices);
	}

	@Override
	public void transformWorldNormal(Matrix3 pMatrix) {
		mTransformation.transformWorldNormal(pMatrix);
	}

	@Override
	public void transformWorldNormal(Matrix3[] pMatrices) {
		mTransformation.transformWorldNormal(pMatrices);
	}

	@Override
	public Matrix3 getLocalNormalMatrix(Matrix3 pTarget) {
		return mTransformation.getLocalNormalMatrix(pTarget);
	}

	@Override
	public Matrix3 getWorldNormalMatrix(Matrix3 pTarget) {
		return mTransformation.getWorldNormalMatrix(pTarget);
	}
	
	@Override
	public void setLocal(Vector3 pPosition, Vector3 pScale, Quaternion pRotation) {
		mTransformation.setLocal(pPosition, pScale, pRotation);
	}
	
	@Override
	public void setWorld(Vector3 pPosition, Vector3 pScale, Quaternion pRotation) {
		mTransformation.setWorld(pPosition, pScale, pRotation);
	}
	
	@Override
	public float getLocalRotationPitch() {
		return mTransformation.getLocalRotationPitch();
	}
	
	@Override
	public float getLocalRotationRoll() {
		return mTransformation.getLocalRotationRoll();
	}
	
	@Override
	public float getLocalRotationYaw() {
		return mTransformation.getLocalRotationYaw();
	}
	
	@Override
	public float getWorldRotationPitch() {
		return mTransformation.getWorldRotationPitch();
	}
	
	@Override
	public float getWorldRotationRoll() {
		return mTransformation.getWorldRotationRoll();
	}
	
	@Override
	public float getWorldRotationYaw() {
		return mTransformation.getWorldRotationYaw();
	}
	
	@Override
	public void setLocalRotationPitch(float pPitch) {
		mTransformation.setLocalRotationPitch(pPitch);
	}
	
	@Override
	public void setLocalRotationRoll(float pRoll) {
		mTransformation.setLocalRotationRoll(pRoll);
	}
	
	@Override
	public void setLocalRotationYaw(float pYaw) {
		mTransformation.setLocalRotationYaw(pYaw);
	}
	
	@Override
	public void setLocalRotation(float pPitch, float pRoll, float pYaw) {
		mTransformation.setLocalRotation(pPitch, pRoll, pYaw);
	}
	
	@Override
	public void setWorldRotationPitch(float pPitch) {
		mTransformation.setWorldRotationPitch(pPitch);
	}
	
	@Override
	public void setWorldRotationRoll(float pRoll) {
		mTransformation.setWorldRotationRoll(pRoll);
	}
	
	@Override
	public void setWorldRotationYaw(float pYaw) {
		mTransformation.setWorldRotationYaw(pYaw);
	}
	
	@Override
	public void setWorldRotation(float pPitch, float pRoll, float pYaw) {
		mTransformation.setWorldRotation(pPitch, pRoll, pYaw);
	}
	
	public float getLocalRotationX() {
		return mTransformation.getLocalRotationX();
	}

	public float getLocalRotationY() {
		return mTransformation.getLocalRotationY();
	}

	public float getLocalRotationZ() {
		return mTransformation.getLocalRotationZ();
	}

	public float getLocalRotation(float pX, float pY, float pZ) {
		return mTransformation.getLocalRotation(pX, pY, pZ);
	}

	public float getLocalRotation(Vector3 pAxis) {
		return mTransformation.getLocalRotation(pAxis);
	}

	public float getWorldRotationX() {
		return mTransformation.getWorldRotationX();
	}

	public float getWorldRotationY() {
		return mTransformation.getWorldRotationY();
	}

	public float getWorldRotationZ() {
		return mTransformation.getWorldRotationZ();
	}

	public float getWorldRotation(float pX, float pY, float pZ) {
		return mTransformation.getWorldRotation(pX, pY, pZ);
	}

	public float getWorldRotation(Vector3 pAxis) {
		return mTransformation.getWorldRotation(pAxis);
	}

	public void setLocalRotationX(float pAngle) {
		mTransformation.setLocalRotationX(pAngle);
	}

	public void setLocalRotationY(float pAngle) {
		mTransformation.setLocalRotationY(pAngle);
	}

	public void setLocalRotationZ(float pAngle) {
		mTransformation.setLocalRotationZ(pAngle);
	}

	public void setLocalRotation(float pX, float pY, float pZ, float pAngle) {
		mTransformation.setLocalRotation(pX, pY, pZ, pAngle);
	}

	public void setLocalRotation(Vector3 pAxis, float pAngle) {
		mTransformation.setLocalRotation(pAxis, pAngle);
	}

	public void setWorldRotationX(float pAngle) {
		mTransformation.setWorldRotationX(pAngle);
	}

	public void setWorldRotationY(float pAngle) {
		mTransformation.setWorldRotationY(pAngle);
	}

	public void setWorldRotationZ(float pAngle) {
		mTransformation.setWorldRotationZ(pAngle);
	}

	public void setWorldRotation(float pX, float pY, float pZ, float pAngle) {
		mTransformation.setWorldRotation(pX, pY, pZ, pAngle);
	}

	public void setWorldRotation(Vector3 pAxis, float pAngle) {
		mTransformation.setWorldRotation(pAxis, pAngle);
	}

	/*========================
	/////NESTED CLASSES////////
	========================*/
	
}
