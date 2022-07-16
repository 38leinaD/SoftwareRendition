package fruitfly.rasterizer3d.math.test;

import fruitfly.rasterizer3d.math.Matrix4f;
import fruitfly.rasterizer3d.math.Vector4f;

public class Test {
	
	public static void main(String[] args) {
		Matrix4f m1, m2;
		Matrix4f mr = Matrix4f.identity(), mer = Matrix4f.identity();
		Vector4f v1;
		Vector4f vr = new Vector4f(), ver = new Vector4f();
		
		// Test 1
		mr = Matrix4f.identity();
		mer = Matrix4f.identity();
		Matrix4f.mul(Matrix4f.identity(), Matrix4f.identity(), mr);
		assert mr.equals(mer) : "Expected " + mer + " but got " + mr;

		// Test 2
		m1 = new Matrix4f(
			2, 5, 5, 9,
			3, 1, 7, 7,
			4, 3, 2, 2,
			1, 1, 9, 1
		);
		
		m2 = new Matrix4f(
			1, 3, 5, 1,
			3, 1, 5, 8,
			1, 3, 1, 4,
			5, 6, 9, 1
		);
		
		mer = new Matrix4f(
			67,  80, 121,  71,
			48,  73,  90,  46,
			25,  33,  55,  38,
			18,  37,  28,  46
		);
		
		Matrix4f.mul(m1, m2, mr);
		assert mr.equals(mer) : "Expected " + mer + " but got " + mr;
		
		// Test 3
		m1 = new Matrix4f(
			2, 5, 5, 9,
			3, 1, 7, 7,
			4, 3, 2, 2,
			1, 1, 9, 1
		);
		
		v1 = new Vector4f(
			1,
			2,
			3,
			4
		);
		
		ver = new Vector4f(
			63,
			54,
			24,
			34	
		);

		Matrix4f.mul(m1, v1, vr);
		assert vr.equals(ver) : "Expected " + ver + " but got " + vr;
		
		// Test translation
		m1.setIdentity();
		m1.translate(2.0f, 5.0f, 1.0f);
		v1 = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
		ver = new Vector4f(2.0f, 5.0f, 1.0f, 1.0f);
		Matrix4f.mul(m1, v1, vr);
		assert vr.equals(ver) : "Expected " + ver + " but got " + vr;
				
		System.out.println("Test successfull!");
	}
}
