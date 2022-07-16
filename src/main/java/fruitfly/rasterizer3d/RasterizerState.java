package fruitfly.rasterizer3d;

import fruitfly.Color;
import fruitfly.rasterizer3d.Rasterizer3D.Primitive;
import fruitfly.rasterizer3d.math.Vector2f;

public class RasterizerState {
	Color color = Color.RED;
	Vector2f textureCoordinates = null;
	Primitive primitiveMode = Primitive.POINTS;
	
	public RasterizerState() {
		
	}
	
	public RasterizerState(RasterizerState state) {
		this.color = state.color;
		this.textureCoordinates = state.textureCoordinates;
		this.primitiveMode = state.primitiveMode;
	}
}
