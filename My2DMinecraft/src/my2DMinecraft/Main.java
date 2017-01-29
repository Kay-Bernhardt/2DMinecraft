package my2DMinecraft;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

import my2DMinecraft.input.Input;
import my2DMinecraft.utils.Timer;
import my2DMinecraft.utils.Window;

public class Main implements Runnable
{
	private Thread thread;
	private Game game;
	private Timer timer;
	
	public Main() 
	{
		thread = new Thread(this, "Game thread");
		thread.run();
	}
	
	@Override
	public void run()
	{
		init();
		gameloop();
		end();
	}
	
	private void init()
	{
		Window.setup();
		game = new Game();
	}
	
	private void gameloop()
	{
		System.out.println("starting game loop");
		timer = new Timer();
		
		while(!Window.shouldClose())
		{
			glfwPollEvents();
			Input.input();
			
			if(timer.shouldUpdate())
			{	
				game.update();
			}
						
			game.render();
			timer.addFrame();
			timer.updateCounter();
		}
	}
	
	private void end()
	{
		Window.end();
	}
	
	
	public static void main(String[] args)
	{
		new Main();
	}
}
