package events;

import java.util.function.Function;

import enemies.Entity;

public class Event
{
	protected Function<Entity, Void> eventFunction;
	
	protected int ticks;
	protected int time;
	
	public Event()
	{
		ticks = 0;
		time = 60;
	}
	
	public Event(int time)
	{
		this.ticks=  0;
		this.time = time;
	}
	
	public void tick()
	{
		ticks++;
	}
	
	public boolean isOver()
	{
		return ticks >= time;
	}
	
	public void apply(Entity entity)
	{
		eventFunction.apply(entity);
	}
}
