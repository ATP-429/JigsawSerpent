package menu;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import Utility.Vector2i;
import main.Camera;

public class Menu
{
	MenuHandler menuHandler;
	
	List<Button> buttons = new ArrayList<Button>();
	
	public void render(Camera cam, Graphics2D bg)
	{
		cam.translate(bg);
		for (Button button : buttons)
			cam.drawImage(bg, button.getImage(), button.x, button.y, button.width, button.height);
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
}
