package chargnn.space.model;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Loader {

	public VerticesModel loadInVao(float[] vertices)
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
			storeInVbo(0, vertices, 3);
		glBindVertexArray(0);
		return new VerticesModel(vaoID, vertices.length / 3);
	}
	
	public VerticesModel loadInVao(float[] vertices, int[] indices)
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
			storeIndices(indices);
			storeInVbo(0, vertices, 3);
		glBindVertexArray(0);
		return new VerticesModel(vaoID, indices.length);
	}
	
	public VerticesModel loadInVao(float[] vertices, int[] indices, float[] textureCoords)
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
			storeIndices(indices);
			storeInVbo(0, vertices, 3);
			storeInVbo(1, textureCoords, 2);
		glBindVertexArray(0);
		return new VerticesModel(vaoID, indices.length);
	}
	
	public VerticesModel loadInVao(float[] vertices, int[] indices, float[] textureCoords, float[] normals)
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
			storeIndices(indices);
			storeInVbo(0, vertices, 3);
			storeInVbo(1, textureCoords, 2);
			storeInVbo(2, normals, 3);
		glBindVertexArray(0);
		return new VerticesModel(vaoID, indices.length);
	}
	public VerticesModel loadSkybox(float[]vertices)
	{
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
			storeInVbo(0, vertices, 3);
		glBindVertexArray(0);
		
		return new VerticesModel(vaoID, vertices.length / 3);
	}
	
	public VerticesModel loadObjModel(String fileName, Loader loader){
		FileReader fr = null;
		try {
			fr = new FileReader(new File(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load file!");
			e.printStackTrace();
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indices = new ArrayList<Integer>();
		float[] verticesArray = null;
		float[] normalsArray = null;
		float[] textureArray = null;
		int[] indicesArray = null;
		try {
		
			while (true){
				line = reader.readLine();
				String[] currentLine = line.split(" ");
				if (line.startsWith("v ")){
					Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					vertices.add(vertex);
				}else if (line.startsWith("vt ")){
					Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), 
						Float.parseFloat(currentLine[2]));
					textures.add(texture);
				}else if (line.startsWith("vn ")){
					Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
							Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
					normals.add(normal);
				}else if (line.startsWith("f ")){
					textureArray = new float[vertices.size()*2];
					normalsArray = new float[vertices.size()*3];
					break;
				}
			}
			
			while(line != null){
				if (!line .startsWith("f ")){
					line = reader.readLine();
					continue;
				}
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				
				processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
				processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
				line = reader.readLine();
			}
			reader.close();
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		verticesArray = new float[vertices.size()*3];
		indicesArray = new int[indices.size()];
		
		int vertexPointer = 0;
		for (Vector3f vertex:vertices){
			verticesArray[vertexPointer++] = vertex.x;
			verticesArray[vertexPointer++] = vertex.y;
			verticesArray[vertexPointer++] = vertex.z;
		}
		
		for (int i=0;i<indices.size();i++){
			indicesArray[i] = indices.get(i);
		}
	
		return loader.loadInVao(verticesArray, indicesArray, textureArray, normalsArray);
	}
	
	private static void processVertex(String[] vertexData, List<Integer> indices,
		List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
		float[] normalsArray) {
		int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
		indices.add(currentVertexPointer);
		
		if(textureArray.length != 0)
		{
			Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
			textureArray[currentVertexPointer*2] = currentTex.x;	
			textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
		}
		
		if(normalsArray.length != 0)
		{
			Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
			normalsArray[currentVertexPointer*3] = currentNorm.x;
			normalsArray[currentVertexPointer*3+1] = currentNorm.y;
			normalsArray[currentVertexPointer*3+2] = currentNorm.z;
		}
	}
	
	private void storeInVbo(int attribNumber, float[] data, int size)
	{
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glVertexAttribPointer(attribNumber, size, GL11.GL_FLOAT, false, 0, 0);
	}
	
	private void storeIndices(int[] indices)
	{
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
		buffer.put(indices);
		buffer.flip();
		
		int vboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	}
	
}
