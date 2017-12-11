package chargnn.space.renderer;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import chargnn.space.main.Main;
import chargnn.space.utils.io.Keyboard;
import chargnn.space.utils.io.Mouse;

public class Camera {
	private float fov = 70.0f;
	private float zNear = 0.01f;
	private float  zFar = 1000.0f;
	
	private Vector3f position;
	private Vector3f rotation;
	
	private Matrix4f projectionMatrix;
	
	public Camera(Vector3f position, Vector3f rotation, float fov, float zNear, float zFar)
	{
		this.position = position;
		this.rotation = rotation;
		this.fov = fov;
		this.zNear = zNear;
		this.zFar = zFar;
	}

	public void move(int delta)
	{	
		if(Mouse.isLocked())
		{
			float mouseDX = Mouse.getRotX();
			float mouseDY = Mouse.getRotY();
			float speed = 10f * delta;
			
			rotation.z = mouseDX;
			rotation.y = mouseDY;
			
			if(Keyboard.keys[GLFW_KEY_W])
			{
				position.x -= forward().x * speed;
				position.y -= forward().y * speed;
				position.z -= forward().z * speed;
			}
			if(Keyboard.keys[GLFW_KEY_S])
			{
				position.x += forward().x * speed;
				position.y += forward().y * speed;
				position.z += forward().z * speed;
			}
			
			if(Keyboard.keys[GLFW_KEY_A])
			{
				position.x -= right().x * speed;
				position.y -= right().y * speed;
				position.z -= right().z * speed;
			}
		
			if(Keyboard.keys[GLFW_KEY_D])
			{
				position.x += right().x * speed;
				position.y += right().y * speed;
				position.z += right().z * speed;
			}
		}
	}
	
	private Vector3f forward()
	{
		Vector3f rot = new Vector3f();
		
		float cosY = (float) Math.cos(Math.toRadians(rotation.getZ() + 90));
		float sinY = (float) Math.sin(Math.toRadians(rotation.getZ() + 90));
		float cosP = (float) Math.cos(Math.toRadians(rotation.getY()));
		float sinP = (float) Math.sin(Math.toRadians(rotation.getY()));
		
		rot.setX(cosY * cosP);
		rot.setY(sinP);
		rot.setZ(sinY * cosP);
	
		return rot;
	}
	
	private Vector3f right()
	{
		Vector3f rot = new Vector3f();
		
		float cosY = (float) Math.cos(Math.toRadians(rotation.getZ()));
		float sinY = (float) Math.sin(Math.toRadians(rotation.getZ()));
		
		rot.setX(cosY);
		rot.setZ(sinY);
	
		return rot;
	}
	
	public void createProjectionMatrix(){
        float aspectRatio = (float) Main.DISPLAY_WIDTH / (float) Main.DISPLAY_HEIGHT;
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = zFar - zNear;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((zFar + zNear) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * zNear * zFar) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
	public Matrix4f createViewMatrix(){
		  Matrix4f viewMatrix = new Matrix4f();
		  viewMatrix.setIdentity();
		  Matrix4f.rotate((float) Math.toRadians(getRotation().getX()), new Vector3f(0,0,1), viewMatrix, viewMatrix);
		  Matrix4f.rotate((float) Math.toRadians(getRotation().getY()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		  Matrix4f.rotate((float) Math.toRadians(getRotation().getZ()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		  Vector3f cameraPos = getPosition();
		  Vector3f negativeCameraPos = new Vector3f(-cameraPos.getX(),-cameraPos.getY(),-cameraPos.getZ());
		  Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		  
		  return viewMatrix;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public float getFov() {
		return fov;
	}

	public float getzNear() {
		return zNear;
	}

	public float getzFar() {
		return zFar;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	
}
