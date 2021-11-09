package main;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import Utility.Vector2i;

public class Handler implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
	protected Camera cam;
	
	protected MouseEvent prevMouseE;
	protected boolean[] keys = new boolean[120], prevKeys = new boolean[120];
	protected boolean[] mouseButtons = new boolean[100], prevMouseButtons = new boolean[120];
	
	public void update()
	{
		
	}
	
	public void render(Graphics2D bg)
	{
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		mouseButtons[e.getButton()] = true;
		prevMouseE = e;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseButtons[e.getButton()] = false;
		prevMouseE = e;
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e)
	{
		//Get difference between the prevMouse coordinates and current mouse coords, in space, and then move camera's position by that difference.
		//This makes it so that the mouse pointer remains at the same location in space, while the camera's position changes.
		/*Vector2i prev = cam.getAbsoluteLocation(getPixelRelativeTo(prevMouseE));
		Vector2i next = cam.getAbsoluteLocation(getPixelRelativeTo(e));
		cam.setPos(cam.getPos().subtract(next.subtract(prev)));
		
		prevMouseE = e;*/
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		prevMouseE = e;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		Vector2i origPos = cam.getAbsoluteLocation(getPixelRelativeTo(e));
		if (e.getWheelRotation() < 0)
			cam.zoomIn();
		else
			cam.zoomOut();
		cam.calibrate(Main.RENDER_WIDTH, Main.RENDER_HEIGHT, cam.getPPU());
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		keys[e.getKeyCode()] = true;
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{
		keys[e.getKeyCode()] = false;
	}
	
	//Returns Vector2i of coords of pixel relative to centre of screen, IN CARTESIAN COORDS
	//NOTE : This is different from getPixelRelativeTo method in stLine. Over there we scale y by -1, so up is positive, so we do RENDER_HEIGHT/2 - pixel.y
	private Vector2i getPixelRelativeTo(int x, int y)
	{
		Vector2i pixel = getPixelInOrigScreen(x, y);
		return new Vector2i(pixel.x - Main.RENDER_WIDTH / 2, pixel.y - Main.RENDER_HEIGHT / 2);
	}
	//Returns Vector2i of coords of pixel relative to centre of screen if you pass the MouseEvent that generated the click on that pixel
	protected Vector2i getPixelRelativeTo(MouseEvent e)
	{
		return getPixelRelativeTo(e.getX(), e.getY());
	}
	
	//Returns Vector2i of the coords that the pixel would be at, if screen resolution was RENDER_WIDTH x RENDER_HEIGHT
	//Basically finds the ratio of x and y coords [Eg if WIDTH = 1000 and x = 500, then we just return RENDER_WIDTH*0.5]
	protected Vector2i getPixelInOrigScreen(int x, int y)
	{
		return new Vector2i((double) x / Main.WIDTH * Main.RENDER_WIDTH, (double) y / Main.HEIGHT * Main.RENDER_HEIGHT);
	}
}
