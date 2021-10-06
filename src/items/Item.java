package items;

import java.awt.Graphics2D;

import main.Camera;
import texture.Texture;
import texture.TextureManager;
import tiles.Tile;

public class Item implements Tile
{
	protected String texStr;
	
	public Item()
	{
		
	}
	
	public Item(String texStr)
	{
		this.texStr = texStr;
	}
	
	@Override
	public void render(Camera cam, double x, double y, Graphics2D bg)
	{
		Texture tex = TextureManager.get(texStr);
		cam.drawImage(bg, tex.getImage(), x, y, 1.0, 1.0);
	}
	
	public Texture getTexture()
	{
		return TextureManager.get(texStr);
	}
	
	public boolean isPickable()
	{
		return true;
	}
}
