package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Utility.Vector2i;
import enemies.Rat;
import foods.BigFly;
import foods.Fly;
import foods.Food;
import keys.BlueKey;
import texture.TextureManager;

public class GameHandler extends Handler
{
	public final int DEFAULT_PPU = 64;
	
	public boolean initialized = false;
	
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
		
		space.add(player);
		
		initialized = true;
	}
	
	@Override
	public void update()
	{
		if (initialized)
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
		
		bg.setColor(new Color(0, 0, 0, 100));
		bg.fillRect(Main.RENDER_WIDTH - 200, 0, 200, 300);
		
		bg.setColor(new Color(255, 255, 255, 255));
		
		this.foodListX = Main.RENDER_WIDTH - 200;
		this.foodListY = 10;
		
		bg.drawString("    Count   Points", this.foodListX, this.foodListY+20);
		
		this.foodListY += 32 + 10;
		
		list(TextureManager.get("food").getImage(), new Food().getSize(), player.insectCount, bg);
		list(TextureManager.get("rat").getImage(), new Rat(Vector2i.NULL_VECTOR).getFoodValue(), player.ratCount, bg);
		list(TextureManager.get("fly").getImage(), new Fly().getSize(), player.flyCount, bg);
		list(TextureManager.get("big_fly").getImage(), new BigFly().getSize(), player.bigFlyCount, bg);
		
		bg.drawString("Size : " + player.getSnake().getBody().size(), foodListX + 10+32, foodListY + 20 + 20);
	}
	
	private int foodListX;
	private int foodListY;
	
	private void list(BufferedImage img, int foodSize, int count, Graphics2D bg)
	{
		bg.drawImage(img, foodListX, foodListY, null);
		bg.drawString("" + count, foodListX + 32 + 10, foodListY + 20);
		bg.drawString("" + foodSize, foodListX + 32 + 72 + 10, foodListY + 20);
		foodListY += 32 + 10;
	}
}
