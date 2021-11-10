package main;

import java.awt.event.KeyEvent;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import Utility.Maths;
import Utility.Vector2i;
import enemies.Entity;
import events.AttackEvent;
import events.SnakeEvent;
import foods.Food;
import items.Item;
import tiles.Tile;

public class Space
{
	private final int WIDTH = 100, HEIGHT = 100; //Number of tiles in x and y directions
	
	private List<Player> players;
	private List<Entity> entities;
	private Tile[][] tiles;
	
	public Space()
	{
		tiles = new Tile[getWIDTH()][getHEIGHT()];
		players = new ArrayList<Player>();
		entities = new ArrayList<Entity>();
	}
	
	public void update()
	{
		for (Player player : players)
		{
			Input input = player.getInput();
			Snake snake = player.getSnake();
			Inventory inventory = player.getInventory();
			Vector2i head = snake.getHead();
			List<SnakeEvent> events = snake.getEvents();
			
			//Handle player's inputs
			if (input.keys[KeyEvent.VK_SHIFT])
				snake.speed = 2;
			else
				snake.speed = Snake.SNAKE_DEFAULT_SPEED;
			
			if (input.mouseButtons[MouseEvent.BUTTON1] && !input.prevMouseButtons[MouseEvent.BUTTON1]) //LMB
				snake.addEvent(new AttackEvent());
			
			for (int i = 0; i < events.size(); i++)
			{
				SnakeEvent event = events.get(i);
				
				event.apply(snake);
				
				event.tick();
				if (event.isOver())
				{
					event.end(snake);
					events.remove(i);
					i--;
				}
			}
			
			Vector2i dir = snake.getDir(input.mouse);
			
			if (!events.isEmpty()) //If snake is doing some action, set dir to snake's direction
				dir = snake.dir;
			
			//Handle collisions
			Vector2i finalPos = head.add(dir.multiply(snake.speed));
			if (Maths.areIntersecting(head, finalPos, new Vector2i(0, 0), new Vector2i(WIDTH, 0)) || Maths.areIntersecting(head, finalPos, new Vector2i(0, HEIGHT), new Vector2i(WIDTH, HEIGHT))) //If snake will collide with upper or lower boundary of space
				dir = new Vector2i(dir.x, 0);
			finalPos = head.add(dir.multiply(snake.speed));
			
			if (Maths.areIntersecting(head, finalPos, new Vector2i(WIDTH, 0), new Vector2i(WIDTH, HEIGHT)) || Maths.areIntersecting(head, finalPos, new Vector2i(0, 0), new Vector2i(0, HEIGHT))) //If snake will collide with right or left boundary of space
				dir = new Vector2i(0, dir.y);
			
			snake.moveTowardsDir(dir);
			
			//Handle tile stuff
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
		
		for (Entity entity : entities)
		{
			entity.update();
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
	
	public List<Entity> getEntities()
	{
		return entities;
	}
	
	public void add(Object o)
	{
		if (o instanceof Player)
			players.add((Player) o);
		else if (o instanceof Entity)
			entities.add((Entity) o);
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
