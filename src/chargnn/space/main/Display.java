package chargnn.space.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;

public class Display {

	private static long windowID;
	private static int width, height;
	private static String title = "Space";
	
	public static void createWindow(int w, int h, boolean fullscreen)
	{
		width = w;
		height = h;
		
		GLFWErrorCallback.createPrint(System.err).set();
		
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");
	    
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		glfwWindowHint(GLFW_ALPHA_BITS, 8);
		glfwWindowHint(GLFW_DEPTH_BITS, 24);
		glfwWindowHint(GLFW_STENCIL_BITS, 8);
		glfwWindowHint(GLFW_SAMPLES, 8);
		
		if(fullscreen)
			windowID = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), NULL);
		else
			windowID = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if(windowID == 0)
			throw new RuntimeException("Failed to create the GLFW window");
	}
	
	public static void show()
	{
		glfwShowWindow(getWindowID());
	}

	public static long getWindowID() {
		return windowID;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}
	
	
}
