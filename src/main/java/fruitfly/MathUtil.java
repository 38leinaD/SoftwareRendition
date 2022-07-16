package fruitfly;

import fruitfly.rasterizer2d.Span2D;

public class MathUtil {
	// Linear interpolation
	public static float interpolate(float a, float b, float i) {
		return a + (b - a) * i;
	}
}
