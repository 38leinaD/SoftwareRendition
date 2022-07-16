package fruitfly.rasterizer3d;

import fruitfly.Color;
import fruitfly.rasterizer3d.math.Matrix4f;
import fruitfly.rasterizer3d.math.Vector4f;

public class VertexData {
	public Vector4f vObject;
	public Vector4f vEye;
	public Vector4f vPerspective;
	public Vector4f vPixel;

	public Color color;
	public float[] varyings;
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OBJS" + vObject);
		sb.append(" -> EYES" + vEye);
		sb.append(" -> PROJS" + vPerspective);
		sb.append(" -> PXLS" + vPixel);
		
		return sb.toString();
	}
}
