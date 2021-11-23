package tiles;

import java.awt.Graphics2D;

import main.Camera;
import texture.Texture;
import texture.TextureManager;

public class Tile
{
	protected String texStr;
	protected boolean collision;
	
	public Tile()
	{
		
	}
	
	public Tile(String texStr)
	{
		this.texStr = texStr;
	}
	
	public void render(Camera cam, double x, double y, Graphics2D bg)
	{
		Texture texture = TextureManager.get(texStr);
		cam.drawImage(bg, texture.getImage(), x, y, 1.0, 1.0);
	}
	
	public boolean hasCollision()
	{
		return collision;
	}
}
