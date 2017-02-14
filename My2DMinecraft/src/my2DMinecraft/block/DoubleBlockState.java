package my2DMinecraft.block;

import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;

public class DoubleBlockState implements BlockState
{
	private BlockTexture texture1;
	private BlockTexture texture2;
	
	public DoubleBlockState(BlockTexture texture1, BlockTexture texture2)
	{
		this.texture1 = texture1;
		this.texture2 = texture2;
	}
	
	@Override
	public void render(Matrix4f matrix)
	{
		texture1.bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", (Matrix4f.translate(new Vector3f(00.f, 00.f, -0.1f)).multiply(matrix)));
		BlockMesh.draw();
		texture1.unbind();
		
		texture2.bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", matrix);
		BlockMesh.draw();
		texture2.unbind();				
	}

	@Override
	public BlockState setTexture(BlockTexture newTexture)
	{
		if(newTexture.steps != 0)
		{
			return new AnimatedBlockState(newTexture);
		}
		else if(newTexture.isForeground)
		{
			texture2 = newTexture;
			return null;
		}
		else
		{
			return new SimpleBlockState(newTexture);
		}
	}

	@Override
	public boolean containsTexture(BlockTexture otherTexture)
	{
		return texture1 == otherTexture || texture2 == otherTexture;
	}

	@Override
	public boolean isBackground()
	{
		return texture1.isBackground;
	}
	
	public BlockTexture getTexture()
	{
		return texture1;
	}

	@Override
	public void update()
	{
		// TODO Auto-generated method stub
		
	}
}
