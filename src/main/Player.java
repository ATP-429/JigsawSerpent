package main;

public class Player
{
	private Snake snake;
	private Inventory inventory;
	private Input input;
	
	public Player()
	{
		snake = new Snake();
		inventory = new Inventory();
	}
	
	public Snake getSnake()
	{
		return snake;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public Input getInput()
	{
		return input;
	}
	
	public void setInput(Input input)
	{
		this.input = input; 
	}
}
