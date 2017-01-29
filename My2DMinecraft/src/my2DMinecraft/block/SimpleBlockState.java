package my2DMinecraft.block;

import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;

public class SimpleBlockState implements BlockState
{
	private BlockTexture texture;
	
	public SimpleBlockState(BlockTexture texture)
	{
		this.texture = texture;
	}
	
	@Override
	public void render(Matrix4f matrix)
	{
		texture.bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", matrix);
		BlockMesh.draw();
		texture.unbind();		
	}

	@Override
	public BlockState setTexture(BlockTexture newTexture)
	{
		if(newTexture.steps != 0)
		{
			return new AnimatedBlockState(newTexture);
		}
		else if(texture.isBackground && newTexture.isForeground)
		{
			return new DoubleBlockState(texture, newTexture);
		}
		else
		{
			texture = newTexture;
			return null;
		}		
	}

	@Override
	public boolean containsTexture(BlockTexture otherTexture)
	{
		return texture == otherTexture;
	}

	@Override
	public boolean isBackground()
	{
		return texture.isBackground;
	}
	
	public BlockTexture getTexture()
	{
		return texture;
	}
}
