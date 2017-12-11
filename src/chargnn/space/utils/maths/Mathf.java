package chargnn.space.utils.maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Mathf {

	public static float PI = (float) Math.PI;
	
	public static Matrix4f createTransformationMatrix(Vector3f transformation, Vector3f rotation, float scale)
	{
		Matrix4f mat = new Matrix4f();
		
		mat.setIdentity();
		Matrix4f.translate(transformation, mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.getX()), new Vector3f(1, 0, 0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.getY()), new Vector3f(0, 1, 0), mat, mat);
		Matrix4f.rotate((float) Math.toRadians(rotation.getZ()), new Vector3f(0, 0, 1), mat, mat);
		Matrix4f.scale(new Vector3f(scale, scale, scale),  mat, mat);
		
		return mat;
	}
	
	public static float sin(float angle)
	{
		return (float) Math.sin(angle);
	}
	
	public static float cos(float angle)
	{
		return (float) Math.sin(angle);
	}
}
