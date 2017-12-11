package chargnn.space.model;

public class TexturedModel {

	private VerticesModel model;
	private Texture texture;
	
	public TexturedModel(VerticesModel model, Texture texture) {
		this.model = model;
		this.texture = texture;
	}

	public VerticesModel getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}	
}
