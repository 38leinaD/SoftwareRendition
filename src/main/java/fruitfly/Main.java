/**
 * 
 */
package fruitfly;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import fruitfly.rasterizer2d.Rasterizer2D;
import fruitfly.rasterizer3d.Rasterizer3D;
import fruitfly.rasterizer3d.Rasterizer3D.Primitive;
import fruitfly.rasterizer3d.Rasterizer3DFacade;
import fruitfly.rasterizer3d.math.Matrix4f;

/**
 * @author daniel
 *
 */
public class Main extends Canvas implements Runnable {

	private boolean running = true;
	private long tick = 0;
	
	private BufferedImage screenBuffer = new BufferedImage(Globals.WIDTH, Globals.HEIGHT, BufferedImage.TYPE_INT_ARGB);
	private int[] screenPixels = ((DataBufferInt)screenBuffer.getRaster().getDataBuffer()).getData();
	
	private Rasterizer2D rasterizer2d = null;
	private Rasterizer3D rasterizer3d = null;
	private Rasterizer3DFacade rasterizer3dFacade = null;
	
	private Texture texture = null;
	
	private Cube c = new Cube();
	
	private Random rand = new Random();
	private List<Cube> cubes = new LinkedList<Cube>();
	
	public void init() {
		Dimension d = new Dimension(Globals.WIDTH * Globals.SCALE, Globals.HEIGHT * Globals.SCALE);
		this.setSize(d);
		this.createBufferStrategy(2);

		InputHandler.get().init();
		this.rasterizer2d = new Rasterizer2D();
		this.rasterizer2d.setFrameBuffer(screenPixels, Globals.WIDTH, Globals.HEIGHT);
		
		this.rasterizer3d = new Rasterizer3D();
		this.rasterizer3d.setFrameBuffer(screenPixels, Globals.WIDTH, Globals.HEIGHT);
		
		this.rasterizer3dFacade = new Rasterizer3DFacade(rasterizer3d);
		
		this.texture = new Texture("/textures.png");
		this.texture.init();
		
		this.rasterizer3d.setCurrentTexture(texture);
		this.rasterizer3d.setDebug(false);
		
		for (int i=0; i<0; i++) {
			Cube c = new Cube();
			c.x = rand.nextFloat() * (rand.nextBoolean() ? 1.0f : -1.0f) * 0.5f;
			c.y = rand.nextFloat() * (rand.nextBoolean() ? 1.0f : -1.0f) * 0.5f;
			c.xRot = (float) (rand.nextFloat() * 2 * Math.PI);
			c.yRot = (float) (rand.nextFloat() * 2 * Math.PI);
			c.zRot = (float) (rand.nextFloat() * 2 * Math.PI);
			
			cubes.add(c);
		}
		
		c.controlled = true;
	}
	
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		
		this.renderScene();
		
		Graphics g = this.getBufferStrategy().getDrawGraphics();
		g.drawImage(screenBuffer, 0, 0, Globals.WIDTH * Globals.SCALE, Globals.HEIGHT * Globals.SCALE, null);
		g.dispose();
		getBufferStrategy().show();
	}

	Color backgroundColor = Color.BLACK;
	
	private float xRot = 0.0f;
	private float yRot = 0.0f;
	private float zRot = 0.0f;

	private void renderScene() {
		Rasterizer3D r = rasterizer3d;
		Rasterizer3DFacade rf = rasterizer3dFacade;
		
		r.clearColor(backgroundColor);
		r.clearDepth();
		
		//this.rasterizer2d.drawLine(Color.RED, 100.0f, 10.0f, Color.GREEN, 300.0f, 200.0f);
		//this.rasterizer2d.drawLine(Color.GREEN, 300.0f, 200.0f, Color.BLUE, 50.0f, 140.0f);
		//this.rasterizer2d.drawLine(Color.BLUE, 50.0f, 140.0f, Color.RED, 100.0f, 10.0f);
		this.rasterizer2d.drawTriangle(Color.RED, 100.0f, 10.0f, Color.GREEN, 300.0f, 200.0f, Color.BLUE, 50.0f, 140.0f);
		
		Matrix4f projectionMatrix = r.getProjectionMatrix();
		projectionMatrix.setIdentity();
		projectionMatrix.perspective((float) (Math.PI/3.0f), 0.01f, 1000.0f);
		//projectionMatrix.simplePerspective();
		
		Matrix4f modelViewMatrix = r.getModelViewMatrix();
		modelViewMatrix.setIdentity();

		modelViewMatrix.rotateY(-Actor.get().yaw);
		modelViewMatrix.translate(-Actor.get().x, -Actor.get().y, -Actor.get().z);
		modelViewMatrix.translate(0.0f, 0.0f, -1.5f);


		//r.setPrimitiveMode(Primitive.TRIANGLE);
		r.setPrimitiveMode(Primitive.TRIANGLES);
		
		this.c.renderTextured(r, texture, 0.0f, 0.0f, 1.0f/16.0f, 1.0f/16.0f);
		//this.c.render(r);
		
		r.setPrimitiveMode(Primitive.LINES);
		r.begin();
		r.addVertex(-0.5f, 0.0f, 0.0f);
		r.addVertex(0.5f, 0.5f, 0.0f);
		r.end();
		
		r.begin();

		//r.addVertex(Color.RED, 0.2f, 0.2f, -0.1f);
		
//		r.setTexturing(true);
//		r.setVertexColor(Color.RED);
//
//		r.setVertexTextureCoordinates(1.0f, 0.0f);
//		r.addVertex(0.1f, -0.1f, 0.1f);
//		r.setVertexTextureCoordinates(1.0f, 1.0f);
//		r.addVertex(0.1f, 0.1f, 0.1f);
//		r.setVertexTextureCoordinates(0.0f, 1.0f);
//		r.addVertex(-0.1f, 0.1f, 0.1f);
//		r.setVertexTextureCoordinates(0.0f, 0.0f);
//		r.addVertex(-0.1f, -0.1f, 0.1f);
		
		
		
		
//		r.addVertex(Color.RED, -0.1f, 0.1f, 0.0f);
//		r.addVertex(Color.RED, 0.1f, 0.1f, 0.0f);
//		r.addVertex(Color.RED, 0.0f, -0.1f, 0.0f);
//		
//		r.addVertex(Color.BLUE, -0.1f, -0.1f, -0.1f);
//		r.addVertex(Color.BLUE, 0.0f, 0.1f, 0.1f);
//		r.addVertex(Color.BLUE, 0.1f, -0.1f, -0.1f);
		
		r.end();
		
		int i=0;
		for (Cube c : cubes) {
			if (i > cubes.size() / 2) {
				c.render(r);
			}
			else {
				c.renderTextured(r, texture, 0.0f, 0.0f, 1.0f/16.0f, 1.0f/16.0f);
			}
			i++;
		}
		
		/*
		r.setDebug(true);
		r.setPrimitiveMode(Primitive.POINTS);
		r.begin();
		r.addVertex(0.0f, 0.0f, 0.0f);
		r.end();
		r.setDebug(false);
		*/
	}

	private void tick() {
		tick++;
		
		//Actor.get().tick(tick);
		
		this.c.tick(tick);
		for (Cube c : cubes) {
			c.tick(tick);
		}
	}

	@Override
	public void run() {
		// http://www.koonsolo.com/news/dewitters-gameloop/
		long nextTickTime = System.nanoTime();
		long lastStatsSecond = System.nanoTime() / 1000000000;
		int ticksPerSecond = 0;
		int framesPerSecond = 0;
		while (running) {
			
			int ticks = 0;
			while (System.nanoTime() > nextTickTime && ticks < Globals.MAX_SKIPPED_FRAMES) {
				tick();			
				nextTickTime += Globals.TICK_DURATION_IN_NANOSECONDS;
				ticks++;
				ticksPerSecond++;
			}
			
			render();
			framesPerSecond++;
			
			long currentStatsSecond = System.nanoTime() / 1000000000;
			if (currentStatsSecond > lastStatsSecond) {
				lastStatsSecond = currentStatsSecond;
				System.out.println("[Stats] fps: " + framesPerSecond + " tps: " + ticksPerSecond);
				framesPerSecond = 0;
				ticksPerSecond = 0;
			}
		}
	}

	private static Main _main = null;
	public static Main get() {
		return _main;
	}
	
	public static void main(String[] args) {

		
		JFrame frame = new JFrame("Software Rendering Demo");
		Dimension d = new Dimension(Globals.WIDTH * Globals.SCALE, Globals.HEIGHT * Globals.SCALE);
		frame.getContentPane().setPreferredSize(d);
		frame.setResizable(false);
		frame.setSize((int)d.getWidth(), (int)d.getHeight());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		//frame.setLocation(2000, 200);
		
		Main m = new Main();
		frame.add(m);
		_main = m;
		m.init();

		
		new Thread(m).start();
	}

}
