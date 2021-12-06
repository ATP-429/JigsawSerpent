package menu;

import java.awt.Graphics2D;

import main.Camera;
import main.Main;
import texture.TextureManager;

public class InstructionMenu extends Menu
{
	public InstructionMenu(Main game)
	{
		super(game);
	}
	
	@Override
	public void render(Camera cam, Graphics2D bg)
	{
		cam.translate(bg);
		cam.drawImage(bg, TextureManager.get("instructions_menu").getImage(), 0, 0, Main.RENDER_WIDTH, Main.RENDER_HEIGHT);
		cam.reset(bg);
		super.render(cam, bg);
	}
}
