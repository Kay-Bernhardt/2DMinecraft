package my2DMinecraft.block;

import static my2DMinecraft.graphics.BlockTexture.*;

import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.math.Matrix4f;

public class Block
{
	public static final int BLOCK_SIZE = 32 / 2;
	
	private BlockState state;
	
	public Block()
	{
		state = new SimpleBlockState(AIR);
	}
	
	public void render(Matrix4f matrix)
	{
		state.render(matrix);
	}
	
	public void update()
	{
		state.update();
	}
	
	public void setTexture(BlockTexture newTexture)
	{
		BlockState newState = state.setTexture(newTexture);
		if(newState != null)
		{
			state = newState;
		}
	}
	
	public boolean containsTexture(BlockTexture otherTexture)
	{
		return state.containsTexture(otherTexture);
	}
	
	public boolean isBackground()
	{
		return state.isBackground();
	}
	
	public BlockTexture getTexture()
	{
		return state.getTexture();
	}
}
