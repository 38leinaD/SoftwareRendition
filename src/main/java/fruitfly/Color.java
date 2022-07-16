/**
 * 
 */
package fruitfly;

/**
 * @author daniel.platz
 *
 */
public class Color {
	private float r, g, b, a;
	
	public Color(float r, float g, float b, float a) {
		this.r = r; this.g = g; this.b = b; this.a = a;
	}
	
	public Color (float r, float g, float b) {
		this(r, g, b, 1.0f);
	}
	
	public Color (int argb) {
		this.a = ((argb >> 24) & 0xff) / 255.0f;
		this.r = ((argb >> 16) & 0xff) / 255.0f;
		this.g = ((argb >> 8) & 0xff) / 255.0f;
		this.b = ((argb >> 0) & 0xff) / 255.0f;
	}
	
	public static Color add(Color c1, Color c2) {
		return new Color((float)Math.min(c1.r + c2.r, 1.0f), (float)Math.min(c1.g + c2.g, 1.0f), (float)Math.min(c1.b + c2.b, 1.0f), (float)Math.min(c1.a + c2.a, 1.0f));
	}
	
	public static Color sub(Color c1, Color c2) {
		return new Color((float)Math.max(c1.r - c2.r, 0.0f), (float)Math.max(c1.g - c2.g, 0.0f), (float)Math.max(c1.b - c2.b, 0.0f), (float)Math.max(c1.a - c2.a, 0.0f));
	}
	
	public static Color mul(Color c1, Color c2) {
		return new Color(c1.r * c2.r, c1.g * c2.g, c1.b * c2.b, c1.a * c2.a);
	}
	
	public static Color div(Color c1, Color c2) {
		return new Color(c1.r / c2.r, c1.g / c2.g, c1.b / c2.b, c1.a / c2.a);
	}
	
	public static Color interpolate(Color c1, Color c2, float i) {
		return new Color(c1.r + (c2.r - c1.r) * i, c1.g + (c2.g - c1.g) * i, c1.b + (c2.b - c1.b) * i, c1.a + (c2.a - c1.a) * i);
	}
	
	public int getAsARGBInt() {
		return ((int)(a * 255) << 24) | ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
	}
	
	@Override
	public String toString() {
		return "Color(" + r + ", " + g + ", " + b + ", " + a + ")";
	}

	public static final Color WHITE 		= new Color(1.0f, 1.0f, 1.0f);
	public static final Color BLACK 		= new Color(0.0f, 0.0f, 0.0f);
	public static final Color GREY 			= new Color(1.0f, 1.0f, 1.0f);

	public static final Color RED 			= new Color(1.0f, 0.0f, 0.0f);
	public static final Color GREEN 		= new Color(0.0f, 1.0f, 0.0f);
	public static final Color BLUE 			= new Color(0.0f, 0.0f, 1.0f);
}
