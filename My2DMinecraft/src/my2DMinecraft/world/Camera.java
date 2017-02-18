package my2DMinecraft.world;

import my2DMinecraft.block.Block;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;

public class Camera
{
	private Vector3f position;
	
	public Camera()
	{
		position = new Vector3f();
	}
	
	public Matrix4f getProjection()
	{
		return Matrix4f.translate(position);
	}
	
	public void setPosition(Vector3f position)
	{
		Vector3f pos = new Vector3f(position);
		this.position = inBounds(pos);
	}
	
	public void addPosition(Vector3f position)
	{
		this.position.add(position);
		if(this.position.z > -0.5 && !(this.position.z < -0.9))
		{
			this.position.add(new Vector3f(00.f, 00.f, -0.1f));
		}		
	}
	
	
	public Vector3f inBounds(Vector3f pos)
	{
		//TODO cleanup
		
		int VIEW_X = Window.getWidth() / 2;
		int VIEW_Y = Window.getHeight() / 2;
		
		Vector3f pos1 = new Vector3f(pos.x, -pos.y - VIEW_Y, 0.0f);
		Vector3f pos2 = new Vector3f(World.LEFT + VIEW_X, World.BOTTOM, 0.0f);
		
		if(pos1.x < pos2.x + Block.BLOCK_SIZE) //right
		{
			//return new Vector3f(pos2.x, pos.y, -0.8f);
			pos.x = pos2.x + Block.BLOCK_SIZE;
		}
		if(pos1.x + VIEW_X > -World.LEFT)
		{
			//return new Vector3f(-pos2.x, pos.y, -0.8f);
			pos.x = -pos2.x + Block.BLOCK_SIZE;
		}
		if (pos1.y < pos2.y)
		{
			//return new Vector3f(pos.x, -pos2.y - VIEW_Y, -0.8f);
			pos.y = -pos2.y - VIEW_Y;
		}
		if(pos1.y + (2 * VIEW_Y) > -pos2.y)
		{
			//return new Vector3f(pos.x, pos2.y + VIEW_Y, -0.8f);
			pos.y = pos2.y + VIEW_Y;
		}
		return pos;
	}
	
	public Vector3f getPosition()
	{
		return new Vector3f(position);
	}
}
