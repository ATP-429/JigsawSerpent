package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Utility.Vector2i;

public class Camera
{
	private Vector2i pos = new Vector2i(); //Centre of the bounding square of the cam
	private double xUnitsOnScreen, yUnitsOnScreen; //Number of units between centre of cam and any one edge of the bounding square of this camera
	private double ppu; //pixelsPerUnit, that is, size of one unit square in terms of pixels on screen
	
	private double fontHeight = 0.4; //Height of font in units IN SPACE DIMENSIONS
	private double fontOffset = 0.1; //Offset of font from the axes IN SPACE DIMENSIONS
	
	public Camera()
	{
		
	}
	
	public Camera(double xUnitsOnScreen, double yUnitsOnScreen, double ppu)
	{
		this.setXUnitsOnScreen(xUnitsOnScreen);
		this.setYUnitsOnScreen(yUnitsOnScreen);
		this.ppu = ppu;
	}
	
	//Configures the camera so that it fills up the whole screen, if ppu is passed to it
	public void calibrate(int WIDTH, int HEIGHT, double ppu)
	{
		this.ppu = ppu;
		this.setXUnitsOnScreen((double) (WIDTH / 2) / ppu); //Calculates number of units btwn centre and edge of screen. [pixels / pixels per unit = units]
		this.setYUnitsOnScreen((double) (HEIGHT / 2) / ppu);
	}
	
	//Returns absolute coordinate of pixel in Space, if coordinate of a pixel is passed wrt centre of cam
	public Vector2i getAbsoluteLocation(Vector2i pixel)
	{
		return new Vector2i(pixel.x / ppu, pixel.y / ppu).add(pos);
	}
	
	public double getXUnitsOnScreen()
	{
		return xUnitsOnScreen;
	}
	
	public double getYUnitsOnScreen()
	{
		return yUnitsOnScreen;
	}
	
	public void setXUnitsOnScreen(double xUnitsOnScreen)
	{
		this.xUnitsOnScreen = xUnitsOnScreen;
	}
	
	public void setYUnitsOnScreen(double yUnitsOnScreen)
	{
		this.yUnitsOnScreen = yUnitsOnScreen;
	}
	
	public Vector2i getPos()
	{
		return this.pos;
	}
	
	public void setPos(Vector2i pos)
	{
		this.pos = pos;
	}
	
	public double getPPU()
	{
		return this.ppu;
	}
	
	public void setPPU(double ppu)
	{
		this.ppu = ppu;
	}
	
	public void zoomIn()
	{
		if (ppu < 100)
			ppu += 5;
	}
	
	public void zoomOut()
	{
		if (ppu > 8)
			ppu -= 5;
	}
	
	public void render(Space space, Graphics2D bg)
	{
		double[] bounds = this.getBounds();
		double xLeft = bounds[0], yUp = bounds[1], xRight = bounds[2], yDown = bounds[3];
		
		translate(bg);
		
		//Draws the visible tiles
		for (int x = (int) xLeft; x <= xRight; x++)
			for (int y = (int) yDown; y <= yUp; y++)
				if (x >= 0 && y >= 0)
					if (space.get(x, y) != null)
						space.get(x, y).render(this, x, y, bg);
					
		for (Player player : space.getPlayers())
		{
			Snake snake = player.getSnake();
			snake.render(this, bg);
		}
		
		//Draws border of Space [That is, rectangle beyond which we would go out of bounds]
		bg.setColor(Color.RED);
		bg.setStroke(new BasicStroke((int) (0.2 * this.getPPU())));
		this.drawRect(bg, 0, 0, space.getWIDTH(), space.getHEIGHT());
		bg.setStroke(new BasicStroke(1));
		
		reset(bg);
	}
	
	//Sets centre of our drawing screen to centre of camera
	public void translate(Graphics2D bg)
	{
		bg.translate(-pos.x * ppu, -pos.y * ppu);
	}
	
	//Resets drawing coords back to normal
	public void reset(Graphics2D bg)
	{
		bg.translate(pos.x * ppu, pos.y * ppu);
	}
	
	//Returns bounds of this camera, more specifically, returns the top-left and the bottom-right coordinates of the camera's view
	public double[] getBounds()
	{
		double xLeft = this.getPos().x - this.getXUnitsOnScreen();
		double xRight = this.getPos().x + this.getXUnitsOnScreen();
		double yUp = this.getPos().y + this.getYUnitsOnScreen();
		double yDown = this.getPos().y - this.getYUnitsOnScreen();
		return new double[] {xLeft, yUp, xRight, yDown};
	}
	
	//The following functions convert space coords to camera coords and draw the required components
	
	public void drawLine(Graphics2D bg, double x1, double y1, double x2, double y2)
	{
		bg.drawLine((int) (x1 * this.getPPU()), (int) (y1 * this.getPPU()), (int) (x2 * this.getPPU()), (int) (y2 * this.getPPU()));
	}
	
	public void drawRect(Graphics2D bg, double x, double y, double width, double height)
	{
		bg.drawRect((int) (x * this.getPPU()), (int) (y * this.getPPU()), (int) (int) (width * this.getPPU()), (int) (height * this.getPPU()));
	}
	
	public void fillRect(Graphics2D bg, double x, double y, double width, double height)
	{
		bg.fillRect((int) (x * this.getPPU()), (int) (y * this.getPPU()), (int) (int) (width * this.getPPU()), (int) (height * this.getPPU()));
	}
	
	public void drawOval(Graphics2D bg, double x, double y, double r1, double r2)
	{
		bg.drawOval((int) (x * this.getPPU()), (int) (y * this.getPPU()), (int) (int) (r1 * this.getPPU()), (int) (r2 * this.getPPU()));
	}
	
	public void fillOval(Graphics2D bg, double x, double y, double r1, double r2)
	{
		bg.fillOval((int) (x * this.getPPU()), (int) (y * this.getPPU()), (int) (int) (r1 * this.getPPU()), (int) (r2 * this.getPPU()));
	}
	
	public void drawString(Graphics2D bg, String str, double x, double y)
	{
		bg.drawString(str, (int) (x * this.getPPU()), (int) (y * this.getPPU()));
	}
	
	//NOTE : width and height MUST BE PASSED AS SPACE UNITS, NOT IMAGE WIDTH AND HEIGHT
	public void drawImage(Graphics2D bg, BufferedImage img, double x, double y, double width, double height)
	{
		bg.drawImage(img, (int) (x * this.getPPU()), (int) (y * this.getPPU()), (int) (width * this.getPPU()), (int) (height * this.getPPU()), null);
	}
}
