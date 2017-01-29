package my2DMinecraft.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWKeyCallback;

import my2DMinecraft.utils.Window;

public class Input extends GLFWKeyCallback
{
	public static boolean[] keys = new boolean[GLFW_KEY_LAST];
	//private static final float SPEED = 10.0f;
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods)
	{
		keys[key] = action != GLFW_RELEASE;
	}
	

	public static boolean isKeyDown(int keycode)
	{
		return keys[keycode];
	}
	
	public static void input()
	{
		if(isKeyDown(GLFW_KEY_ESCAPE))
		{
			Window.setShouldClose();
		}
		/*
		if(isKeyDown(GLFW_KEY_D))
		{
			World.camera.addPosition(new Vector3f(-SPEED, 0.0f, 0.0f));
		}
		
		if(isKeyDown(GLFW_KEY_A))
		{
			World.camera.addPosition(new Vector3f(SPEED, 0.0f, 0.0f));
		}
		
		if(isKeyDown(GLFW_KEY_W))
		{
			World.camera.addPosition(new Vector3f(0.0f, -SPEED, 0.0f));
		}
		
		if(isKeyDown(GLFW_KEY_S))
		{
			World.camera.addPosition(new Vector3f(0.0f, SPEED, 0.0f));
		}
		*/
	}
}
