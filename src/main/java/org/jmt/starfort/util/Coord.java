package org.jmt.starfort.util;

import java.io.Serializable;

/**
 * A three dimensional integer coordinate, meant to represent a blocks location in the world.
 * 
 * @author Jediminer543
 *
 */
public class Coord implements Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 762022302312507787L;
	/**
	 * The locations of the coordinate
	 */
	public int x, y, z;
	
	public Coord() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Coord(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * Adds the passed coord to this coord
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return<br>
	 * M = Modify
	 * 
	 * @param c Coordinate to add
	 * @return this
	 */
	public Coord addM(Coord c) {
		this.x += c.x;
		this.y += c.y;
		this.z += c.z;
		return this;
	}
	
	/**
	 * Adds the passed coord to this coord. Doesn't modify this
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return<br>
	 * 
	 * @param c Coordinate to add
	 * @return this
	 */
	public Coord add(Coord c) {
		return new Coord(this.x + c.x, this.y + c.y, this.z + c.z);
	}
	
	/**
	 * Adds the passed coord to this coord
	 * <br><br>
	 * Modifies this coordinate
	 * 
	 * @param c Coordinate to add
	 * @return this
	 */
	public Coord subM(Coord c) {
		this.x -= c.x;
		this.y -= c.y;
		this.z -= c.z;
		return this;
	}
	
	/**
	 * Adds the passed coord to this coord
	 * <br><br>
	 * Doesn't modify this
	 * 
	 * @param c Coordinate to subtract
	 * @return this
	 */
	public Coord sub(Coord c) {
		return new Coord(this.x - c.x, this.y - c.y, this.z - c.z);
	}
	
	/**
	 * Multiplies the coord by the value passed
	 * <br><br>
	 * Modifies this coordinate
	 * 
	 * @param i Value to multiply by
	 * @return this
	 */
	public Coord multM(int i) {
		this.x *= i;
		this.y *= i;
		this.z *= i;
		return this;
	}
	
	/**
	 * Multiplies the coord by the value passed. Doesn't modify this
	 * <br><br>
	 * Doesn't modify this
	 * 
	 * @param i Value to multiply by
	 * @return New coord
	 */
	public Coord mult(int i) {
		return new Coord(x * i, y * i, z * i);
	}
	
	/**
	 * Gets the abs value of this coord.
	 * <br><br>
	 * Modifies this coordinate
	 * 
	 * @return this
	 */
	public Coord absM() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
	}
	
	/**
	 * Gets the abs value of this coord. Doesn't modify this
	 * <br><br>
	 * Doesn't modify this
	 * 
	 * @return New coord
	 */
	public Coord abs() {
		return new Coord(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	/**
	 * Gets the abs value of this coord.
	 * <br><br>
	 * Modifies this coordinate
	 * 
	 * @param c vector to max comparit with this
	 * @return this
	 */
	public Coord maxM(Coord c) {
		x = Math.max(x, c.x);
		y = Math.max(y, c.y);
		z = Math.max(z, c.z);
		return this;
	}
	
	/**
	 * Gets the abs value of this coord. Doesn't modify this
	 * <br><br>
	 * Doesn't modify this
	 * 
	 * @param c vector to max comparit with this
	 * @return New coord
	 */
	public Coord max(Coord c) {
		return new Coord(Math.max(x, c.x), Math.max(y, c.y), Math.max(z, c.z));
	}
	
	/**
	 * Gets the abs value of this coord.
	 * <br><br>
	 * Modifies this coordinate
	 * 
	 * @param c vector to max comparit with this
	 * @return this
	 */
	public Coord minM(Coord c) {
		x = Math.min(x, c.x);
		y = Math.min(y, c.y);
		z = Math.min(z, c.z);
		return this;
	}
	
	/**
	 * Get's the manhattan distance value of this coord
	 * 
	 * @return Manhattan Distance as an integer.
	 */
	public int manhattan() {
		return this.x+this.y+this.z;
	}
	
	/**
	 * Get's the pythagorean distance of this coord
	 * 
	 * @return Pythagorean distance as float
	 */
	public double pythag() {
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	/**
	 * Gets the abs value of this coord. Doesn't modify this
	 * <br><br>
	 * Doesn't modify this
	 * 
	 * @param c vector to max comparit with this
	 * @return New coord
	 */
	public Coord min(Coord c) {
		return new Coord(Math.min(x, c.x), Math.min(y, c.y), Math.min(z, c.z));
	}
	
	public Coord get() {
		return new Coord(x, y, z);
	}
	
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coord that = (Coord) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
    	return x ^ y ^ z;
    }
    
    @Override
    public String toString() {
		return "(" + x + "," + y + "," + z + ")" ;
    }
    
    @Override
    public Coord clone() {
		return new Coord(this.getX(), this.getY(), this.getZ());
    }
	
}