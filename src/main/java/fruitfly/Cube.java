package fruitfly;

import fruitfly.rasterizer3d.Rasterizer3D;
import fruitfly.rasterizer3d.Rasterizer3DFacade;

public class Cube {
	public float x = 0.5f, y = 0.5f, z = 0.0f;
	public float xRot = 0.0f, yRot = 0.0f, zRot = 0.0f;
	
	public boolean controlled = false;
	
	public void render(Rasterizer3D r) {
		Rasterizer3DFacade rf = new Rasterizer3DFacade(r);
		
		r.pushModelViewMatrix();
		
		r.getModelViewMatrix().translate(x, y, z);
		r.getModelViewMatrix().rotateX(xRot);
		r.getModelViewMatrix().rotateY(yRot);
		r.getModelViewMatrix().rotateZ(zRot);

		r.begin();
		
		// front
		rf.renderColorQuad(
			0.1f, -0.1f, 0.1f,
			0.1f, 0.1f, 0.1f,
			-0.1f, 0.1f, 0.1f,
			-0.1f, -0.1f, 0.1f,
			Color.RED			
		);
				
		// back
		rf.renderColorQuad(
			-0.1f, -0.1f, -0.1f,
			-0.1f, 0.1f, -0.1f,
			0.1f, 0.1f, -0.1f,
			0.1f, -0.1f, -0.1f,
			Color.RED
		);
		
		// left
		rf.renderColorQuad(
			-0.1f, -0.1f, -0.1f,
			-0.1f, -0.1f, 0.1f,
			-0.1f, 0.1f, 0.1f,
			-0.1f, 0.1f, -0.1f,
			Color.GREEN
		);

		// right
		rf.renderColorQuad(
			0.1f, 0.1f, -0.1f,
			0.1f, 0.1f, 0.1f,
			0.1f, -0.1f, 0.1f,
			0.1f, -0.1f, -0.1f,
			Color.GREEN
		);
	
		// top
		rf.renderColorQuad(
			-0.1f, 0.1f, -0.1f,
			-0.1f, 0.1f, 0.1f,
			0.1f, 0.1f, 0.1f,
			0.1f, 0.1f, -0.1f,
			Color.BLUE
		);

		// bottom
		rf.renderColorQuad(
			0.1f, -0.1f, -0.1f,
			0.1f, -0.1f, 0.1f,
			-0.1f, -0.1f, 0.1f,
			-0.1f, -0.1f, -0.1f,
			Color.BLUE
		);
		
		r.end();
		
		r.popModelViewMatrix();
	}
	
	public void renderTextured(Rasterizer3D r, Texture t, float uStart, float vStart, float uStop, float vStop) {
		Rasterizer3DFacade rf = new Rasterizer3DFacade(r);
		
		r.pushModelViewMatrix();
		
		r.getModelViewMatrix().translate(x, y, z);
		r.getModelViewMatrix().rotateX(xRot);
		r.getModelViewMatrix().rotateY(yRot);
		r.getModelViewMatrix().rotateZ(zRot);
		
		r.begin();
		
		// front
		rf.renderTexturedQuad(
			0.1f, -0.1f, 0.1f, uStop, vStop,
			0.1f, 0.1f, 0.1f, uStop, vStart,
			-0.1f, 0.1f, 0.1f, uStart, vStart,
			-0.1f, -0.1f, 0.1f, uStart, vStop
		);
				
		// back
		rf.renderTexturedQuad(
			-0.1f, -0.1f, -0.1f, uStop, vStop,
			-0.1f, 0.1f, -0.1f, uStop, vStart,
			0.1f, 0.1f, -0.1f, uStart, vStart,
			0.1f, -0.1f, -0.1f, uStart, vStop
		);
		
		// left
		rf.renderTexturedQuad(
			-0.1f, -0.1f, -0.1f, uStop, vStop,
			-0.1f, -0.1f, 0.1f, uStop, vStart,
			-0.1f, 0.1f, 0.1f, uStart, vStart,
			-0.1f, 0.1f, -0.1f, uStart, vStop
		);

		// right
		rf.renderTexturedQuad(
			0.1f, 0.1f, -0.1f, uStop, vStop,
			0.1f, 0.1f, 0.1f, uStop, vStart,
			0.1f, -0.1f, 0.1f, uStart, vStart,
			0.1f, -0.1f, -0.1f, uStart, vStop
		);
	
		// top
		rf.renderTexturedQuad(
			-0.1f, 0.1f, -0.1f, uStop, vStop,
			-0.1f, 0.1f, 0.1f, uStop, vStart,
			0.1f, 0.1f, 0.1f, uStart, vStart,
			0.1f, 0.1f, -0.1f, uStart, vStop
		);

		// bottom
		rf.renderTexturedQuad(
			0.1f, -0.1f, -0.1f, uStop, vStop,
			0.1f, -0.1f, 0.1f, uStop, vStart,
			-0.1f, -0.1f, 0.1f, uStart, vStart,
			-0.1f, -0.1f, -0.1f, uStart, vStop
		);
		
		r.end();
		
		r.popModelViewMatrix();
	}

	public void tick(long tick) {
		if (controlled) {
			if (InputHandler.get().one.pressed)	xRot += 0.1f;
			if (InputHandler.get().two.pressed) yRot += 0.1f;
			if (InputHandler.get().three.pressed) yRot += 0.1f;
			
			if (InputHandler.get().left.pressed) x-=0.01;
			if (InputHandler.get().right.pressed) x+=0.01;
			if (InputHandler.get().up.pressed) y+=0.01;
			if (InputHandler.get().down.pressed) y-=0.01;
			if (InputHandler.get().front.pressed) z+=0.01;
			if (InputHandler.get().back.pressed) z-=0.01;
		}
		else {
			xRot += 0.1f;
			yRot += 0.1f;
			yRot += 0.1f;
		}

		if (xRot >= 2*Math.PI) xRot-=2*Math.PI;
		if (xRot < 0.0f) xRot+=2*Math.PI;
		
		if (yRot >= 2*Math.PI) yRot-=2*Math.PI;
		if (yRot < 0.0f) yRot+=2*Math.PI;
		
		if (zRot >= 2*Math.PI) zRot-=2*Math.PI;
		if (zRot < 0.0f) zRot+=2*Math.PI;
		
		

	}
}
