package fruitfly.rasterizer2d;

import fruitfly.Color;

public class Span2D {
	public Color c1, c2;
	public int x1, x2, y;
	
	public Span2D(int y, Color c1, int x1, Color c2, int x2) {
		if (x1 < x2) {
			this.c1 = c1;
			this.c2 = c2;
			this.x1 = x1;
			this.x2 = x2;
		}
		else {
			this.c1 = c2;
			this.c2 = c1;
			this.x1 = x2;
			this.x2 = x1;
		}
		this.y = y;
	}
}
