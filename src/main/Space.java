package main;

import items.Item;
import tiles.Tile;
import tiles.Wall;

public class Space
{
	Tile[][] tiles = new Tile[1000][1000];
	
	public Space()
	{
		tiles[0][0] = new Wall();
	}
	
	public void put(int x, int y, Tile tile)
	{
		tiles[x][y] = tile;
	}
}
