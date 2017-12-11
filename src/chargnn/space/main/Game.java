package chargnn.space.main;

import org.lwjgl.util.vector.Vector3f;

import chargnn.space.Entity.Entity;
import chargnn.space.Entity.Light;
import chargnn.space.Entity.planet.Planet;
import chargnn.space.model.Loader;
import chargnn.space.model.Texture;
import chargnn.space.model.TexturedModel;
import chargnn.space.renderer.Renderer;
import chargnn.space.renderer.Skybox;
import chargnn.space.shader.StaticShader;
import chargnn.space.utils.io.Mouse;
import chargnn.space.utils.maths.Mathf;

public class Game {

	private StaticShader staticShader;
	private Renderer renderer;
	private Loader loader;
	private Skybox skybox;
	
	//TEMP
	private Planet planet;
	private Light light;
	
	public void init()
	{
		staticShader = new StaticShader("res/shaders/static.vert", "res/shaders/static.frag");
		loader = new Loader();
		renderer = new Renderer(staticShader);
		skybox = new Skybox(loader, renderer.getCamera().getProjectionMatrix());
		
		planet = new Planet(new TexturedModel(loader.loadObjModel("res/models/AlienPlanet.obj", loader), new Texture("res/textures/Planet color.png", true)), new Vector3f(50, 30, -5000), new Vector3f(0, 0, 0), 1000);
		light = new Light(new Vector3f(24000f, -730f, 46300f), new Vector3f(1, 1, 1));
	}
	
	public void update(int delta)
	{
		if(Mouse.isLocked())
		{
			renderer.getCamera().move(delta);			
			planet.updateRotation(new Vector3f(0, 0, 0), delta);
		}
	}	
	
	public void render()
	{
		staticShader.bind();
			staticShader.updateViewMatrix(renderer.getCamera().createViewMatrix());
			staticShader.loadLight(light);
			renderer.renderEntity(planet, staticShader);
		staticShader.unbind();
		
		skybox.render(renderer.getCamera());
	}

}
