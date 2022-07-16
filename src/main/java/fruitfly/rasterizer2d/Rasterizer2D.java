package fruitfly.rasterizer2d;

import fruitfly.Color;
import fruitfly.Globals;

public class Rasterizer2D {
	protected int[] frameBuffer;
	protected int width, height;
	
	public Rasterizer2D() {
		
	}
	
	public void setFrameBuffer(int[] buffer, int width, int height) {
		this.frameBuffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	public void setPixel(Color c, int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return;
		frameBuffer[y * width + x] = c.getAsARGBInt();
	}
	
	public void setPixel(Color c, float x, float y) {
		setPixel(c, (int)x, (int)y);
	}
	
	public void clearColor(Color c) {
		for (int y=0; y<Globals.HEIGHT; y++) {
			for (int x=0; x<Globals.WIDTH; x++) {
				this.setPixel(c, x, y);
			}
		}
	}
	
	public void drawLine(Color c1, float x1, float y1, Color c2, float x2, float y2) {
		float xDiff = (x2 - x1);
		float yDiff = (y2 - y1);
		
		if (xDiff == 0.0f && yDiff == 0.0f) {
			setPixel(c1, x1, y1);
		}
		else if (Math.abs(xDiff) > Math.abs(yDiff)) {
			float xMin, xMax;
			if (x1 < x2) {
				xMin = x1;
				xMax = x2;
			}
			else {
				xMin = x2;
				xMax = x1;
			}
			
			float slope = yDiff / xDiff;
			for (float x = xMin; x <= xMax; x++) {
				float y = y1 + (x - x1)  * slope;
				Color c = Color.interpolate(c1, c2, (x - x1) / xDiff);
				setPixel(c, x, y);
			}
		}
		else {
			float yMin, yMax;
			if (y1 < y2) {
				yMin = y1;
				yMax = y2;
			}
			else {
				yMin = y2;
				yMax = y1;
			}
			
			float slope = yDiff / xDiff;
			for (float y = yMin; y <= yMax; y++) {
				float x = x1 + (y - y1)  * 1/slope;
				Color c = Color.interpolate(c1, c2, (y - y1) / yDiff);
				setPixel(c, x, y);
			}
		}
	}
	
	public void drawTriangle(Color c1, float x1, float y1, Color c2, float x2, float y2, Color c3, float x3, float y3) {
		Edge2D[] edges = new Edge2D[] {
			new Edge2D(c1, (int)x1, (int)y1, c2, (int)x2, (int)y2),
			new Edge2D(c2, (int)x2, (int)y2, c3, (int)x3, (int)y3),
			new Edge2D(c3, (int)x3, (int)y3, c1, (int)x1, (int)y1)
		};
		
		int maxLength = 0;
		int longEdge = 0;
		
		for (int i=0; i<3; i++) {
			int length = edges[i].y2 - edges[i].y1;
			if (length > maxLength) {
				maxLength = length;
				longEdge = i;
			}
		}
		
		int shortEdge1 = (longEdge + 1) % 3;
		int shortEdge2 = (longEdge + 2) % 3;
		
		drawSpansBetweenEdges(edges[longEdge], edges[shortEdge1]);
		drawSpansBetweenEdges(edges[longEdge], edges[shortEdge2]);
	}

	private void drawSpansBetweenEdges(Edge2D edge1, Edge2D edge2) {
		int yDiff1 = edge1.y2 - edge1.y1;
		if (yDiff1 == 0) return;
		int yDiff2 = edge2.y2 - edge2.y1;
		if (yDiff2 == 0) return;
		
		int xDiff1 = edge1.x2 - edge1.x1;
		int xDiff2 = edge2.x2 - edge2.x1;

		
		float factor1 = (edge2.y1 - edge1.y1) / (float)yDiff1;
		float factorStep1 = 1.0f / yDiff1;
		float factor2 = 0.0f;
		float factorStep2 = 1.0f / yDiff2;
		
		for (int y=edge2.y1; y<edge2.y2; y++) {
			Span2D span = new Span2D(
					y,
					Color.interpolate(edge1.c1, edge1.c2, factor1), edge1.x1 + (int)(xDiff1 * factor1),
					Color.interpolate(edge2.c1, edge2.c2, factor2), edge2.x1 + (int)(xDiff2 * factor2));
			drawSpan(span);
			
			factor1+=factorStep1;
			factor2+=factorStep2;
		}
	}

	private void drawSpan(Span2D span) {
		int xDiff = span.x2 - span.x1;
		if (xDiff == 0) return;
		
		float factor = 0.0f;
		float factorStep = 1.0f / (float)xDiff;
		
		for (int x=span.x1; x<span.x2; x++) {
			setPixel(Color.interpolate(span.c1, span.c2, factor), x, span.y);
			factor+=factorStep;
		}
	}
}
