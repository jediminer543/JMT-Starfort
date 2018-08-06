package org.jmt.starfort.world.item.component;

import java.util.List;

import org.jmt.starfort.world.component.IComponent;
import org.jmt.starfort.world.item.IItem;

/**
 * A container of items, allowing for many to be stored in one space
 * 
 * @author jediminer543
 *
 */
public interface IComponentContainer extends IComponent {
	
	public List<IItem> getItems();
	
	public void addItem(IItem item);
	
	public IItem getItem(int id);
}
