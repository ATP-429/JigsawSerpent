package tiles;
import java.awt.Color;
import java.awt.Graphics2D;

import main.Camera;
import tiles.Tile;

public class Wall implements Tile
{
	@Override
	public void render(Camera cam, double x, double y, Graphics2D bg)
	{
		bg.setColor(Color.BLACK);
		cam.fillRect(bg, x, y, 1, 1);
	}
}
