package menu;

import java.awt.Graphics2D;

import main.Camera;
import main.GameHandler;
import main.Main;
import texture.TextureManager;

public class MainMenu extends Menu
{
	public MainMenu(Main game)
	{
		super(game);
		Button playButton = new Button("play_button", 458, 234);
		playButton.setOnClick(new Runnable()
		{
			@Override
			public void run()
			{
				game.pop();
				GameHandler gameHandler = new GameHandler();
				game.push(gameHandler);
				gameHandler.start();
				System.out.println("Game started");
			}
		});
		add(playButton);
		
		Button instructionsButton = new Button("instructions_button", 314, 332);
		instructionsButton.setOnClick(new Runnable()
		{
			@Override
			public void run()
			{
				menuHandler.push(new InstructionMenu(game));
			}
		});
		add(instructionsButton);
		
		Button quitButton = new Button("quit_button", 468, 434);
		quitButton.setOnClick(new Runnable()
		{
			@Override
			public void run()
			{
				System.exit(0);
			}
		});
		add(quitButton);
		
		/*Button optionsButton = new Button("options_button", 0, 0);
		add(optionsButton);*/
	}
	
	@Override
	public void render(Camera cam, Graphics2D bg)
	{
		cam.translate(bg);
		cam.drawImage(bg, TextureManager.get("main_menu_background").getImage(), 0, 0, Main.RENDER_WIDTH, Main.RENDER_HEIGHT);
		cam.reset(bg);
		super.render(cam, bg);
	}
}
