package chargnn.space.shader;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

import org.lwjgl.util.vector.Matrix4f;

import chargnn.space.Entity.Light;

public class StaticShader extends Shader{

	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		glBindAttribLocation(getProgramID(), 0, "position");
		glBindAttribLocation(getProgramID(), 1, "textureCoords");
		glBindAttribLocation(getProgramID(), 2, "normals");

	}

	@Override
	protected void bindUniforms() {	
		addUniforms("transformationMatrix");
		addUniforms("projectionMatrix");
		addUniforms("viewMatrix");
		
		addUniforms("lightPosition");
		addUniforms("lightColor");
	}
	
	public void updateTransformationMatrix(Matrix4f mat)
	{
		super.setUniformMat4(getUniformLocation("transformationMatrix"), mat);
	}
	
	public void updateProjectionMatrix(Matrix4f mat)
	{
		super.setUniformMat4(getUniformLocation("projectionMatrix"), mat);
	}
	
	public void updateViewMatrix(Matrix4f mat)
	{
		super.setUniformMat4(getUniformLocation("viewMatrix"), mat);
	}
	
	public void loadLight(Light light)
	{
		super.setUniform3f(glGetUniformLocation(getProgramID(), "lightPosition"), light.getPosition());
		super.setUniform3f(glGetUniformLocation(getProgramID(), "lightColor"), light.getColor());
	}
}
