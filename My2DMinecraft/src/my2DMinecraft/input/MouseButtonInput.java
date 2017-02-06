package my2DMinecraft.input;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonInput extends GLFWMouseButtonCallback
{

	public static boolean leftClicked;
	public static boolean rightReleased;
	
	@Override
	public void invoke(long window, int button, int action, int mods)
	{
		if(button == 0)
		{
			leftClicked = action != GLFW_RELEASE; //sets to false when released
		}
		else if(button == 1)
		{
			rightReleased = action == GLFW_RELEASE; //reset at update end?
		}
		
	}

}
