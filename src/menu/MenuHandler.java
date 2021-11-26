package menu;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Stack;

import Utility.Vector2i;
import main.Camera;
import main.Handler;
import main.Main;

public class MenuHandler extends Handler
{
	private Stack<Menu> menuStack;
	
	public MenuHandler()
	{
		cam = new Camera();
		cam.calibrate(Main.WIDTH / 2, Main.HEIGHT / 2, 1);
		cam.setPos(new Vector2i(Main.RENDER_WIDTH / 2, Main.RENDER_HEIGHT / 2)); //We want (0, 0) to be at the top-left of the screen, so camera must be at this location
		menuStack = new Stack<Menu>();
	}
	
	public void push(Menu menu)
	{
		menu.menuHandler = this;
		menuStack.push(menu);
	}
	
	@Override
	public void render(Graphics2D bg)
	{
		bg.translate(Main.RENDER_WIDTH / 2, Main.RENDER_HEIGHT / 2); //Makes (0, 0) the centre of our screen
		
		menuStack.peek().render(cam, bg);
		
		bg.translate(-Main.RENDER_WIDTH / 2, -Main.RENDER_HEIGHT / 2);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		Menu menu = menuStack.peek();
		menu.clickAt(cam.getAbsoluteLocation(getPixelRelativeTo(e)));
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Menu menu = menuStack.peek();
		menu.hoverAt(cam.getAbsoluteLocation(getPixelRelativeTo(e)));
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		//Do nothing, since we don't want user to be able to zoom in menu
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			menuStack.pop();
		if (menuStack.isEmpty())
			System.exit(0);
	}
}
