package chargnn.space.utils.io;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFW;

public class Keyboard extends GLFWKeyCallback{

	private long windowID;
	
	public static boolean[] keys = new boolean[65536];
	
	public Keyboard(long window)
	{
		windowID = window;
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		 keys[key] = action != GLFW_RELEASE;	
		 
		 updateInput();
	}
	
	public static boolean isKeyDown(int keycode) {
	    return keys[keycode];
	}
	
	private void updateInput()
	{
		if(keys[GLFW.GLFW_KEY_ESCAPE])
		{
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			Mouse.setLocked(false);
		}
	}
}
