package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import Utility.Vector2i;
import enemies.Rat;
import foods.Food;
import keys.BlueKey;

public class GameHandler extends Handler
{
	public final int DEFAULT_PPU = 32;
	
	Space space;
	Player player;
	
	public GameHandler()
	{
		
	}
	
	public void start()
	{
		space = new Space();
		cam = new Camera();
		player = new Player();
		
		cam.calibrate(Main.RENDER_WIDTH, Main.RENDER_WIDTH, DEFAULT_PPU);
		
		space.put(5, 4, new BlueKey());
		space.put(3, 9, new BlueKey());
		space.put(15, 0, new BlueKey());
		
		Rat rat = new Rat(new Vector2i(5, 5));
		
		space.add(rat);
		
		for (int i = 10; i <= 20; i++)
			for (int j = 10; j <= 20; j++)
				space.put(i, j, new Food());
			
		space.add(player);
		
		player.getInventory().add(new BlueKey());
	}
	
	@Override
	public void update()
	{
		Input input = new Input();
		if (prevMouseE != null)
			input.mouse = cam.getAbsoluteLocation(getPixelRelativeTo(prevMouseE));
		input.keys = this.keys;
		input.mouseButtons = this.mouseButtons;
		input.prevKeys = this.prevKeys;
		input.prevMouseButtons = this.prevMouseButtons;
		player.setInput(input); //Set player's input to inputs captured from canvas. This input will then be interpreted in Space update() method
		
		cam.setPos(player.getSnake().getHead());
		
		space.update();
		
		prevKeys = keys.clone();
		prevMouseButtons = mouseButtons.clone();
	}
	
	@Override
	public void render(Graphics2D bg)
	{
		bg.translate(Main.RENDER_WIDTH / 2, Main.RENDER_HEIGHT / 2); //Makes (0, 0) the centre of our screen
		
		cam.render(space, bg);
		
		bg.translate(-Main.RENDER_WIDTH / 2, -Main.RENDER_HEIGHT / 2);
		
		player.getInventory().render(bg);
		bg.setFont(new Font(bg.getFont().getFontName(), Font.PLAIN, 24));
		bg.setColor(Color.BLACK);
		bg.drawString("SIZE : " + player.getSnake().getBody().size(), 10, Main.RENDER_HEIGHT - 10);
	}
}
