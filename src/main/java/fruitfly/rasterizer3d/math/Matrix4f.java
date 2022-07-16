package fruitfly.rasterizer3d.math;

public class Matrix4f {
	public float 		m11, m21, m31, m41,
						m12, m22, m32, m42,
						m13, m23, m33, m43,
						m14, m24, m34, m44;
	
	public Matrix4f(	float m11, float m21, float m31, float m41,
						float m12, float m22, float m32, float m42,
						float m13, float m23, float m33, float m43,
						float m14, float m24, float m34, float m44)
	{
		this.m11 = m11;	this.m21 = m21; this.m31 = m31; this.m41 = m41;
		this.m12 = m12;	this.m22 = m22; this.m32 = m32; this.m42 = m42;
		this.m13 = m13;	this.m23 = m23; this.m33 = m33; this.m43 = m43;
		this.m14 = m14;	this.m24 = m24; this.m34 = m34; this.m44 = m44;
	}
	
	public Matrix4f() {
		this.m11 = 0.0f;	this.m21 = 0.0f; this.m31 = 0.0f; this.m41 = 0.0f;
		this.m12 = 0.0f;	this.m22 = 0.0f; this.m32 = 0.0f; this.m42 = 0.0f;
		this.m13 = 0.0f;	this.m23 = 0.0f; this.m33 = 0.0f; this.m43 = 0.0f;
		this.m14 = 0.0f;	this.m24 = 0.0f; this.m34 = 0.0f; this.m44 = 0.0f;
	}
	
	public Matrix4f(Matrix4f m) {
		this.m11 = m.m11;	this.m21 = m.m21; this.m31 = m.m31; this.m41 = m.m41;
		this.m12 = m.m12;	this.m22 = m.m22; this.m32 = m.m32; this.m42 = m.m42;
		this.m13 = m.m13;	this.m23 = m.m23; this.m33 = m.m33; this.m43 = m.m43;
		this.m14 = m.m14;	this.m24 = m.m24; this.m34 = m.m34; this.m44 = m.m44;
	}
	
	public static Matrix4f identity() {
		return new Matrix4f(
			1.0f, 0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f, 1.0f
		);
	}
	
	public static void mul(Matrix4f a, Matrix4f b, Matrix4f dest) {
		dest.m11 = a.m11 * b.m11 + a.m21 * b.m12 + a.m31 * b.m13 + a.m41 * b.m14;
		dest.m21 = a.m11 * b.m21 + a.m21 * b.m22 + a.m31 * b.m23 + a.m41 * b.m24;
		dest.m31 = a.m11 * b.m31 + a.m21 * b.m32 + a.m31 * b.m33 + a.m41 * b.m34;
		dest.m41 = a.m11 * b.m41 + a.m21 * b.m42 + a.m31 * b.m43 + a.m41 * b.m44;
		
		dest.m12 = a.m12 * b.m11 + a.m22 * b.m12 + a.m32 * b.m13 + a.m42 * b.m14;
		dest.m22 = a.m12 * b.m21 + a.m22 * b.m22 + a.m32 * b.m23 + a.m42 * b.m24;
		dest.m32 = a.m12 * b.m31 + a.m22 * b.m32 + a.m32 * b.m33 + a.m42 * b.m34;
		dest.m42 = a.m12 * b.m41 + a.m22 * b.m42 + a.m32 * b.m43 + a.m42 * b.m44;
		
		dest.m13 = a.m13 * b.m11 + a.m23 * b.m12 + a.m33 * b.m13 + a.m43 * b.m14;
		dest.m23 = a.m13 * b.m21 + a.m23 * b.m22 + a.m33 * b.m23 + a.m43 * b.m24;
		dest.m33 = a.m13 * b.m31 + a.m23 * b.m32 + a.m33 * b.m33 + a.m43 * b.m34;
		dest.m43 = a.m13 * b.m41 + a.m23 * b.m42 + a.m33 * b.m43 + a.m43 * b.m44;
		
		dest.m14 = a.m14 * b.m11 + a.m24 * b.m12 + a.m34 * b.m13 + a.m44 * b.m14;
		dest.m24 = a.m14 * b.m21 + a.m24 * b.m22 + a.m34 * b.m23 + a.m44 * b.m24;
		dest.m34 = a.m14 * b.m31 + a.m24 * b.m32 + a.m34 * b.m33 + a.m44 * b.m34;
		dest.m44 = a.m14 * b.m41 + a.m24 * b.m42 + a.m34 * b.m43 + a.m44 * b.m44;
	}
	
	public static void mul(Matrix4f a, Vector4f b, Vector4f dest) {
		dest.x = a.m11 * b.x + a.m21 * b.y + a.m31 * b.z + a.m41 * b.w;
		dest.y = a.m12 * b.x + a.m22 * b.y + a.m32 * b.z + a.m42 * b.w;
		dest.z = a.m13 * b.x + a.m23 * b.y + a.m33 * b.z + a.m43 * b.w;
		dest.w = a.m14 * b.x + a.m24 * b.y + a.m34 * b.z + a.m44 * b.w;
	}

	public void perspective(float fov, float near, float far) {
		float s = (float) Math.sin(fov/2);
		float c = (float) Math.cos(fov/2);
		float Q = s / (1 - (near/far));
		
		this.m11 = c;		this.m21 = 0;		this.m31 = 0;		this.m41 = 0;
		this.m12 = 0;		this.m22 = c;		this.m32 = 0;		this.m42 = 0;
		this.m13 = 0;		this.m23 = 0;		this.m33 = -Q;		this.m43 = -Q*near;
		this.m14 = 0;		this.m24 = 0;		this.m34 = -s;		this.m44 = 0;
	}

	public void simplePerspective() {
		
		this.setIdentity();
		this.m34 = 1.0f/-0.25f; // Projection-Plane at z=-0.25
		this.m44 = 0.0f;
	}
	
	public void translate(float x, float y, float z) {
		Matrix4f t = new Matrix4f(
			1.0f,	0.0f,	0.0f,	x,
			0.0f,	1.0f,	0.0f,	y,
			0.0f,	0.0f,	1.0f,	z,
			0.0f,	0.0f,	0.0f,	1.0f		
		);
		Matrix4f m = new Matrix4f(this);
		Matrix4f.mul(m, t, this);
	}
	
	public void scale(float sx, float sy, float sz) {
		Matrix4f s = new Matrix4f(
			sx,		0.0f,	0.0f,	0.0f,
			0.0f,	sy,		0.0f,	0.0f,
			0.0f,	0.0f,	sz,		0.0f,
			0.0f,	0.0f,	0.0f,	1.0f		
		);
		Matrix4f m = new Matrix4f(this);
		Matrix4f.mul(m, s, this);
	}
	
	public void rotateX(float rad) {
		final float s = (float) Math.sin(rad);
		final float c = (float) Math.cos(rad);
		
		Matrix4f t = new Matrix4f(
			1.0f,	0.0f,	0.0f,	0.0f,
			0.0f,	c,		-s,		0.0f,
			0.0f,	s,		c,		0.0f,
			0.0f,	0.0f,	0.0f,	1.0f		
		);
		Matrix4f m = new Matrix4f(this);
		Matrix4f.mul(m, t, this);
	}
	
	public void rotateY(float rad) {
		final float s = (float) Math.sin(rad);
		final float c = (float) Math.cos(rad);
		
		Matrix4f t = new Matrix4f(
			c,		0.0f,	s,		0.0f,
			0.0f,	1.0f,	0.0f,	0.0f,
			-s,		0.0f,	c,		0.0f,
			0.0f,	0.0f,	0.0f,	1.0f		
		);
		Matrix4f m = new Matrix4f(this);
		Matrix4f.mul(m, t, this);
	}
	
	public void rotateZ(float rad) {
		final float s = (float) Math.sin(rad);
		final float c = (float) Math.cos(rad);
		
		Matrix4f t = new Matrix4f(
			c,		-s,		0.0f,	0.0f,
			s,		c,		0.0f,	0.0f,
			0.0f,	0.0f,	1.0f,	0.0f,
			0.0f,	0.0f,	0.0f,	1.0f		
		);
		Matrix4f m = new Matrix4f(this);
		Matrix4f.mul(m, t, this);
	}
	
	public void setIdentity() {
		m11 = 1.0f;	m21 = 0.0f;	m31 = 0.0f;	m41 = 0.0f;
		m12 = 0.0f;	m22 = 1.0f;	m32 = 0.0f;	m42 = 0.0f;
		m13 = 0.0f;	m23 = 0.0f;	m33 = 1.0f;	m43 = 0.0f;
		m14 = 0.0f;	m24 = 0.0f;	m34 = 0.0f;	m44 = 1.0f;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(m11);
		result = prime * result + Float.floatToIntBits(m12);
		result = prime * result + Float.floatToIntBits(m13);
		result = prime * result + Float.floatToIntBits(m14);
		result = prime * result + Float.floatToIntBits(m21);
		result = prime * result + Float.floatToIntBits(m22);
		result = prime * result + Float.floatToIntBits(m23);
		result = prime * result + Float.floatToIntBits(m24);
		result = prime * result + Float.floatToIntBits(m31);
		result = prime * result + Float.floatToIntBits(m32);
		result = prime * result + Float.floatToIntBits(m33);
		result = prime * result + Float.floatToIntBits(m34);
		result = prime * result + Float.floatToIntBits(m41);
		result = prime * result + Float.floatToIntBits(m42);
		result = prime * result + Float.floatToIntBits(m43);
		result = prime * result + Float.floatToIntBits(m44);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrix4f other = (Matrix4f) obj;
		if (Float.floatToIntBits(m11) != Float.floatToIntBits(other.m11))
			return false;
		if (Float.floatToIntBits(m12) != Float.floatToIntBits(other.m12))
			return false;
		if (Float.floatToIntBits(m13) != Float.floatToIntBits(other.m13))
			return false;
		if (Float.floatToIntBits(m14) != Float.floatToIntBits(other.m14))
			return false;
		if (Float.floatToIntBits(m21) != Float.floatToIntBits(other.m21))
			return false;
		if (Float.floatToIntBits(m22) != Float.floatToIntBits(other.m22))
			return false;
		if (Float.floatToIntBits(m23) != Float.floatToIntBits(other.m23))
			return false;
		if (Float.floatToIntBits(m24) != Float.floatToIntBits(other.m24))
			return false;
		if (Float.floatToIntBits(m31) != Float.floatToIntBits(other.m31))
			return false;
		if (Float.floatToIntBits(m32) != Float.floatToIntBits(other.m32))
			return false;
		if (Float.floatToIntBits(m33) != Float.floatToIntBits(other.m33))
			return false;
		if (Float.floatToIntBits(m34) != Float.floatToIntBits(other.m34))
			return false;
		if (Float.floatToIntBits(m41) != Float.floatToIntBits(other.m41))
			return false;
		if (Float.floatToIntBits(m42) != Float.floatToIntBits(other.m42))
			return false;
		if (Float.floatToIntBits(m43) != Float.floatToIntBits(other.m43))
			return false;
		if (Float.floatToIntBits(m44) != Float.floatToIntBits(other.m44))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return	"/ " + m11 + " " + m21 + " " + m31 + " " + m41 + " \\\n" +
				"| " + m12 + " " + m22 + " " + m32 + " " + m42 + " |\n" +
				"| " + m13 + " " + m23 + " " + m33 + " " + m43 + " |\n" +
				"\\ " + m14 + " " + m24 + " " + m34 + " " + m44 + " /";
	}
}
