package events;

import Utility.Vector2i;
import main.Snake;

public class AttackEvent extends SnakeEvent
{
	public AttackEvent()
	{
		time = 30;
	}
	
	@Override
	public void apply(Snake snake)
	{
		snake.speed = 3;
		//snake.moveTowardsDir(snake.getBody().peekLast());
		snake.dir = snake.getBody().peekLast();
	}
	
	@Override
	public void end(Snake snake)
	{
		snake.speed = 1;
	}
}
