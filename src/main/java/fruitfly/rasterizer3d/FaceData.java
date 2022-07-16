package fruitfly.rasterizer3d;

import fruitfly.Color;
import fruitfly.rasterizer3d.math.Vector3f;

public class FaceData {
	public VertexData[] vertices;
	public Vector3f normalPerspectiveSpace;
	
	public FaceData() {
		vertices = new VertexData[3];
	}
	
	public void calculateFaceDetails() {
		// Get plane vectors
		// Note that "v2 - v1" is e1 as we want the normal to point in the correct direction for our right-handed coord system
		// TODO if we switch to left-handed for Clip-space, I will need to change this
		Vector3f e1 = new Vector3f(
			this.vertices[2].vPerspective.x - this.vertices[1].vPerspective.x,
			this.vertices[2].vPerspective.y - this.vertices[1].vPerspective.y,
			this.vertices[2].vPerspective.z - this.vertices[1].vPerspective.z
		);
		
		Vector3f e2 = new Vector3f(
			this.vertices[0].vPerspective.x - this.vertices[1].vPerspective.x,
			this.vertices[0].vPerspective.y - this.vertices[1].vPerspective.y,
			this.vertices[0].vPerspective.z - this.vertices[1].vPerspective.z
		);
		
		// Calculate Normal to plane
		Vector3f e3 = e1.cross(e2);
		e3.normalize();
		this.normalPerspectiveSpace = e3;
	}
	
	public boolean isFrontFacing() {
		return (this.normalPerspectiveSpace.dot(Rasterizer3D.eyeVectorEyeSpace) <= 0);
	}
	
	public void tint(Color c) {
		for (int i=0; i<3; i++) {
			this.vertices[i].varyings = new float[] {};
			this.vertices[i].color = c;
		}
	}
}
