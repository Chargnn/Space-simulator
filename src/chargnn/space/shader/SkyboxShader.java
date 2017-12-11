package chargnn.space.shader;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

import org.lwjgl.util.vector.Matrix4f;

public class SkyboxShader extends Shader{

	public SkyboxShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		glBindAttribLocation(getProgramID(), 0, "position");
	}

	@Override
	protected void bindUniforms() {
		addUniforms("projectionMatrix");
		addUniforms("viewMatrix");
	}

	public void updateProjectionMatrix(Matrix4f mat)
	{
		super.setUniformMat4(getUniformLocation("projectionMatrix"), mat);
	}
	
	public void updateViewMatrix(Matrix4f mat)
	{
		mat.m30 = 0;
		mat.m31 = 0;
		mat.m32 = 0;
		super.setUniformMat4(getUniformLocation("viewMatrix"), mat);
	}
}
