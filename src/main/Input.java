package main;

import Utility.Vector2i;

public class Input
{
	public boolean[] mouseButtons, prevMouseButtons;
	public boolean[] keys, prevKeys;
	public Vector2i mouse;
	
	public Input()
	{
		mouseButtons = new boolean[100];
		prevMouseButtons = new boolean[100];
		keys = new boolean[120];
		prevKeys = new boolean[120];
		mouse = Vector2i.NULL_VECTOR;
	}
}