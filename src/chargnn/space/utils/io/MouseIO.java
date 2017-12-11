package chargnn.space.utils.io;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseIO extends GLFWMouseButtonCallback{

	private long windowID;
	
	public MouseIO(long window)
	{
		windowID = window;
	}
	
	@Override
	public void invoke(long window, int arg1, int arg2, int arg3) {
		
		if(!Mouse.isLocked())
		{
			if(glfwGetMouseButton(windowID, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS)
			{
				glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
				glfwSetCursorPos(window, Mouse.getPosition().x, Mouse.getPosition().y);
				Mouse.setLocked(true);
			}
		}
	}
}
