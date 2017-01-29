package my2DMinecraft.world;

import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;

public class Camera
{
	private Vector3f position;
	//private Matrix4f projection;
	
	public Camera()
	{
		position = new Vector3f();
		//projection = new Matrix4f();
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
	}
	
	public Matrix4f getProjection()
	{
		return Matrix4f.translate(position);
	}
	
	public void addPosition(Vector3f position)
	{
		this.position.add(position);
		this.position.add(new Vector3f(00.f, 00.f, -0.1f));
	}
	
	public Vector3f getPosition()
	{
		return this.position;
	}
}
