package texture;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
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
	
	public static void loadTextures()
	{
		loadTextures("res/");
	}
	
	private static void loadTextures(String path)
	{
		File folder = new File(path);
		String[] directories = folder.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File current, String name)
			{
				return new File(current, name).isDirectory();
			}
		});
		
		for (String dir : directories)
			loadTextures(path + "/" + dir);
		
		File[] files = folder.listFiles(new FileFilter()
		{
			@Override
			public boolean accept(File file)
			{
				return file.isFile();
			}
		});
		
		for (File file : files)
			addTexture(file.getName().substring(0, file.getName().indexOf('.')), file.getPath());
	}
}
