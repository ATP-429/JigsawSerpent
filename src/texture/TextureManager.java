package texture;

import java.util.HashMap;

public class TextureManager
{
	public static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static void addTexture(String texStr, String path)
	{
		Texture tex = new Texture(path);
		textures.put(texStr, tex);
	}
	
	public static Texture get(String texStr)
	{
		return textures.get(texStr);
	}
}
