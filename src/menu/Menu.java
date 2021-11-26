package menu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import Utility.Vector2i;
import main.Camera;
import main.Main;

public class Menu
{
	Main game;
	MenuHandler menuHandler;
	
	List<Button> buttons = new ArrayList<Button>();
	
	public Menu(Main game)
	{
		this.game = game;
	}
	
	public void render(Camera cam, Graphics2D bg)
	{
		cam.translate(bg);
		for (Button button : buttons)
		{
			cam.drawImage(bg, button.getImage(), button.x, button.y, button.width, button.height);
			if (button.isHovered())
			{
				bg.setColor(new Color(255, 255, 255, 128));
				bg.setStroke(new BasicStroke((int) (cam.getPPU() * 3)));
				cam.drawRect(bg, button.x, button.y, button.width, button.height);
				bg.setStroke(new BasicStroke(1));
			}
		}
		cam.reset(bg);
	}
	
	public void add(Button button)
	{
		buttons.add(button);
	}
	
	public void clickAt(Vector2i pos)
	{
		for (Button button : buttons)
		{
			if (pos.x >= button.x && pos.x < button.x + button.width && pos.y >= button.y && pos.y < button.y + button.height)
			{
				button.onClick.run();
			}
		}
	}
	
	public void hoverAt(Vector2i pos)
	{
		for (Button button : buttons)
		{
			if (pos.x >= button.x && pos.x < button.x + button.width && pos.y >= button.y && pos.y < button.y + button.height)
				button.setHover(true);
			else
				button.setHover(false);
		}
	}
}
