package my2DMinecraft.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.util.HashMap;
import java.util.Map;

import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.ShaderUtils;

public class Shader
{
	public static final int VERTEX_ATTRIB = 0;
	public static final int TCOORD_ATTRIB = 1;
	
	public static Shader BLOCK;
	public static Shader BIRD;
	
	private boolean enabled = false;
	
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment)
	{
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public static void LoadAll()
	{
		BLOCK = new Shader("block.vert", "block.frag");
		BIRD = new Shader("bird.vert", "bird.frag");
	}
	
	public int getUniform(String name)
	{
		//so we don't have to ask for the location all the time
		if(locationCache.containsKey(name))
		{
			return locationCache.get(name);
		}
		
		int result = glGetUniformLocation(ID, name);
		if(result == -1)
		{
			System.err.println("Could not find uniform variable '" + name + "'!");
		}
		else
		{
			locationCache.put(name, result);
		}
		return result;
	}
	
	//used to send data to shader
	public void setUniform1i(String name, int value)
	{
		if(!enabled) enable();
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value)
	{
		if(!enabled) enable();
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform2f(String name, float x, float y)
	{
		if(!enabled) enable();
		glUniform2f(getUniform(name), x, y);
	}
	
	public void setUniform3f(String name, Vector3f vector)
	{
		if(!enabled) enable();
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniformMatrix4f(String name, Matrix4f matrix)
	{
		if(!enabled) enable();
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	public void enable()
	{
		glUseProgram(ID);
		enabled = true;
	}
	
	public void disable()
	{
		glUseProgram(0);
		enabled = false;
	}
}
