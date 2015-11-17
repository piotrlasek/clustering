package org.dmtools.clustering.NBC.required;

import java.util.ArrayList;

public class LvaHashTree
{
	/**
	 * 
	 * @author Piotrek
	 *
	 */
	public class LvaNode
	{
		public int coordinate =- 1;
		public ArrayList<LvaNode> childNodes;
		public Cell c;
		
		LvaNode() {
		    childNodes = new ArrayList<LvaNode>();
		}
	}
	
	/**
	 * 
	 */
	ArrayList<LvaNode> rootNodes;
	
	/**
	 * 
	 */
	public LvaHashTree()
	{
		rootNodes = new ArrayList<LvaNode>();
	}
	
	/**
	 * 
	 * @param coordinates
	 * @return
	 */
	public Cell newCell(int[] coordinates)
	{
		Cell c = new Cell();
		
		ArrayList<LvaNode> nodes = rootNodes;
		LvaNode currentNode = null;
		boolean found = false;
		
		for(int i = 0; i < coordinates.length; i++)
		{
			found = false;
			for(int j = 0; j < nodes.size(); j++)
			{
				currentNode = nodes.get(j);
				if (currentNode.coordinate == coordinates[i])
				{
					nodes = currentNode.childNodes;
					found = true;
				}
			}
			if (!found)
			{
				LvaNode newNode = new LvaNode();
				newNode.coordinate = coordinates[i];
				nodes.add(newNode);
				nodes = newNode.childNodes;
				currentNode = newNode;
			}
		}
		
		currentNode.c = c;
		return c;
	}
	
	/**
	 * 
	 * @param coordinates
	 * @return
	 */
	public Cell getCell(int[] coordinates)
	{
		Cell c = null;
		
		ArrayList<LvaNode> nodes = rootNodes;
		LvaNode currentNode = null;
		boolean found = false;
		
		for(int i = 0; i < coordinates.length; i++)
		{
			found = false;
			for(int j = 0; j < nodes.size(); j++)
			{
				currentNode = nodes.get(j);
				if (currentNode.coordinate == coordinates[i])
				{
					nodes = currentNode.childNodes;
					found = true;
					break;
				}
			}
			if (!found)
				break;
		}
		if (found)
			return currentNode.c;
		else
			return null;
		
	}

	/**
	 * 
	 * @param coordinates
	 * @return
	 */
	public Cell exists(int[] coordinates)
	{
		Cell c = null;
		
		return c;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		LvaHashTree lht = new LvaHashTree();
		
		int[] coord = new int[]{1,2,3};
		Cell c1 = lht.newCell(coord);
		Cell c2 = lht.getCell(coord);
	}
}
