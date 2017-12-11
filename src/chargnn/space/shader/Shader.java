package chargnn.space.shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Shader {

	private int programID;
	
	private HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(4*4);
	
	public Shader(String vertexFile, String fragmentFile)
	{
		programID = glCreateProgram();
		
		if(programID == GL_FALSE)
		{
			System.err.println("Shader programID error!");
			System.exit(1);
		}
		
		createShader(loadShader(vertexFile), GL_VERTEX_SHADER);
		createShader(loadShader(fragmentFile), GL_FRAGMENT_SHADER);
		
		bindAttributes();
		
		glLinkProgram(programID);
		glValidateProgram(programID);
		
		bindUniforms();
	}
	
	protected abstract void bindAttributes();
	
	protected abstract void bindUniforms();
	
	public void bind()
	{
		glUseProgram(programID);
	}
	
	public void unbind()
	{
		glUseProgram(0);
	}
	
	public void setUniform1f(int location, float value)
	{
		glUniform1f(location, value);
	}
	
	public void setUniform3f(int location, Vector3f value)
	{
		glUniform3f(location, value.getX(), value.getY(), value.getZ());
	}
	
	public void setUniformMat4(int location, Matrix4f mat)
	{
		mat.store(matrixBuffer);
		matrixBuffer.flip();
		
		glUniformMatrix4fv(location, false, matrixBuffer);
	}
	
	public void addUniforms(String varName)
	{
		int location = glGetUniformLocation(programID, varName);
		
		if(location == 0xFFFFFFFF)
		{
			System.err.println("Could not find uniform: " + varName);
			System.exit(1);
		}
		
		uniforms.put(varName, location);
	}
	
	public int getUniformLocation(String varName)
	{
		if(uniforms.containsKey(varName))
			return uniforms.get(varName);
		else
		{
			System.err.println("Could not find uniform: " + varName);
			System.exit(1);
		}
		
		return -1;
	}
	
	private void createShader(String source, int type)
	{
		int shaderID = glCreateShader(type);
		
		if(shaderID == GL_FALSE)
		{
			System.err.println("Shader compile error: " + shaderID);
			System.exit(1);
		}
		
		glShaderSource(shaderID, source);
		glCompileShader(shaderID);
		
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE)
		{
			System.err.println(glGetShaderInfoLog(shaderID, 2048));
			System.exit(1);
		}
		
		glAttachShader(programID, shaderID);
	}
	
	private String loadShader(String file)
	{
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line = "";

			while((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			
			reader.close();
			
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "NULL";
	}

	public int getProgramID() {
		return programID;
	}
	
}
