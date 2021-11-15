package enemies;

import Utility.Vector2i;
import events.Event;
import events.MotionEvent;

public class Rat extends Entity
{
	private final double PROB_OF_MOVING_PER_TICK = 0.015;
	
	public Rat(Vector2i pos)
	{
		super("rat", pos); 
		
	
	}
	
	public void update()
	{
		super.update();
		
		if(events.size() == 0)
		{
			if(Math.random() < PROB_OF_MOVING_PER_TICK)
			{
				if(Math.random() < 0.25)
				{
					texStr = "ratright";
					this.addEvent(MotionEvent.MOTION_RIGHT());
				}
				else if(Math.random() < 0.5)
				{
					texStr = "ratleft";
						this.addEvent(MotionEvent.MOTION_LEFT());
				}
				else if(Math.random() < 0.75)
				{
					texStr = "ratup";
				
					this.addEvent(MotionEvent.MOTION_UP());
				}
				else
				{
					texStr = "rat";
					
					this.addEvent(MotionEvent.MOTION_DOWN());
				}
			}
		}
}
}