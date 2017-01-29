package my2DMinecraft.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseInput extends GLFWCursorPosCallback
{
	public static double mouseX;
	public static double mouseY;
	
	@Override
	public void invoke(long window, double xpos, double ypos)
	{
		mouseX = xpos;
		mouseY = ypos;
	}

}
