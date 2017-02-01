package my2DMinecraft.world;

import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;

public class Camera
{
	private Vector3f position;
	//private Matrix4f projection;
	
	public Camera()
	{
		position = new Vector3f();
		//projection = new Matrix4f();
	}
	
	public Matrix4f getProjection()
	{
		return Matrix4f.translate(position);
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = isValidPosition(position);
		System.out.println(this.position);
	}
	
	public void addPosition(Vector3f position)
	{
		this.position.add(position);
		this.position.add(new Vector3f(00.f, 00.f, -0.1f));
	}
	
	
	public Vector3f isValidPosition(Vector3f pos)
	{
		//TODO rename and cleanup
		
		int VIEW_X = Window.getWidth() / 2;
		int VIEW_Y = Window.getHeight() / 2;
		
		Vector3f pos1 = new Vector3f(pos.x, -pos.y - VIEW_Y, 0.0f);
		Vector3f pos2 = new Vector3f(World.LEFT + VIEW_X, World.BOTTOM, 0.0f);
		
		if(pos1.x < pos2.x)
		{
			return new Vector3f(pos2.x, pos.y, 0.0f);
		}
		else if(pos1.x + VIEW_X > -World.LEFT)
		{
			return new Vector3f(-pos2.x, pos.y, 0.0f);
		}
		else if (pos1.y < pos2.y)
		{
			return new Vector3f(pos.x, -pos2.y - VIEW_Y, 0.0f);
		}
		else if(pos1.y + (2 * VIEW_Y) > -pos2.y)
		{
			return new Vector3f(pos.x, pos2.y + VIEW_Y, 0.0f);
		}
		return pos;
	}
	
	public Vector3f getPosition()
	{
		return new Vector3f(position);
	}
}
