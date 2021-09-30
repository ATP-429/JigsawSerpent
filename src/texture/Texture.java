package texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture
{
	private BufferedImage texImg;
	
	public Texture(String path)
	{
		try
		{
			texImg = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public int getWidth()
	{
		return texImg.getWidth();
	}
	
	public int getHeight()
	{
		return texImg.getHeight();
	}
	
	public BufferedImage getImage()
	{
		return texImg;
	}
}
