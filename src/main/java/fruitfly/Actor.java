package fruitfly;

public class Actor {
	public float x = 0.0f, y = 0.0f, z = 0.0f;
	public float yaw = 0.0f;
	
	public void tick(long tick) {
		if (InputHandler.get().left.pressed) {
			yaw += 0.05f;
		}
		if (InputHandler.get().right.pressed) {
			yaw -= 0.05f;
		}
		if (InputHandler.get().up.pressed) {
			x -= 0.1f * (float)(Math.sin(yaw));
			z -= 0.1f * (float) (Math.cos(yaw));
		}
		if (InputHandler.get().down.pressed) {
			x += 0.1f * (float)(Math.sin(yaw));
			z += 0.1f * (float) (Math.cos(yaw));
		}
		
		if (yaw >= 2*Math.PI) yaw-=2*Math.PI;
		if (yaw < 0.0f) yaw+=2*Math.PI;
	}
	
	private static Actor _instance = null;
	public static Actor get() {
		if (_instance == null)  _instance = new Actor();
		return _instance;
	}	
}
