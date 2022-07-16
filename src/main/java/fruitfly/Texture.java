package fruitfly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Texture {
	private String path;
	private BufferedImage textureImage;
	private int[] texturePixels;
	
	public Texture(String path) {
		this.path = path;
	}
	
	public void init() {
		try {
			textureImage = ImageIO.read(this.getClass().getResourceAsStream(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		texturePixels = textureImage.getRGB(0, 0, textureImage.getWidth(), textureImage.getHeight(), null, 0, textureImage.getWidth());
	}
	
	public int getWidth() {
		return textureImage.getWidth();
	}
	
	public int getHeight() {
		return textureImage.getHeight();
	}
	
	public int get(float u, float v) {
		if (u < 0.0f || u > 1.0f || v < 0.0f || v > 1.0f) {
			System.out.printf("[WARNING] Illegal Texture Access @ (%s, %s)\n", u, v);
			return 0xffff00ff;
		}
		float dd = 0.001f;
		u = (u - 0.5f) * (1.0f - dd) + (0.5f - dd / 2.0f);
		v = (v - 0.5f) * (1.0f - dd) + (0.5f - dd / 2.0f);
		
		return texturePixels[((int)((int)(v * textureImage.getHeight()) * textureImage.getWidth())) + (int)(u * textureImage.getWidth())];
	}
}
