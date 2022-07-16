package fruitfly.rasterizer3d.math;

public class Vector4f {
	public float x, y, z, w;
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector4f v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.w = v.w;
	}
	
	public Vector4f() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public Vector4f(Vector3f v3) {
		this(v3.x, v3.y, v3.z, 1.0f);
	}
	
	public void homogenize() {
		this.x /= this.w;
		this.y /= this.w;
		this.z /= this.w;
		this.w = 1.0f;
	}

	public float dot(Vector4f that) {
		return this.x * that.x + this.y * that.y + this.z * that.z + this.w * that.w;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
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
		Vector4f other = (Vector4f) obj;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	
	public String toString() {
		return	"(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
}
