package main;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import Utility.Vector2i;
import enemies.Entity;
import enemies.Rat;
import events.AttackEvent;
import events.SnakeEvent;
import foods.Food;
import items.Item;
import keys.BlueKey;
import tiles.BlueLock;
import tiles.Grass;
import tiles.Tile;
import tiles.Wall;

public class Space
{
	public static final Tile BACKGROUND_TILE = new Grass();
	
	private final int WIDTH = 100, HEIGHT = 100; //Number of tiles in x and y directions
	
	private List<Player> players;
	private List<Entity> entities;
	private Tile[][] tiles;
	
	public Space()
	{
		tiles = new Tile[getWIDTH()][getHEIGHT()];
		players = new ArrayList<Player>();
		entities = new ArrayList<Entity>();
		
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				tiles[i][j] = BACKGROUND_TILE;
			
		for (int i = 5; i <= 11; i++)
		{
			tiles[i][11] = new Wall();
			tiles[i][5] = new Wall();
		}
		for (int i = 5; i <= 11; i++)
		{
			tiles[5][i] = new Wall();
			tiles[11][i] = new Wall();
		}
		tiles[8][5] = new BlueLock();
		
		for (int i = 6; i <= 10; i++)
			for (int j = 6; j <= 10; j++)
				this.put(i, j, new Food());
			
		Rat rat = new Rat(new Vector2i(10.5, 10.5));
		Rat rat2 = new Rat(new Vector2i(9.5, 9.5));
		Rat rat3 = new Rat(new Vector2i(7.5, 7.5));
		Rat rat4 = new Rat(new Vector2i(6.5, 6.5));
		
		this.add(rat);
		this.add(rat2);
		this.add(rat3);
		this.add(rat4);
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
			
			if (snake.action == Action.ATTACKING) //If snake is attacking, set dir to snake's direction
				dir = snake.dir;
			
			Vector2i finalPos = head.add(dir.multiply(snake.speed));
			
			//Handle collisions
			if (!available(finalPos))
			{
				double xDir = dir.x, yDir = dir.y;
				//See if it works when we set movement along x-axis
				dir = new Vector2i(xDir, 0);
				
				finalPos = head.add(dir.multiply(snake.speed));
				
				if (!available(finalPos)) //If that didn't work, see if it works when we set movement along y-axis
					dir = new Vector2i(0, yDir);
				
				finalPos = head.add(dir.multiply(snake.speed));
				
				if (!available(finalPos)) //If that didn't work either, just set dir to 0,0
					dir = Vector2i.NULL_VECTOR;
			}
			
			if (dir.getMagnitude() != 0) //If dir is 0, don't move snake, otherwise the tail moves forward while the head remains where it is, compressing the snake
				snake.moveTowardsDir(dir);
			
			//Handle tile stuff
			
			//Check surrounding tiles and remove locks if user has key
			for (int cx = -1; cx <= 1; cx++)
			{
				for (int cy = -1; cy <= 1; cy++)
				{
					Tile t = this.get(head.x - cx, head.y - cy);
					if (t instanceof Wall)
					{
						if (t instanceof BlueLock)
						{
							for (Item item : inventory.getItems())
							{
								if (item instanceof BlueKey)
								{
									inventory.remove(item);
									this.clearTile(head.x - cx, head.y - cy);
									break;
								}
							}
						}
					}
				}
			}
			
			Tile tile = this.get(head.x, head.y);
			if (tile instanceof Food)
			{
				if (head.subtract(this.getCentreFrom(head.x, head.y)).getMagnitude() < ((Food) tile).getRadius())
				{
					snake.eat((Food) tile);
					player.insectCount++;
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
			
			//Check if snake is eating any entity, only if snake is ATTACKING though
			if (snake.action == Action.ATTACKING)
			{
				for (int i = 0; i < entities.size(); i++)
				{
					Entity entity = entities.get(i);
					if (entity.getPos().distanceFrom(head) < 0.9)
					{
						if (entity instanceof Rat)
						{
							player.ratCount++;
							snake.eat(entity.getFoodValue());
						}
						entities.remove(i);
						i--;
					}
				}
			}
		}
		
		for (Entity entity : entities)
		{
			entity.update();
		}
	}
	
	private boolean available(Vector2i pos)
	{
		if (pos.x < 0 || pos.y < 0 || pos.x > WIDTH || pos.y > HEIGHT)
			return false;
		if (get(pos.x, pos.y) != null)
			return !get(pos.x, pos.y).hasCollision();
		return false;
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
			tiles[(int) x][(int) y] = new Grass();
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
	
	public Tile getBackground()
	{
		return BACKGROUND_TILE;
	}
}
