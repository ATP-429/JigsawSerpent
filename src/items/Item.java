package items;

import java.awt.Graphics2D;

import main.Camera;
import texture.Texture;
import texture.TextureManager;
import tiles.Tile;

public class Item extends Tile
{
	public Texture getTexture()
	{
		return TextureManager.get(texStr);
	}
	
	public boolean isPickable()
	{
		return true;
	}
}
