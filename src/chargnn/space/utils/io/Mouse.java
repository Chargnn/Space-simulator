package chargnn.space.utils.io;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.util.vector.Vector2f;

import chargnn.space.main.Main;

public class Mouse extends GLFWCursorPosCallback {

	private static long windowID;
	
	private static Vector2f position;
	private Vector2f oldPosition;
	
	private static boolean locked = false;
	private static float rotX = 0;
	private static float rotY = 0;
	
	public Mouse(long window)
	{
		windowID = window;
		
		position = new Vector2f(Main.DISPLAY_WIDTH / 2, Main.DISPLAY_HEIGHT / 2);
		oldPosition = new Vector2f();
	}
	
	@Override
	public void invoke(long window, double x, double y) {		
		if(locked)
		{
			oldPosition = position;
			position.x = (float) x;
			position.y = (float) y;
			
			updateInput();
		}
	}
	
	private void updateInput()
	{		
		rotX = oldPosition.x - Main.DISPLAY_WIDTH / 2;
		rotY = oldPosition.y - Main.DISPLAY_HEIGHT / 2;
	}

	public static Vector2f getPosition() {
		return position;
	}

	public Vector2f getOldPosition() {
		return oldPosition;
	}

	public static boolean isLocked() {
		return locked;
	}
	
	public static void setLocked(boolean bool)
	{
		locked = bool;
	}

	public static float getRotX() {
		return rotX;
	}

	public static float getRotY() {
		return rotY;
	}
}
