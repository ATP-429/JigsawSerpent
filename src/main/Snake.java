package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import Utility.Vector2i;
import Utility.Vector3i;
import events.SnakeEvent;
import foods.Food;

public class Snake
{
	protected List<SnakeEvent> events;
	
	public static final int DEFAULT_SNAKE_LENGTH = 50;
	public static final double MAX_TURN_ANGLE = 0.15;
	public static final double SNAKE_UNIT = 0.05;
	
	public static final int MAX_SNAKE_LENGTH = 200;
	
	//SNAKE FEATURES
	public static final double SNAKE_BORDER_WIDTH = 0.01; //Thickness of snake's border
	public static final double SNAKE_EYE_RADIUS = 0.05;
	public static final double SNAKE_EYE_SEPARATION = 0.15;
	public static final int SNAKE_DEFAULT_SPEED = 1;
	
	private double snakeWidth = 0.3;
	
	public int speed = SNAKE_DEFAULT_SPEED;
	
	public Action action = Action.IDLE;
	
	private ArrayDeque<Vector2i> body;
	private Vector2i tail, head;
	private int foodEaten;
	
	private double units = 0;
	
	public Vector2i dir;
	
	public Snake()
	{
		tail = new Vector2i(4, 4);
		head = tail;
		body = new ArrayDeque<Vector2i>();
		foodEaten = 0;
		
		//Add default body parts to snake (Default snake is facing east and its tail starts at (0, 0))
		for (int i = 0; i < DEFAULT_SNAKE_LENGTH; i++)
			body.add(new Vector2i(SNAKE_UNIT, 0));
		
		for (Vector2i dir : body)
			head = head.add(dir);
		
		events = new ArrayList<SnakeEvent>();
	}
	
	//Returns the direction in which snake must move, if coordinates of mouse are passed to this function
	public Vector2i getDir(Vector2i point)
	{
		Vector2i dir = point.subtract(head); //Get the next body part of snake based on where snake wants to move towards
		//if (dir.getMagnitude() >= 5 * SNAKE_UNIT) //Move only if next body part is greater than 5 snake units, that is, snake moves by a signifcant amount
		{
			//Rotate only by MAX_TURN_ANGLE even if snake is told to move to an angle larger than that
			if (Vector2i.angleBetween(dir, body.peekLast()) >= MAX_TURN_ANGLE)
			{
				Vector2i newDir1 = body.peekLast().rotate(MAX_TURN_ANGLE);
				Vector2i newDir2 = body.peekLast().rotate(-MAX_TURN_ANGLE);
				if (newDir1.subtract(dir).getMagnitude() < newDir2.subtract(dir).getMagnitude())
					dir = newDir1;
				else
					dir = newDir2;
			}
		}
		return dir.normalize().multiply(SNAKE_UNIT);
	}
	
	//NOTE: MAKES SNAKE MOVE TOWARDS 'DIR' BY ADDING 'DIR' TO ITS BODY
	//THIS MEANS MAGNITUDE OF 'DIR' SHOULD BE EQUAL TO SNAKE_UNIT
	public void moveTowardsDir(Vector2i dir)
	{
		dir = dir.normalize().multiply(SNAKE_UNIT);
		for (int i = 0; i < speed; i++)
		{
			addBody(dir);
			removeTail();
		}
	}
	
	public void moveTowardsPoint(Vector2i point)
	{
		Vector2i dir = getDir(point);
		moveTowardsDir(dir);
	}
	
	//NOTE : LEFT AND RIGHT DO NOT NECESSARILY MEAN IT'S ACTUALLY THE LEFT OR RIGHT SIDE OF SNAKE. LEFT AND RIGHT IS JUST TO DIFFERENTIATE BETWEEN THE TWO SIDES.
	public void render(Camera cam, Graphics2D bg)
	{
		Vector2i prevSpine = this.getTail(), prevSpineLeft = null, prevSpineRight = null, nextSpineLeft = null, nextSpineRight = null;
		int index = body.size(); //We set i to body.size() and then decrement it in the loop. This is because we want the front of the snake to remain constant
		Vector2i[] bodyArr = null;
		bodyArr = body.toArray(new Vector2i[0]);
		for (Vector2i dir : bodyArr)
		{
			Vector2i nextSpine = prevSpine.add(dir); //Next point that makes up the snake
			
			Vector2i perp = dir.cross(new Vector3i(0, 0, 1)).toVector2i().normalize();
			
			if (prevSpineLeft == null || prevSpineRight == null)
			{
				prevSpineLeft = prevSpine.add(perp.multiply(snakeWidth / 2));
				prevSpineRight = prevSpine.subtract(perp.multiply(snakeWidth / 2));
			}
			nextSpineLeft = nextSpine.add(perp.multiply(snakeWidth / 2));
			nextSpineRight = nextSpine.subtract(perp.multiply(snakeWidth / 2));
			
			GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
			path.moveTo(prevSpineLeft.x, prevSpineLeft.y);
			path.lineTo(prevSpineRight.x, prevSpineRight.y);
			path.lineTo(nextSpineRight.x, nextSpineRight.y);
			path.lineTo(nextSpineLeft.x, nextSpineLeft.y);
			path.lineTo(prevSpineLeft.x, prevSpineLeft.y);
			
			AffineTransform scaleAT = new AffineTransform();
			scaleAT.scale(cam.getPPU(), cam.getPPU());
			path.transform(scaleAT);
			
			/*if(i % 10 == 0)
			{
				bg.setColor(Color.YELLOW);
				cam.fillOval(bg, prevSpine.x-snakeWidth/2, prevSpine.y-snakeWidth/2, snakeWidth, snakeWidth);
			}*/
			
			//Draw a yellow scale every 4th line
			if (index % 4 == 0)
				bg.setColor(Color.YELLOW);
			else
				bg.setColor(Color.BLUE);
			bg.fill(path);
			
			//Draw snake's border
			bg.setStroke(new BasicStroke((float) (SNAKE_BORDER_WIDTH * cam.getPPU())));
			bg.setColor(Color.BLACK);
			bg.draw(path);
			bg.setStroke(new BasicStroke(1));
			
			//We need to make sure prevSpineLeft = nextSpineLeft after one iteration, otherwise each individual rectangle of the snake is visible
			prevSpine = nextSpine;
			prevSpineLeft = nextSpineLeft;
			prevSpineRight = nextSpineRight;
			
			index--;
		}
		
		//Draw snake head as bezier curve
		Vector2i perp = body.peekLast().cross(new Vector3i(0, 0, 1)).toVector2i().normalize();
		
		Vector2i controlPoint1 = this.getHead().add(body.peekLast().multiply(8)).subtract(perp.multiply(0.2));
		Vector2i controlPoint2 = this.getHead().add(body.peekLast().multiply(8)).add(perp.multiply(0.2));
		
		GeneralPath path = new GeneralPath(GeneralPath.WIND_NON_ZERO);
		path.moveTo(prevSpineLeft.x, prevSpineLeft.y);
		path.lineTo(prevSpineRight.x, prevSpineRight.y);
		
		path.curveTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, prevSpineLeft.x, prevSpineLeft.y);
		
		AffineTransform scaleAT = new AffineTransform();
		scaleAT.scale(cam.getPPU(), cam.getPPU());
		path.transform(scaleAT);
		
		bg.setColor(Color.BLUE);
		bg.fill(path);
		bg.setColor(Color.BLACK);
		bg.setStroke(new BasicStroke((float) (SNAKE_BORDER_WIDTH * cam.getPPU())));
		bg.draw(path);
		bg.setStroke(new BasicStroke(1));
		
		//Draw snake's eyes
		bg.setColor(Color.WHITE);
		Vector2i leftEye = this.getHead().add(body.peekLast().multiply(3)).add(perp.multiply(SNAKE_EYE_SEPARATION / 2));
		Vector2i rightEye = this.getHead().add(body.peekLast().multiply(3)).subtract(perp.multiply(SNAKE_EYE_SEPARATION / 2));
		cam.fillOvalAt(bg, leftEye.x, leftEye.y, SNAKE_EYE_RADIUS, SNAKE_EYE_RADIUS);
		cam.fillOvalAt(bg, rightEye.x, rightEye.y, SNAKE_EYE_RADIUS, SNAKE_EYE_RADIUS);
		bg.setColor(Color.BLACK);
		cam.drawOvalAt(bg, leftEye.x, leftEye.y, SNAKE_EYE_RADIUS, SNAKE_EYE_RADIUS);
		cam.drawOvalAt(bg, rightEye.x, rightEye.y, SNAKE_EYE_RADIUS, SNAKE_EYE_RADIUS);
	}
	
	public void addEvent(SnakeEvent event)
	{
		events.add(event);
	}
	
	public void eat(Food food)
	{
		foodEaten += food.getSize();
	}
	
	public void addBody(Vector2i dir)
	{
		body.addLast(dir);
		head = head.add(dir);
	}
	
	public void removeTail()
	{
		if (foodEaten > 0 && body.size() <= MAX_SNAKE_LENGTH)
		{
			if (snakeWidth < 0.4)
				snakeWidth += 0.001;
			foodEaten--;
		}
		else
		{
			tail = tail.add(body.peekFirst());
			body.removeFirst();
		}
	}
	
	public Vector2i getTail()
	{
		return tail;
	}
	
	public Vector2i getHead()
	{
		return head;
	}
	
	public Deque<Vector2i> getBody()
	{
		return body;
	}
	
	public double getSnakeWidth()
	{
		return snakeWidth;
	}
	
	public List<SnakeEvent> getEvents()
	{
		return events;
	}
}
