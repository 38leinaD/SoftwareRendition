/**
 * 
 */
package fruitfly;

/**
 * @author daniel
 *
 */
public class Globals {
	public static final int WIDTH = 512;
	public static final int HEIGHT = 480;
	public static final int SCALE = 2;
	
	public static final long TICKS_PER_SECOND = 60;
	public static final double TICK_DURATION_IN_NANOSECONDS = 1000000000 / TICKS_PER_SECOND;
	public static final int MAX_SKIPPED_FRAMES = 10;
	
	public static boolean DEBUG = true;
}
