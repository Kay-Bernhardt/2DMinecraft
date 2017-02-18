package my2DMinecraft.block;

import static my2DMinecraft.graphics.BlockTexture.*;

import my2DMinecraft.graphics.BlockIconMesh;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;

public class BlockIcon
{
	private BlockTexture texture;
	
	public BlockIcon()
	{
		texture = AIR;
	}
	
	public BlockIcon(BlockTexture tex)
	{
		texture = tex;
	}
	
	public void render(Matrix4f matrix)
	{
		texture.bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", matrix);
		BlockIconMesh.draw();
		texture.unbind();	
	}
	
	public void setTexture(BlockTexture tex)
	{
		texture = tex;
	}
	
	public BlockTexture getTexture()
	{
		return texture;
	}
}
