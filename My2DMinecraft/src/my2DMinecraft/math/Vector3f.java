package my2DMinecraft.math;

public class Vector3f
{
	public float x, y, z;	//z to determine if something is in front of something else
	
	public Vector3f()
	{
		x = 0.0f;
		y = 0.0f;
		z = 0.0f;
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f vector)
	{
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}
	
	public void add(Vector3f vector)
	{
		this.x += vector.x;
		this.y += vector.y;
		this.z += vector.z;
	}
	
	public String toString()
	{
		return "X: " + x + " Y: " + y + " Z: " + z;
	}
}
