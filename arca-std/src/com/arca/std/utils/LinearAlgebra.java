package com.arca.std.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public final class LinearAlgebra {
	
	private LinearAlgebra(){
		
	}
	
	/**
	 * @param pVertices - An array with all vertices from the shape
	 * @return An array containing the normal of each face
	 */
	public static Vector3[] getFaceNormals(Vector3[] pVertices){
		Vector3[] axes = new Vector3[pVertices.length];
		
		for(int i = 0, j = 1; i < pVertices.length; i++, j++){
			Vector3 v1, v2;
			
			v1 = new Vector3(pVertices[i]);
			v2 = new Vector3(pVertices[j == pVertices.length ? 0 : j]);
			
			axes[i] = v1.crs(v2).nor();
		}
		
		return axes;
	}
	
	/**
	 * Calculates the projection of vectorA into vectorB
	 * 
	 * @param pVectorA the 3D vector to be projected
	 * @param pVectorB the unit length 3D vector representing the axis
	 * @return The 3D projection vector
	 */
	public static Vector3 getProjection(Vector3 pVectorA, Vector3 pVectorB){
		/*
		    proj.x = ( dp / (b.x*b.x + b.y*b.y) ) * b.x;
			proj.y = ( dp / (b.x*b.x + b.y*b.y) ) * b.y;
			
			where dp is the dotprod of a and b: dp = (a.x*b.x + a.y*b.y)
			
			Note that the result is a vector; also, (b.x*b.x + b.y*b.y) is simply the length of b squared. 
			
			If b is a unit vector, (b.x*b.x + b.y*b.y) = 1, and thus a projected onto b reduces to: 
			
			proj.x = dp*b.x;
			proj.y = dp*b.y; 
		 */
		
		final float dotProduct = pVectorA.dot(pVectorB);
		
		return new Vector3(dotProduct * pVectorB.x, dotProduct * pVectorB.y, dotProduct * pVectorB.z);
	}
	
	/**
	 * Calculates the projection of vectorA into vectorB
	 * 
	 * @param pVectorA the 2D vector to be projected
	 * @param pVectorB the unit length 2D vector representing the axis
	 * @return The 2D projection vector
	 */
	public static Vector2 getProjection(Vector2 pVectorA, Vector2 pVectorB){
		final float dotProduct = pVectorA.dot(pVectorB);
		
		return new Vector2(dotProduct * pVectorB.x, dotProduct * pVectorB.y);
	}
}
