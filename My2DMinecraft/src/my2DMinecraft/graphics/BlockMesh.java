package my2DMinecraft.graphics;

import my2DMinecraft.block.Block;

public class BlockMesh
{
	private static VertexArray mesh;
	
	
	public BlockMesh()
	{
		float[] vertices = new float[]
		{
				-Block.BLOCK_SIZE, -Block.BLOCK_SIZE, 0.0f,
				-Block.BLOCK_SIZE,  Block.BLOCK_SIZE, 0.0f,
				 Block.BLOCK_SIZE,  Block.BLOCK_SIZE, 0.0f,
				 Block.BLOCK_SIZE, -Block.BLOCK_SIZE, 0.0f
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
	
	public static void bind()
	{
		mesh.bind();
	}
	
	public static void draw()
	{
		mesh.draw();
	}
	
	public static void unbind()
	{
		mesh.unBind();
	}
}
