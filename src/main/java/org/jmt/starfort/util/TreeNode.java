package org.jmt.starfort.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Based upon: http://stackoverflow.com/questions/3522454/java-tree-data-structure
 * 
 * A data tree structure used for 
 * 
 * @author Jediminer543, Stack overflow
 *
 * @param <T>
 */
public class TreeNode<T> implements Iterable<TreeNode<T>> {
	
    T data;
    TreeNode<T> parent;
    List<TreeNode<T>> children;

    public TreeNode(T data) {
        this.data = data;
        this.children = new LinkedList<TreeNode<T>>();
    }

    public TreeNode<T> addChild(T child) {
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    
    public TreeNode<T> findNode(T data) {
    	TreeNode<T> result = null;
    	for (TreeNode<T> node : this) {
    		TreeNode<T> tmp = node.findNode(data);
    		if (tmp != null) {
    			result = tmp;
    			break;
    		}
    	}
		return result;
    }
    
    public boolean contains(T data) {
    	return findNode(data) != null;
    }
    
    public ArrayList<TreeNode<T>> findNodes(T data) {
    	ArrayList<TreeNode<T>> result = new ArrayList<TreeNode<T>>();
    	for (TreeNode<T> node : this) {
    		TreeNode<T> tmp = node.findNode(data);
    		if (tmp != null) {
    			result = tmp;
    			break;
    		}
    	}
		return result;
    }

	@Override
	public Iterator<TreeNode<T>> iterator() {
		return children.iterator();
	}

	

}
