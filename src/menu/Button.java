package menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import texture.Texture;
import texture.TextureManager;

public class Button
{
	private String texStr;
	private boolean hover;
	
	int width, height, x, y;
	
	Runnable onClick;
	
	public Button(String texStr, int x, int y)
	{
		Texture texture = TextureManager.get(texStr);
		width = texture.getWidth();
		height = texture.getHeight();
		
		this.texStr = texStr;
		this.x = x;
		this.y = y;
	}
	
	public void setOnClick(Runnable func)
	{
		onClick = func;
	}
	
	public void render(Graphics2D bg)
	{
		
	}
	
	public BufferedImage getImage()
	{
		return TextureManager.get(texStr).getImage();
	}
	
	public void setHover(boolean val)
	{
		hover = val;
	}
	
	public boolean isHovered()
	{
		return hover;
	}
}
