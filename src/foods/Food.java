package foods;

import items.Item;

public class Food extends Item
{
	protected int size = 5;
	protected double radius = 0.3;
	
	public Food()
	{
		texStr = "food";
	}
	
	public int getSize()
	{
		return size;
	}
	
	public double getRadius()
	{
		return radius;
	}
	
	@Override
	public boolean isPickable()
	{
		return false;
	}
}
