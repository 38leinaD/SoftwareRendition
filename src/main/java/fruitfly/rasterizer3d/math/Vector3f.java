package fruitfly.rasterizer3d.math;

public class Vector3f {
public float x, y, z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f() {
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Vector3f(Vector4f v4) {
		Vector4f v4dup = new Vector4f(v4);
		v4dup.homogenize();
		this.x = v4dup.x;
		this.y = v4dup.y;
		this.z = v4dup.z;
	}
	
	public float dot(Vector3f that) {
		return this.x * that.x + this.y * that.y + this.z * that.z;
	}

	public Vector3f cross(Vector3f that) {
		return new Vector3f(
			this.y * that.z - this.z * that.y,
			this.z * that.x - this.x * that.z,
			this.x * that.y - this.y * that.x
		);
	}
	
	public void normalize() {
		float l = this.length();
		this.x /= l;
		this.y /= l;
		this.z /= l;
	}
	
	public float length() {
		return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	@Override
	public String toString() {
		return "Vector3f [x=" + x + ", y=" + y + ", z=" + z + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
}
