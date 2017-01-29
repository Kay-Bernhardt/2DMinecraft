package my2DMinecraft.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonInput extends GLFWMouseButtonCallback
{

	public static boolean leftClicked;
	
	@Override
	public void invoke(long window, int button, int action, int mods)
	{
		leftClicked = action != GLFW_RELEASE; //sets to false when released
		
	}

}
