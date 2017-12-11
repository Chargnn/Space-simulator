package chargnn.space.main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.system.MemoryStack.stackPush;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import chargnn.space.utils.io.Keyboard;
import chargnn.space.utils.io.Mouse;
import chargnn.space.utils.io.MouseIO;

public class Main {

	private long lastTime = 0;
	private long lastFPS;
	private int fps;
	
	public static final int DISPLAY_WIDTH = 720, DISPLAY_HEIGHT = 600;
	
	private Game game;
	@SuppressWarnings("unused")
	private GLFWKeyCallback keyCallback;
	@SuppressWarnings("unused")
	private GLFWCursorPosCallback cursorCallback;
	private GLFWMouseButtonCallback mouseButtonCallback;
	
	public static void main(String[] args) {
		Main main = new Main();
		
		main.game = new Game();

		main.startGame();
	}
	
	private void startGame()
	{
		init();
	}
	
	private void init()
	{
		Display.createWindow(800, 600, false);
		
		glfwSetKeyCallback(Display.getWindowID(), keyCallback = new Keyboard(Display.getWindowID()));	
		glfwSetCursorPosCallback(Display.getWindowID(), cursorCallback = new Mouse(Display.getWindowID()));
		glfwSetMouseButtonCallback(Display.getWindowID(), mouseButtonCallback = new MouseIO(Display.getWindowID()));
		
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(Display.getWindowID(), pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(
				Display.getWindowID(),
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		
		glfwMakeContextCurrent(Display.getWindowID());
		glfwSwapInterval(1);
		Display.show();
		run();
	}
	
	private void run()
	{
		GL.createCapabilities(); //Create GL context
		game.init();	
		
		getDelta();
        lastFPS = getTime(); 
        
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		while(!glfwWindowShouldClose(Display.getWindowID()))
		{
			int delta = getDelta();
		
			glfwPollEvents();
			update(delta);		
			render();
			
		}
		
		close();
	}
	
	private void update(int delta)
	{
		game.update(delta);
		updateFPS();
	}
	
	private void render()
	{
		glfwSwapBuffers(Display.getWindowID());
		glEnable(GL_DEPTH_TEST);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		game.render();
	}
	
	private void close()
	{
		glfwTerminate();
	}
	
	public long getTime() {
	    return System.nanoTime() / 1000000;
	}

	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastTime);
	    lastTime = time;
	         
	    return delta;
	}
	
	public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            System.out.println("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
}
