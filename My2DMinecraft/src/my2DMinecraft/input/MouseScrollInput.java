package my2DMinecraft.input;

import org.lwjgl.glfw.GLFWScrollCallback;

import my2DMinecraft.world.Hotbar;

public class MouseScrollInput extends GLFWScrollCallback
{
	@Override
	public void invoke(long window, double xoffset, double yoffset)
	{
		Hotbar.scroll(-yoffset);		
	}
}
