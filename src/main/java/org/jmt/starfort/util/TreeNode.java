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
 * @param <T> The source of the tree
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
    
    public TreeNode<T>[] addChildren(T[] childs) {
    	ArrayList<TreeNode<T>> newChildren = new ArrayList<TreeNode<T>>();
    	for (T child : childs){
        TreeNode<T> childNode = new TreeNode<T>(child);
        childNode.parent = this;
        newChildren.add(childNode);
    	}
    	this.children.addAll(newChildren);
    	@SuppressWarnings("unchecked")
		TreeNode<T>[] tmp = (TreeNode<T>[]) new Object[newChildren.size()];
        return newChildren.toArray(tmp);
    }
    
    public TreeNode<T> findNode(T data) {
    	TreeNode<T> result = null;
    	if (this.data == data) {
    		return this;
    	}
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
    	if (this.data == data) {
    		result.add(this);
    	}
    	for (TreeNode<T> node : this) {
    		ArrayList<TreeNode<T>> tmp = node.findNodes(data);
    		if (tmp.size() > 0) {
    			result.addAll(tmp);
    			break;
    		}
    	}
		return result;
    }
    
//    public abstract class TreeProcess {
//    	/**
//    	 * Perform this process against this node and child nodes, untill it returns true
//    	 * @param node
//    	 * @param data
//    	 */
//    	public abstract boolean process(TreeNode<T> node, T data);
//    }
//    
//    public boolean performProcess(TreeProcess process) {
//    	if (process.process(this, data)) {
//    		return true;
//    	} else {
//    		for (TreeNode<T> cnode : this) {
//    			
//    			if (cnode.performProcess(process)) {
//    				
//    			}
//    		}
//    	}
//    	return false;
//    }

	@Override
	public Iterator<TreeNode<T>> iterator() {
		return children.iterator();
	}

	

}
