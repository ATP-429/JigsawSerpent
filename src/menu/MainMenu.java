package menu;

import main.GameHandler;
import main.Main;

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
		
		/*Button optionsButton = new Button("options_button", 0, 0);
		add(optionsButton);*/
	}
}
