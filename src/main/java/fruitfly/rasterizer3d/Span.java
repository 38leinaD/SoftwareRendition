package fruitfly.rasterizer3d;

import fruitfly.Color;
import fruitfly.rasterizer2d.Span2D;

class Span {
	public Color c1, c2;
	public int x1, x2, y;
	public float z1, z2;
	public float[] varyings1, varyings2;
	
	public Span(int y, Color c1, int x1, float z1, float varyings1[], Color c2, int x2, float z2, float varyings2[]) {
		if (x1 < x2) {
			this.c1 = c1;
			this.c2 = c2;
			this.x1 = x1;
			this.x2 = x2;
			this.z1 = z1;
			this.z2 = z2;
			this.varyings1 = varyings1;
			this.varyings2 = varyings2;
		}
		else {
			this.c1 = c2;
			this.c2 = c1;
			this.x1 = x2;
			this.x2 = x1;
			this.z1 = z2;
			this.z2 = z1;
			this.varyings1 = varyings2;
			this.varyings2 = varyings1;
		}
		this.y = y;
	}
}
