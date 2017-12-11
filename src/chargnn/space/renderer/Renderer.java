package chargnn.space.renderer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import chargnn.space.Entity.Entity;
import chargnn.space.shader.StaticShader;
import chargnn.space.utils.maths.Mathf;

public class Renderer {
	
	private Camera camera;
	
	public Renderer(StaticShader shader)
	{
		camera = new Camera(new Vector3f(), new Vector3f(), 70.0f, 0.01f, 50000.0f);
		camera.createProjectionMatrix();
		
		shader.bind();
			shader.updateProjectionMatrix(camera.getProjectionMatrix());
		shader.unbind();
	}
	
	public void renderEntity(Entity entity, StaticShader shader)
	{
		glBindVertexArray(entity.getModel().getModel().getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2); 
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());	
			Matrix4f transformationMatrix = Mathf.createTransformationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
			shader.updateTransformationMatrix(transformationMatrix);
			glDrawElements(GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	public Camera getCamera() {
		return camera;
	}
}
