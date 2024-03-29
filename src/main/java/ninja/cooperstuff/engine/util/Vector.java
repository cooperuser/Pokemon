package ninja.cooperstuff.engine.util;

import ninja.cooperstuff.pokemon.util.Direction;

public class Vector {
	public static final Vector zero=new Vector(), one=new Vector(1, 1);

	public double x, y;

	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector() {
		this(0.0, 0.0);
	}

	public double magnitude() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
	}

	public Vector normalized() {
		double magnitude = this.magnitude();
		if (magnitude == 0) return new Vector();
		else return Vector.div(this, magnitude);
	}

	public Vector add(Vector other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Vector sub(Vector other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}

	public Vector mul(double other) {
		this.x *= other;
		this.y *= other;
		return this;
	}

	public Vector div(double other) {
		if (other == 0) {
			this.x = 0;
			this.y = 0;
			return this;
		}
		this.x /= other;
		this.y /= other;
		return this;
	}

	public double dot(Vector other) {
		return this.x * other.x + this.y * other.y;
	}

	public double angle() {
		return Math.atan2(this.y, this.x);
	}

	public IntVector getIntVector() {
		return new IntVector(this.x, this.y);
	}

	public IntVector getTile() {
		return new IntVector(this.x / 32, this.y / 32);
	}

	@Override
	public int hashCode() {
		return (String.format("%s,%s", this.x, this.y)).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return (o != null && o.getClass() == this.getClass() && o.hashCode() == this.hashCode());
	}

	public Vector clone() {
		return new Vector(this.x, this.y);
	}

	public String toString() {
		return String.format("Vector(%s, %s)", (int) this.x, (int) this.y);
	}

	public static Vector add(Vector left, Vector right) {
		return new Vector(left.x + right.x, left.y + right.y);
	}

	public static Vector sub(Vector left, Vector right) {
		return new Vector(left.x - right.x, left.y - right.y);
	}

	public static Vector mul(Vector left, double scalar) {
		return new Vector(left.x * scalar, left.y * scalar);
	}

	public static Vector div(Vector left, double scalar) {
		if (scalar == 0) return new Vector();
		return new Vector(left.x / scalar, left.y / scalar);
	}

	public static double dot(Vector left, Vector right) {
		return left.x * right.x + left.y * right.y;
	}

	public static double distanceSquared(Vector a, Vector b) {
		return Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2);
	}

	public static double distance(Vector a, Vector b) {
		return Math.sqrt(Vector.distanceSquared(a, b));
	}

	public static Vector fromAngle(double angle, double scale) {
		return new Vector(Math.cos(angle), Math.sin(angle)).mul(scale);
	}

	public static Vector fromAngle(double angle) {
		return Vector.fromAngle(angle, 1.0);
	}

	public static Vector fromDirection(Direction direction) {
		switch(direction) {
			case UP: return new Vector(0, -1);
			case DOWN: return new Vector(0, 1);
			case LEFT: return new Vector(-1, 0);
			default: return new Vector(1, 0);
		}
	}

	public static Vector bezier(Vector v0, Vector v1, Vector v2, double t) {
		return Vector.add(
				Vector.mul(v0, Math.pow(1 - t, 2)),
				Vector.add(
						Vector.mul(v1, 2 * t * (1 - t)),
						Vector.mul(v2, Math.pow(t, 2))
				)
		);
	}

	public static boolean equals(Vector left, Vector right) {
		return left.x == right.x && left.y == right.y;
	}
}
