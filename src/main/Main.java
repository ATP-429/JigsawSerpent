package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.JFrame;

import menu.MainMenu;
import menu.MenuHandler;
import texture.TextureManager;

public class Main extends Canvas //Basically, 'extends Canvas' just makes any Main object a painting canvas, that is, we can draw on it
{
	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final int RENDER_WIDTH = 1280, RENDER_HEIGHT = 720;
	
	public static Stack<Handler> handlerStack = new Stack<Handler>();
	
	public final int TICKS_PER_SECOND = 60;
	
	JFrame frame;
	
	MouseEvent prevMouseE;
	boolean[] keys = new boolean[120];
	
	//Entry-point of program
	public static void main(String[] args)
	{
		Main game = new Main(); //Making an object of Main class
		//Thread.sleep() in init() function can throw an exception, so we need to catch it here to compile without errors.
		//This isn't that important, so ignore this
		try
		{
			game.init(); //Goes to init() function inside 'game' object
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	public void init() throws InterruptedException //Thread.sleep() throws an InterruptedException, so we have to make the function do so too, or surround Thread.sleep() with try catch. Again, ignore this
	{
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); //Set preferred size of canvas to WIDTH x HEIGHT
		setFocusable(true);
		requestFocus();
		
		/*TextureManager.addTexture("blue_key", "res/textures/blue_key.png");
		TextureManager.addTexture("food", "res/textures/food.png");
		TextureManager.addTexture("rat", "res/textures/rat.png");
		TextureManager.addTexture("start_button", "res/textures/start_button.png");*/
		TextureManager.loadTextures();
		
		frame = new JFrame(); //Creates a window
		frame.setResizable(false); //Now window cannot be resized by moving its borders
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Adds 'this' object to the frame. 'this' refers to the object that is executing this function, that is, the 'game' object we declared above in main(). 
		frame.add(this); //Basically, it's adding the 'game' object (Which is just a canvas) to the window, so anything we draw on the canvas will now be visible on the frame
		
		frame.setVisible(true); //Makes our frame visible
		
		frame.pack(); //Sets size of the JFrame such that all components inside it are at their preferred size, or higher
		
		//setLocationRelativeTo just sets the window's position on the screen with respect to another component 
		frame.setLocationRelativeTo(null); //If the argument is 'null', it just puts the window at the centre of the screen
		
		MenuHandler menuHandler = new MenuHandler();
		menuHandler.push(new MainMenu(this));
		push(menuHandler);
		
		long msPerFrame = 1000 / TICKS_PER_SECOND;
		
		long prev = System.nanoTime();
		long prevTime = System.nanoTime();
		int fps = 0;
		while (true)
		{
			update();
			repaint();
			fps++;
			
			long currTime = System.nanoTime();
			if (currTime - prevTime >= 1000000000)
			{
				prevTime = currTime;
				frame.setTitle("FPS : " + fps);
				fps = 0;
			}
			Thread.sleep(msPerFrame);
		}
		
		/*double nsPerTick = 1000000000.0 / 60;
		double nsPerFrame = 1000000000.0 / 60;
		double delta = 0;
		
		long prev = System.nanoTime();
		long prevTime = System.nanoTime();
		long prevTimeFrame = System.nanoTime();
		int fps = 0;
		while (true)
		{
			while (delta >= 1)
			{
				update();
				delta--;
			}
			
			long currTimeFrame = System.nanoTime();
			if (currTimeFrame - prevTimeFrame >= nsPerFrame)
			{
				repaint(); //Draws new frame (Calls update())
				fps++;
				prevTimeFrame = currTimeFrame;
			}
			
			long currTime = System.nanoTime();
			if (currTime - prevTime >= 1000000000)
			{
				prevTime = currTime;
				frame.setTitle("FPS : " + fps);
				fps = 0;
			}
			
			long curr = System.nanoTime();
			delta += (curr - prev) / nsPerTick;
			prev = curr;
		}*/
	}
	
	public void update()
	{
		if(!handlerStack.isEmpty())
			handlerStack.peek().update();
	}
	
	@Override
	public void paint(Graphics g)
	{
		/* Instead of drawing on the main screen directly, we first draw on a new BufferedImage we create
		 * This is because if we clear the previous screen and directly draw on it, we will see flickering of the screen.
		 * For eg, let's say we were drawing a moving circle on the screen
		 * If we clear the screen to white and then draw the circle directly, the user will be able to temporarily see the cleared screen before circle is drawn on it
		 * This causes flickering of the objects on our screen.
		 * However, if we first draw the circle on another image, and then just copy that image over to the main screen, the user will never see a cleared screen.
		 * This is what we're doing below.
		 */
		
		BufferedImage bufferImg = new BufferedImage(RENDER_WIDTH, RENDER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D bg = (Graphics2D) bufferImg.getGraphics();
		
		//Set background to white
		bg.setBackground(Color.WHITE);
		bg.clearRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
		
		bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //Turn anti-aliasing on
		bg.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE); //Just makes the graphics more accurate
		
		if(!handlerStack.isEmpty())
			handlerStack.peek().render(bg);
		
		//Draws bufferImg on our original canvas
		g.drawImage(bufferImg, 0, 0, WIDTH, HEIGHT, null);
	}
	
	@Override
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void push(Handler handler)
	{
		if (!handlerStack.isEmpty())
		{
			Handler prevHandler = handlerStack.peek();
			removeMouseListener(prevHandler);
			removeMouseMotionListener(prevHandler);
			removeMouseWheelListener(prevHandler);
			removeKeyListener(prevHandler);
		}
		handlerStack.push(handler);
		addMouseListener(handler);
		addMouseMotionListener(handler);
		addMouseWheelListener(handler);
		addKeyListener(handler);
	}
	
	public void pop()
	{
		Handler handler = handlerStack.peek();
		removeMouseListener(handler);
		removeMouseMotionListener(handler);
		removeMouseWheelListener(handler);
		removeKeyListener(handler);
		handlerStack.pop();
		if (!handlerStack.isEmpty())
		{
			Handler prevHandler = handlerStack.peek();
			addMouseListener(prevHandler);
			addMouseMotionListener(prevHandler);
			addMouseWheelListener(prevHandler);
			addKeyListener(prevHandler);
		}
	}
}
