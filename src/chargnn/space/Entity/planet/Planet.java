package chargnn.space.Entity.planet;

import org.lwjgl.util.vector.Vector3f;

import chargnn.space.Entity.Entity;
import chargnn.space.model.TexturedModel;
import chargnn.space.utils.maths.Mathf;

public class Planet extends Entity{

	private final float ROTATION_SPEED = 0.000001f;
	
	public Planet(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
		super(model, position, rotation, scale);
	}
	
	public void updateRotation(Vector3f centerPosition, int delta)
	{
		float speed = ROTATION_SPEED * delta;
		
		float angle = 90 * Mathf.PI / 180;
		float rotX = Mathf.cos(angle) * (getPosition().x - centerPosition.x) - Mathf.sin(angle) * (getPosition().z - centerPosition.z) + centerPosition.x;
		float rotZ = Mathf.sin(angle) * (getPosition().x - centerPosition.x) - Mathf.cos(angle) * (getPosition().z - centerPosition.z) + centerPosition.z;
		
		increaseRotation(new Vector3f(0, speed, 0));
		increasePosition(new Vector3f(rotX * speed, 0, rotZ * speed));
	}

}
