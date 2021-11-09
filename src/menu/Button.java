package menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

import texture.Texture;
import texture.TextureManager;

public class Button extends JButton
{
	private String texStr;
	
	int width, height, x, y;
	
	Runnable onClick;
	
	public Button(String texStr, int x, int y)
	{
		super(texStr);
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
}
