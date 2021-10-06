package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import Utility.Vector2i;
import foods.Food;
import keys.BlueKey;
import texture.TextureManager;

public class Main extends Canvas implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener //Basically, 'extends Canvas' just makes any Main object a painting canvas, that is, we can draw on it
{
	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final int RENDER_WIDTH = 1280, RENDER_HEIGHT = 720;
	
	private final int DEFAULT_PPU = 32;
	
	JFrame frame;
	
	Camera cam;
	Space space;
	Player player;
	
	MouseEvent prevMouseE;
	
	boolean[] keys = new boolean[120];
	
	//Entry-point of program
	public static void main(String[] args)
	{
		Main game = new Main(); //Making an object of Main class
		game.addMouseListener(game);
		game.addMouseMotionListener(game);
		game.addMouseWheelListener(game);
		game.addKeyListener(game);
		game.setPreferredSize(new Dimension(WIDTH, HEIGHT)); //Set preferred size of canvas to WIDTH x HEIGHT
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
		TextureManager.addTexture("blue_key", "res/textures/blue_key.png");
		TextureManager.addTexture("food", "res/textures/food.png");
		
		space = new Space();
		cam = new Camera();
		player = new Player();
		
		cam.calibrate(RENDER_WIDTH, RENDER_WIDTH, DEFAULT_PPU);
		
		space.put(5, 4, new BlueKey());
		space.put(3, 9, new BlueKey());
		space.put(15, 0, new BlueKey());
		
		for (int i = 10; i <= 20; i++)
			for(int j = 10; j <= 20; j++)
				space.put(i, j, new Food());
		
		space.add(player);
		
		player.getInventory().add(new BlueKey());
		
		frame = new JFrame(); //Creates a window
		frame.setResizable(false); //Now window cannot be resized by moving its borders
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Adds 'this' object to the frame. 'this' refers to the object that is executing this function, that is, the 'game' object we declared above in main(). 
		frame.add(this); //Basically, it's adding the 'game' object (Which is just a canvas) to the window, so anything we draw on the canvas will now be visible on the frame
		
		frame.setVisible(true); //Makes our frame visible
		
		frame.pack(); //Sets size of the JFrame such that all components inside it are at their preferred size, or higher
		
		//setLocationRelativeTo just sets the window's position on the screen with respect to another component 
		frame.setLocationRelativeTo(null); //If the argument is 'null', it just puts the window at the centre of the screen
		
		//We want around 60 frames per second. That is, we want 60 frames to be displayed every 1s = 1000ms
		//So, 60 frames = 1000 ms
		//=> 1 frame = 1000/60 ms
		//=> For 60 frames per second, we want to draw 1 frame every 1000/60 ms
		long msPerFrame = (long) (1000.0 / 60);
		long prev = System.currentTimeMillis();
		int fps = 0;
		while (true)
		{
			update();
			repaint(); //Draws new frame (Calls update())
			fps++;
			
			long curr = System.currentTimeMillis();
			if (curr - prev > 1000)
			{
				frame.setTitle("FPS : " + fps);
				fps = 0;
				prev = curr;
			}
			
			Thread.sleep(msPerFrame); //Delays execution of code by 'msPerFrame' milliseconds
		}
	}
	
	public void update()
	{
		Input input = new Input();
		if (prevMouseE != null)
			input.mouse = cam.getAbsoluteLocation(getPixelRelativeTo(prevMouseE));
		input.keys = this.keys;
		
		player.setInput(input); //Set player's input to inputs captured from canvas. This input will then be interpreted in Space update() method
		
		cam.setPos(player.getSnake().getHead());
		
		space.update();
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
		
		bg.translate(RENDER_WIDTH / 2, RENDER_HEIGHT / 2); //Makes (0, 0) the centre of our screen
		
		cam.render(space, bg);
		
		bg.translate(-RENDER_WIDTH / 2, -RENDER_HEIGHT / 2);
		
		player.getInventory().render(bg);
		bg.setFont(new Font(bg.getFont().getFontName(), Font.PLAIN, 24));
		bg.setColor(Color.BLACK);
		bg.drawString("SIZE : " + player.getSnake().getBody().size(), 10, RENDER_HEIGHT - 10);
		
		//Draws bufferImg on our original canvas
		g.drawImage(bufferImg, 0, 0, WIDTH, HEIGHT, null);
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		prevMouseE = e;
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
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
		Vector2i prev = cam.getAbsoluteLocation(getPixelRelativeTo(prevMouseE));
		Vector2i next = cam.getAbsoluteLocation(getPixelRelativeTo(e));
		cam.setPos(cam.getPos().subtract(next.subtract(prev)));
		
		prevMouseE = e;
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
		cam.calibrate(RENDER_WIDTH, RENDER_HEIGHT, cam.getPPU());
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
	
	//Returns Vector2i of coords of pixel relative to centre of screen
	private Vector2i getPixelRelativeTo(int x, int y)
	{
		return getPixelInOrigScreen(x, y).subtract(new Vector2i(RENDER_WIDTH / 2, RENDER_HEIGHT / 2));
	}
	
	//Returns Vector2i of coords of pixel relative to centre of screen if you pass the MouseEvent that generated the click on that pixel
	private Vector2i getPixelRelativeTo(MouseEvent e)
	{
		return getPixelRelativeTo(e.getX(), e.getY());
	}
	
	//Returns Vector2i of the coords that the pixel would be at, if screen resolution was RENDER_WIDTH x RENDER_HEIGHT
	//Basically finds the ratio of x and y coords [Eg if WIDTH = 1000 and x = 500, then we just return RENDER_WIDTH*0.5]
	private Vector2i getPixelInOrigScreen(int x, int y)
	{
		return new Vector2i((double) x / WIDTH * RENDER_WIDTH, (double) y / HEIGHT * RENDER_HEIGHT);
	}
	
	//We need to do this because when repaint() is called, the original update() method of Canvas class clears the screen before calling paint()
	//We never want to clear the screen. Explanation as to why given in paint() method
	@Override
	public void update(Graphics g)
	{
		paint(g);
	}
}
