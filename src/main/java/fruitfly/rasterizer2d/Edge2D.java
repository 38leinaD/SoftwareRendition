package fruitfly.rasterizer2d;

import fruitfly.Color;

public class Edge2D {
	public Color c1, c2;
	public int x1, y1, x2, y2;
	
	public Edge2D(Color c1, int x1, int y1, Color c2, int x2, int y2) {
		if (y1 < y2) {
			this.c1 = c1;
			this.x1 = x1;
			this.y1 = y1;
			
			this.c2 = c2;
			this.x2 = x2;
			this.y2 = y2;
		}
		else {
			this.c2 = c1;
			this.x2 = x1;
			this.y2 = y1;
			
			this.c1 = c2;
			this.x1 = x2;
			this.y1 = y2;
		}
	}
}
