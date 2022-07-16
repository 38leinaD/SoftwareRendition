/**
 * 
 */
package fruitfly;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.List;

/**
 * @author daniel.platz
 *
 */
public class InputHandler implements KeyListener {
	private List<Key> keys = new LinkedList<Key>();
	
	class Key {
		public boolean pressed;
		
		public Key() {
			keys.add(this);
			pressed = false;
		}
	}
	
	public Key left = new Key();
	public Key right = new Key();
	public Key up = new Key();
	public Key down = new Key();
	
	public Key front = new Key();
	public Key back = new Key();
	
	public Key one = new Key();
	public Key two = new Key();
	public Key three = new Key();

	public void init() {
		Main.get().addKeyListener(this);
	}
	
	private void toggle(KeyEvent event, boolean pressed) {
		if (event.getKeyCode() == KeyEvent.VK_UP) {
			up.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
			down.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
			left.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
			right.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_F) {
			front.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_B) {
			back.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_1) {
			one.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_2) {
			two.pressed = pressed;
		}
		else if (event.getKeyCode() == KeyEvent.VK_3) {
			three.pressed = pressed;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent event) {
		toggle(event, true);
	}

	@Override
	public void keyReleased(KeyEvent event) {
		toggle(event, false);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private static InputHandler _instance;
	public static InputHandler get() {
		if (_instance == null) _instance = new InputHandler();
		return _instance;
	}
}
