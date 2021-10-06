package main;

import java.util.ArrayList;
import java.util.List;

import Utility.Vector2i;
import foods.Food;
import items.Item;
import tiles.Tile;

public class Space
{
	private final int WIDTH = 100, HEIGHT = 100; //Number of tiles in x and y directions
	
	private List<Player> players;
	private Tile[][] tiles;
	
	public Space()
	{
		tiles = new Tile[getWIDTH()][getHEIGHT()];
		players = new ArrayList<Player>();
	}
	
	public void update()
	{
		for (Player player : players)
		{
			Input input = player.getInput();
			Snake snake = player.getSnake();
			Inventory inventory = player.getInventory();
			Vector2i head = snake.getHead();
			
			snake.moveTowards(input.mouse);
			
			Tile tile = this.get(head.x, head.y);
			
			if (tile instanceof Food)
			{
				if (head.subtract(this.getCentreFrom(head.x, head.y)).getMagnitude() < ((Food) tile).getRadius())
				{
					snake.eat((Food) tile);
					this.clearTile(head.x, head.y);
				}
			}
			else if (tile instanceof Item)
			{
				if (((Item) tile).isPickable())
				{
					inventory.add((Item) tile);
					this.clearTile(head.x, head.y);
				}
			}
		}
	}
	
	public void put(double x, double y, Tile tile)
	{
		int ix = (int) x, iy = (int) y;
		if (ix >= 0 && iy >= 0 && ix < WIDTH && iy < HEIGHT)
			tiles[(int) x][(int) y] = tile;
	}
	
	public Tile get(double x, double y)
	{
		int ix = (int) x, iy = (int) y;
		if (ix >= 0 && iy >= 0 && ix < WIDTH && iy < HEIGHT)
			return tiles[(int) x][(int) y];
		return null;
	}
	
	//Returns centre of tile at (x, y)
	public Vector2i getCentreFrom(double x, double y)
	{
		double ix = (int) x, iy = (int) y;
		return new Vector2i(ix + 0.5, iy + 0.5);
	}
	
	public void clearTile(double x, double y)
	{
		int ix = (int) x, iy = (int) y;
		if (ix >= 0 && iy >= 0 && ix < WIDTH && iy < HEIGHT)
			tiles[(int) x][(int) y] = null;
	}
	
	public List<Player> getPlayers()
	{
		return players;
	}
	
	public void add(Object o)
	{
		if (o instanceof Player)
			players.add((Player) o);
	}
	
	public int getWIDTH()
	{
		return WIDTH;
	}
	
	public int getHEIGHT()
	{
		return HEIGHT;
	}
}
