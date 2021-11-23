package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import items.Item;

public class Inventory
{
	private static final int CAPACITY = 6;
	
	private static int ITEM_SIZE = 64;
	private List<Item> items = new ArrayList<Item>();
	
	public List<Item> getItems()
	{
		return items;
	}
	
	public void add(Item item)
	{
		items.add(item);
	}
	
	public void render(Graphics2D bg)
	{
		int x = 0, y = 0;
		for (Item item : items)
		{
			bg.drawImage(item.getTexture().getImage(), x, y, ITEM_SIZE, ITEM_SIZE, null);
			x+= ITEM_SIZE;
		}
		for(int i = 0; i < CAPACITY; i++)
		{
			bg.setColor(new Color(0xFFAAAAAA));
			bg.drawRect(ITEM_SIZE*i, 0, ITEM_SIZE, ITEM_SIZE);
		}
	}

	public void remove(Item item)
	{
		items.remove(item);
	}
}
