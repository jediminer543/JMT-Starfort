package org.jmt.starfort.world.component;

/**
 * Defines a component that is damageable. These components will take damage first.
 * 
 * Any component that doesn't implement this is assumed to have no health;
 *  thus any component that doesn't implement this will be automaticly 
 *  destroyed when damage is applied to the block if there are no damagable
 *  components (making damagable components act like armour).
 * 
 * @author Jediminer543
 *
 */
public interface IComponentDamagable {

	/**
	 * Return the components current health
	 * 
	 * @return the components current health
	 */
	public int getComponentHealth();
	
	/**
	 * Return the components max health
	 * 
	 * @return
	 */
	public int getComponentMaxHealth();
	
	/**
	 * Modify the components health 
	 * <br><br>
	 * Literally health += value
	 * 
	 */
	public void modComponentHealth(int delta);
	
}
