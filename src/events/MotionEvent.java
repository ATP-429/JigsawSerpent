package events;

import Utility.Vector2i;
import enemies.Entity;

public class MotionEvent extends Event
{
	public MotionEvent()
	{
		super();
		super.eventFunction = (Entity e) -> {
			e.movePos(new Vector2i(1.0 / 60, 0));
			return null;
		};
	}
	
	public MotionEvent(Vector2i deltaPerTick)
	{
		super();
		super.eventFunction = (Entity e) -> {
			e.movePos(deltaPerTick);
			return null;
		};
	}
	
	public MotionEvent(Vector2i deltaPerTick, int time)
	{
		super(time);
		super.eventFunction = (Entity e) -> {
			e.movePos(deltaPerTick);
			return null;
		};
	}
	
	public static MotionEvent MOTION_RIGHT()
	{
		return new MotionEvent(new Vector2i(4.0 / 60, 0), 15);
	}
	
	public static MotionEvent MOTION_LEFT()
	{
		return new MotionEvent(new Vector2i(-4.0 / 60, 0), 15);
	}
	
	public static MotionEvent MOTION_UP()
	{
		return new MotionEvent(new Vector2i(0, -4.0 / 60), 15);
	}
	
	public static MotionEvent MOTION_DOWN()
	{
		return new MotionEvent(new Vector2i(0, 4.0 / 60), 15);
	}
}
