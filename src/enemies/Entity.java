package enemies;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import Utility.Vector2i;
import events.Event;
import main.Camera;
import texture.Texture;
import texture.TextureManager;

public class Entity
{
	protected List<Event> events;
	protected String texStr;
	protected Vector2i pos;
	
	public Entity(String texStr, Vector2i pos)
	{
		this.texStr = texStr;
		this.pos = pos;
		events = new ArrayList<Event>();
	}
	
	public void update()
	{
		for(int i = 0; i < events.size(); i++)
		{
			Event event = events.get(i);
			
			event.apply(this);
			
			event.tick();
			if(event.isOver())
			{
				events.remove(i);
				i--;
			}
		}
	}
	
	public void addEvent(Event event)
	{
		events.add(event);
	}
	
	public void render(Camera cam, Graphics2D bg)
	{
		Texture texture = TextureManager.get(texStr);
		cam.drawImage(bg, texture.getImage(), pos.x-0.5, pos.y-0.5, 1.0, 1.0);
	}
	
	public void setPos(Vector2i pos)
	{
		this.pos = pos;
	}
	
	public Vector2i getPos()
	{
		return pos;
	}
	
	public void movePos(Vector2i delta)
	{
		this.pos = this.pos.add(delta);
	}
}
