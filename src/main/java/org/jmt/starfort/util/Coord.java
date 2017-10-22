package org.jmt.starfort.util;

/**
 * A three dimensional integer coordinate, meant to represent a blocks location in the world.
 * 
 * @author Jediminer543
 *
 */
public class Coord implements Cloneable {
	
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
	public Coord addRM(Coord c) {
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
	public Coord addR(Coord c) {
		return new Coord(this.x + c.x, this.y + c.y, this.z + c.z);
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
	public Coord subRM(Coord c) {
		this.x -= c.x;
		this.y -= c.y;
		this.z -= c.z;
		return this;
	}
	
	/**
	 * Adds the passed coord to this coord. Doesn't modify this
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return<br>
	 * 
	 * @param c Coordinate to subtract
	 * @return this
	 */
	public Coord subR(Coord c) {
		return new Coord(this.x - c.x, this.y - c.y, this.z - c.z);
	}
	
	/**
	 * Multiplies the coord by the value passed
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return<br>
	 * M = Modify
	 * 
	 * @param i Value to multiply by
	 * @return this
	 */
	public Coord multRM(int i) {
		this.x *= i;
		this.y *= i;
		this.z *= i;
		return this;
	}
	
	/**
	 * Multiplies the coord by the value passed. Doesn't modify this
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return
	 * 
	 * @param i Value to multiply by
	 * @return New coord
	 */
	public Coord multR(int i) {
		return new Coord(x * i, y * i, z * i);
	}
	
	/**
	 * Gets the abs value of this coord. Doesn't modify this
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return
	 * 
	 * @param i Value to multiply by
	 * @return New coord
	 */
	public Coord absR() {
		return new Coord(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	/**
	 * Gets the abs value of this coord.
	 * <br><br>
	 * Method Naming Convension <br>
	 * R = Return<br>
	 * M = Modify
	 * 
	 * @param i Value to multiply by
	 * @return this
	 */
	public Coord absRM() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
		return this;
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