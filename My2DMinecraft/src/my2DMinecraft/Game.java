package my2DMinecraft;

import static org.lwjgl.opengl.GL11.*;

import my2DMinecraft.input.MouseButtonInput;
import my2DMinecraft.utils.Window;
import my2DMinecraft.world.World;

public class Game
{
	private World world;
	
	public Game()
	{
		world = new World();
	}
	
	public void update()
	{	
		world.update();
		MouseButtonInput.rightReleased = false;
	}
	
	public void render()
	{
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		world.render();
		
		int i = glGetError();
		if(i != GL_NO_ERROR)
		{
			System.out.println("Main render(): " + i);
		}
		
		Window.swapBuffer();
	}
}
