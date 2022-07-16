package fruitfly.rasterizer3d;

import java.util.LinkedList;
import java.util.List;

import fruitfly.Color;
import fruitfly.Globals;
import fruitfly.MathUtil;
import fruitfly.Texture;
import fruitfly.rasterizer2d.Rasterizer2D;
import fruitfly.rasterizer3d.math.Matrix4f;
import fruitfly.rasterizer3d.math.Vector2f;
import fruitfly.rasterizer3d.math.Vector3f;
import fruitfly.rasterizer3d.math.Vector4f;

public class Rasterizer3D {
	private int[] frameBuffer;
	private int width, height;
	
	private Matrix4f modelViewMatrix; // from Object-space to Eye-space
	private Matrix4f projectionMatrix; // from Eye-space to Perspective
	
	private List<Matrix4f> modelViewMatrixStack = new LinkedList<Matrix4f>();
	
	// TODO: Perspective to Clipspace
	// TODO: Clip-space to NDC
	
	private Matrix4f pixelSpaceMatrix; // from NDC to Pixel-Space
	
	private RasterizerState state = new RasterizerState();
	private List<RasterizerState> stateStack = new LinkedList<RasterizerState>();
	
	private float[] depthBuffer;
	private Texture currentTexture;
	private boolean debug = false;
	
	public Rasterizer3D() {
		modelViewMatrix = Matrix4f.identity();
		projectionMatrix = Matrix4f.identity();
	}
	
	public enum Primitive {
		POINTS,
		LINES, // TODO
		TRIANGLES
	};
	
	public void setFrameBuffer(int[] buffer, int width, int height) {
		this.frameBuffer = buffer;
		this.width = width;
		this.height = height;
		
		depthBuffer = new float[width * height];
		
		// Do here because we need to know size of the framebuffer to define pixel-space transform
		// NDC -> Pixel-Space as in Blinn p142f
		final float ratio = height / (float)width;
		final float epsilon = 0.001f;
		
		final float dx = (width - epsilon) / 2.0f;
		final float dy = (height - epsilon) / 2.0f;
		
		final float sx = (width - epsilon) / 2.0f;
		final float sy = (height - epsilon) / (-2.0f * ratio);
		
		pixelSpaceMatrix = Matrix4f.identity();
		pixelSpaceMatrix.translate(dx, dy, 0.0f);
		pixelSpaceMatrix.scale(sx, sy, 1.0f);
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void clearDepth() {
		for (int i=0; i<depthBuffer.length; i++) {
			depthBuffer[i] = Float.POSITIVE_INFINITY;
		}
	}

	public Matrix4f getModelViewMatrix() {
		return modelViewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setPrimitiveMode(Primitive mode) {
		this.state.primitiveMode = mode;
	}
	
	public void setTexturing(boolean texturing) {
		if (!texturing) {
			this.state.textureCoordinates = null;
		}
	}
	
	public void saveState() {
		this.stateStack.add(this.state);
		this.state = new RasterizerState(this.state);
	}
	
	public void restoreState() {
		this.state = this.stateStack.remove(this.stateStack.size() - 1);
	}
	
	public void pushModelViewMatrix() {
		this.modelViewMatrixStack.add(this.modelViewMatrix);
		this.modelViewMatrix = new Matrix4f(this.modelViewMatrix);
	}
	
	public void popModelViewMatrix() {
		modelViewMatrix = modelViewMatrixStack.remove(modelViewMatrixStack.size() - 1);
	}
	
	public void clearColor(Color c) {
		for (int i=0; i<width * height; i++) {
			frameBuffer[i] = c.getAsARGBInt();
		}
	}
	
	private FaceData face = new FaceData();
	private LineData line = new LineData();
	private int currentVertexIndex;
	
	final static Vector3f eyeVectorEyeSpace = new Vector3f(0.0f, 0.0f, -1.0f);
	
	public void begin() {
		currentVertexIndex = 0;
	}
	
	public void end() {
		
	}
	
	
	
	private void processVertex(VertexData vertexData) {
		vertexData.color = state.color;
		
		vertexData.vEye = new Vector4f();
		Matrix4f.mul(modelViewMatrix, vertexData.vObject, vertexData.vEye);
		
		vertexData.vPerspective = new Vector4f();
		// Note: We switch from a right-handed coordinate-system to a left-handed
		Matrix4f.mul(projectionMatrix, vertexData.vEye, vertexData.vPerspective);

		float wPersp = vertexData.vPerspective.w;
		
		vertexData.varyings = state.textureCoordinates != null ? new float[] { state.textureCoordinates.x/wPersp, state.textureCoordinates.y/wPersp, 1/wPersp } : new float[] {};
		
		// Triangles
		if (state.primitiveMode == Primitive.TRIANGLES) {
			face.vertices[currentVertexIndex] = vertexData;
			
			if (currentVertexIndex == 2) {
				processFace(face);			
			}
			currentVertexIndex = (currentVertexIndex + 1) % 3;
		}
		else if (state.primitiveMode == Primitive.LINES) {
			line.vertices[currentVertexIndex] = vertexData;
			
			if (currentVertexIndex == 1) {
				processLine(line);
			}
			currentVertexIndex = (currentVertexIndex + 1) % 2;
		}
		// POINTS
		else if (state.primitiveMode == Primitive.POINTS) {
			VertexData vd = face.vertices[0];
			setPixel(state.color, (int)(vd.vPixel.x), (int)(vd.vPixel.y), vd.vPixel.z);
		}
		
	}
	
	private void processLine(LineData lineData) {
		
		if (clipLine(lineData)) return;
		
		for (VertexData vertexData : lineData.vertices) {
			calculatePostClipVertexData(vertexData);
		}
				
		drawLine(
			lineData.vertices[0].color, lineData.vertices[0].vPixel.x, lineData.vertices[0].vPixel.y,
			lineData.vertices[1].color, lineData.vertices[1].vPixel.x, lineData.vertices[1].vPixel.y
		);
	}

	private void processFace(FaceData faceData) {
		faceData.calculateFaceDetails();

		// Back-Face Culling
		if (!faceData.isFrontFacing()) return;
						
		// Clipping
		if (clipFace(faceData)) return;

		for (VertexData vertexData : faceData.vertices) {
			calculatePostClipVertexData(vertexData);
		}
		
		drawFace(face);
	}
	
	private void calculatePostClipVertexData(VertexData vertexData) {
		// Perspective-divide
		vertexData.vPerspective.homogenize();
		
		Vector4f vPixel = new Vector4f();
		Matrix4f.mul(pixelSpaceMatrix, vertexData.vPerspective, vPixel);
		vPixel.homogenize(); // needed?
		vertexData.vPixel = vPixel;
	}
	
	private static final Vector4f[] CLIPPING_PLANES = new Vector4f[] {
		new Vector4f(1.0f, 0.0f, 0.0f, 0.0f), // x=0
		new Vector4f(-1.0f, 0.0f, 0.0f, 1.0f), // x=1
	};
	
	private boolean clipFace(FaceData faceData) {
		int bc0, bc1;
		VertexData v0, v1;
		
		for (int i=0; i<3; i++) {
			bc0 = 0; bc1 = 0;
			v0 = faceData.vertices[i];
			v1 = faceData.vertices[(i + 1) % 3];
			
			for (int j=0; j<CLIPPING_PLANES.length; j++) {
				if (CLIPPING_PLANES[j].dot(v0.vPerspective) < 0) { // outside-test
					bc0 |= 1 << j;
				}
				if (CLIPPING_PLANES[j].dot(v1.vPerspective) < 0) { // outside-test
					bc1 |= 1 << j;
				}
			}
			
			if ((bc0 & bc1) == 0) { // Not both outside the same clip-plane
				if ((bc0 | bc1) == 0) { // Not point is outside any of the clip-planes
					return false;
				}
			}
		}
		
		return false;
	}
	
	private boolean clipLine(LineData lineData) {
		int bc0 = 0, bc1 = 0;
		VertexData v0 = lineData.vertices[0];
		VertexData v1 = lineData.vertices[1];
		
		bc0 = 0; bc1 = 0;
	
		for (int j=0; j<CLIPPING_PLANES.length; j++) {
			if (CLIPPING_PLANES[j].dot(v0.vPerspective) < 0) {
				bc0 |= 1 << j;
			}
			if (CLIPPING_PLANES[j].dot(v1.vPerspective) < 0) {
				bc1 |= 1 << j;
			}
		}
//		System.out.println("v0: " + v0 + "bc0: " + Integer.toBinaryString(bc0));
//		System.out.println("v1: " + v1 + "bc1: " + Integer.toBinaryString(bc1));
		
		if ((bc0 & bc1) == 0) { // Not both outside the same clip-plane
			if ((bc0 | bc1) == 0) { // Not point is outside any of the clip-planes
				return false;
			}
		}
		
		return true;
	}
	
	public void setVertexColor(Color c) {
		state.color = c;
	}
	
	public void setVertexTextureCoordinates(float u, float v) {
		state.textureCoordinates = new Vector2f(u, v);
	}
	
	public void addVertex(float x, float y, float z) {
		VertexData vertexData = new VertexData();
		vertexData.vObject = new Vector4f(x, y, z, 1.0f);
		
		processVertex(vertexData);
	}
	
	public void drawFace(FaceData face) {
		Edge e1, e2, e3;
		// Edges are always from smaller y to higher y value
		if (face.vertices[0].vPixel.y < face.vertices[1].vPixel.y) {
			e1 = new Edge(face.vertices[0], face.vertices[1]);
		}
		else {
			e1 = new Edge(face.vertices[1], face.vertices[0]);
		}
		
		if (face.vertices[1].vPixel.y < face.vertices[2].vPixel.y) {
			e2 = new Edge(face.vertices[1], face.vertices[2]);
		}
		else {
			e2 = new Edge(face.vertices[2], face.vertices[1]);
		}
		
		if (face.vertices[2].vPixel.y < face.vertices[0].vPixel.y) {
			e3 = new Edge(face.vertices[2], face.vertices[0]);
		}
		else {
			e3 = new Edge(face.vertices[0], face.vertices[2]);
		}
		
		Edge[] edges = new Edge[] { e1, e2, e3 };
		
		int maxLength = 0;
		int longEdge = 0;
		
		for (int i=0; i<3; i++) {
			int length = (int)edges[i].v2.vPixel.y - (int)edges[i].v1.vPixel.y;
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

	// edge1 has to be the long edge (y-component-wise)!!!
	private void drawSpansBetweenEdges(Edge edge1, Edge edge2) {
		int e1x1 = (int) edge1.v1.vPixel.x;
		int e1x2 = (int) edge1.v2.vPixel.x;
		int e2x1 = (int) edge2.v1.vPixel.x;
		int e2x2 = (int) edge2.v2.vPixel.x;
		
		int e1y1 = (int) edge1.v1.vPixel.y;
		int e1y2 = (int) edge1.v2.vPixel.y;
		int e2y1 = (int) edge2.v1.vPixel.y;
		int e2y2 = (int) edge2.v2.vPixel.y;
		
		int yDiff1 = e1y2 - e1y1;
		if (yDiff1 == 0) return;
		int yDiff2 = e2y2 - e2y1;
		if (yDiff2 == 0) return;
		
		int xDiff1 = e1x2 - e1x1;
		int xDiff2 = e2x2 - e2x1;

		float factor1 = (e2y1 - e1y1) / (float)yDiff1;
		float factorStep1 = 1.0f / yDiff1;
		float factor2 = 0.0f;
		float factorStep2 = 1.0f / yDiff2;
		
		for (int y=e2y1; y<e2y2; y++) {

			float[] varyings1 = new float[edge1.v1.varyings.length];
			float[] varyings2 = new float[edge1.v1.varyings.length];
			
			for (int i=0; i<edge1.v1.varyings.length; i++) {
				varyings1[i] = MathUtil.interpolate(edge1.v1.varyings[i], edge1.v2.varyings[i], factor1);
				varyings2[i] = MathUtil.interpolate(edge2.v1.varyings[i], edge2.v2.varyings[i], factor2);
			}

			
			Span span = new Span(
					y,
					Color.interpolate(edge1.v1.color, edge1.v2.color, factor1), e1x1 + (int)(xDiff1 * factor1), MathUtil.interpolate(edge1.v1.vPixel.z, edge1.v2.vPixel.z, factor1), varyings1,
					Color.interpolate(edge2.v1.color, edge2.v2.color, factor2), e2x1 + (int)(xDiff2 * factor2), MathUtil.interpolate(edge2.v1.vPixel.z, edge2.v2.vPixel.z, factor2), varyings2);
			drawSpan(span);

			factor1+=factorStep1;
			factor2+=factorStep2;
		}
	}

	private void drawSpan(Span span) {
		int xDiff = span.x2 - span.x1;
		if (xDiff == 0) return;
		
		float factor = 0.0f;
		float factorStep = 1.0f / (float)xDiff;

		float u, v, inverseWPersp;
		for (int x=span.x1; x<span.x2; x++) {
			
			Color c = null;
			if (span.varyings1.length >= 3) {
				u = MathUtil.interpolate(span.varyings1[0], span.varyings2[0], factor);
				v = MathUtil.interpolate(span.varyings1[1], span.varyings2[1], factor);
				inverseWPersp = MathUtil.interpolate(span.varyings1[2], span.varyings2[2], factor);
				c = new Color(currentTexture.get(u/inverseWPersp, v/inverseWPersp));
			}
			else {
				c = Color.interpolate(span.c1, span.c2, factor);
			}
			setPixel(c, x, span.y, MathUtil.interpolate(span.z1, span.z2, factor));
			
			factor+=factorStep;
		}
	}

	public void drawLine(Color c1, float x1, float y1, Color c2, float x2, float y2) {
		float xDiff = (x2 - x1);
		float yDiff = (y2 - y1);
		
		if (xDiff == 0.0f && yDiff == 0.0f) {
			setPixel(c1, Math.round(x1), Math.round(y1), 0.0f);
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
				setPixel(c, Math.round(x), Math.round(y), 0.0f);
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
				setPixel(c, Math.round(x), Math.round(y), 0.0f);
			}
		}
	}
	
	public void setPixel(Color c, int x, int y, float z) {
		if (x<0 || x >= width || y<0 || y>=height) {
			if (debug) System.out.println("Tried to set illegal pixel (" + x + "," + y + ")");
			return;
		}
		if (depthBuffer[y * width + x] <= z) return;
		depthBuffer[y * width + x] = z;
		
		if (x < 0 || x >= width || y < 0 || y >= height) return;
		frameBuffer[y * width + x] = c.getAsARGBInt();
	}
	
	public void setCurrentTexture(Texture tex) {
		this.currentTexture = tex;
	}
}
