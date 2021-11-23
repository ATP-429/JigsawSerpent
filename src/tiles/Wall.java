package tiles;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Camera;

public class Wall extends Tile
{
	public Wall()
	{
		this.collision = true;
		this.texStr = "metal_dark";
	}
}
