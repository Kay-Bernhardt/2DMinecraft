package my2DMinecraft.utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import my2DMinecraft.graphics.Shader;
import my2DMinecraft.input.Input;
import my2DMinecraft.input.MouseButtonInput;
import my2DMinecraft.input.MouseInput;
import my2DMinecraft.math.Matrix4f;

public class Window
{
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	private static final String TITLE = "My 2D Minecraft";
	
	private static long window = 0;
	
	private Window()	{	}
	
	public static void setup()
	{
		System.out.println("Initializing");
		
		initGlfw();
		initWindow();
		initGL();
		
		System.out.println("Initializing complete");
	}
	
	private static void initGlfw()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit())
		{
			throw new IllegalStateException("GLFW Failed to initialize!");
		}
	}
	
	private static void initWindow()
	{
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);		
		if(window == NULL)
		{
			throw new IllegalStateException("GLFW window creation failed");
		}
		
		glfwSetKeyCallback(window, new Input());
		glfwSetCursorPosCallback(window, new MouseInput());
		glfwSetMouseButtonCallback(window, new MouseButtonInput());
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2); //center on screen
		
		//glfwSwapInterval(1); // Enable VSync, which effective caps the frame-rate of the application to 60 frames-per-second
		glfwMakeContextCurrent(window);
		
		glfwShowWindow(window);
	}	
	
	private static void initGL()
	{
		GL.createCapabilities();
		
		glEnable(GL_DEPTH_TEST);
		glActiveTexture(GL_TEXTURE1);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glClearColor(0.4f, 0.5f, 1f, 1.0f);
		
		Shader.LoadAll();	
		
		Matrix4f pr_matrix = Matrix4f.orthographic(-WIDTH / 2, WIDTH / 2, -HEIGHT / 2, HEIGHT / 2, -1.0f, 1.0f);		
		Shader.BLOCK.setUniformMatrix4f("pr_matrix", pr_matrix);
		Shader.BLOCK.setUniform1i("tex", 1);
		
		Shader.BIRD.setUniformMatrix4f("pr_matrix", pr_matrix);
		Shader.BIRD.setUniform1i("tex", 1);
	}
	
	public static void swapBuffer()
	{
		glfwSwapBuffers(window);
	}
	
	public static void end()
	{
		Callbacks.glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static boolean shouldClose()
	{
		return glfwWindowShouldClose(window);
	}
	
	public static void setShouldClose()
	{
		glfwSetWindowShouldClose(window, true);
	}

	public static int getWidth()
	{
		return WIDTH;
	}

	public static int getHeight()
	{
		return HEIGHT;
	}
	
	public static void setTitle(String str)
	{
		glfwSetWindowTitle(window, TITLE + " " + str);
	}
}
