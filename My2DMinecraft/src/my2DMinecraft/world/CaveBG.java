package my2DMinecraft.world;

import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.graphics.VertexArray;
import my2DMinecraft.math.Matrix4f;

public class CaveBG
{
public static VertexArray mesh;	
	
	public CaveBG()
	{
		float[] vertices = new float[]
		{
				-World.LEFT * 2, -World.BOTTOM + (6 * Block.BLOCK_SIZE), -0.5f,
				-World.LEFT * 2,  World.BOTTOM, -0.5f,
				 World.LEFT,  World.BOTTOM, -0.5f,
				 World.LEFT, -World.BOTTOM + (6 * Block.BLOCK_SIZE), -0.5f
		};
		
		byte[] indices = new byte[]
		{
				0, 1, 2,
				2, 3, 0
		};
		
		float[] tcs = new float[]
		{
				0.0f, 1.0f,
				0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
	}
	
	public void render(Matrix4f matrix)
	{
		bind();
		Shader.BG.setUniformMatrix4f("vw_matrix", matrix);
		draw();
		unbind();
	}
	
	public void bind()
	{
		mesh.bind();
	}
	
	public void draw()
	{
		mesh.draw();
	}
	
	public void unbind()
	{
		mesh.unBind();
	}
}
