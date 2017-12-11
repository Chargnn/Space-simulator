package chargnn.space.model;

public class VerticesModel {

	private int vaoID;
	private int vertexCount;
	
	public VerticesModel(int vao, int vertexCount)
	{
		this.vaoID = vao;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
}
