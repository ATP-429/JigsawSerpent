package main;

import Utility.Vector2i;

public class Input
{
	boolean[] keys;
	Vector2i mouse;
	
	public Input()
	{
		keys = new boolean[120];
		mouse = Vector2i.NULL_VECTOR;
	}
}