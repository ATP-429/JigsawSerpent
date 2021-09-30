package tiles;

import java.awt.Graphics2D;

import main.Camera;

public interface Tile
{
	public void render(Camera cam, double x, double y, Graphics2D bg);
}
