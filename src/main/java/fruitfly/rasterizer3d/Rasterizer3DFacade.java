package fruitfly.rasterizer3d;

import fruitfly.Color;
import fruitfly.rasterizer3d.Rasterizer3D.Primitive;

public class Rasterizer3DFacade {
	private Rasterizer3D r;
	
	public Rasterizer3DFacade(Rasterizer3D rasterizer) {
		this.r = rasterizer;
	}
	
	public void renderColorQuad(
			float x1, float y1, float z1,
			float x2, float y2, float z2,
			float x3, float y3, float z3,
			float x4, float y4, float z4,
			Color c)
	{
		r.saveState();
		
		r.setPrimitiveMode(Primitive.TRIANGLES);
		r.setTexturing(false);
		
		r.setVertexColor(c);
		r.addVertex(x1, y1, z1);
		r.addVertex(x2, y2, z2);
		r.addVertex(x3, y3, z3);
		
		r.addVertex(x1, y1, z1);
		r.addVertex(x3, y3, z3);
		r.addVertex(x4, y4, z4);
		
		r.restoreState();
	}
	
	public void renderTexturedQuad(
			float x1, float y1, float z1, float u1, float v1,
			float x2, float y2, float z2, float u2, float v2,
			float x3, float y3, float z3, float u3, float v3,
			float x4, float y4, float z4, float u4, float v4)
	{
		r.saveState();
		
		r.setPrimitiveMode(Primitive.TRIANGLES);
		r.setTexturing(true);
		
		r.setVertexTextureCoordinates(u1, v1);
		r.addVertex(x1, y1, z1);
		r.setVertexTextureCoordinates(u2, v2);
		r.addVertex(x2, y2, z2);
		r.setVertexTextureCoordinates(u3, v3);
		r.addVertex(x3, y3, z3);
		
		r.setVertexTextureCoordinates(u1, v1);
		r.addVertex(x1, y1, z1);
		r.setVertexTextureCoordinates(u3, v3);
		r.addVertex(x3, y3, z3);
		r.setVertexTextureCoordinates(u4, v4);
		r.addVertex(x4, y4, z4);
		
		r.restoreState();
	}
}
