package my2DMinecraft.block;

import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;

public class AnimatedBlockState implements BlockState
{
	private BlockTexture[] texture;
	int steps;
	int timeBetweenSteps;
	int counter;
	int currStep;	
	
	public AnimatedBlockState(BlockTexture texture)
	{		
		this.steps = texture.steps;
		this.texture = new BlockTexture[steps];
		BlockTexture[] temp = BlockTexture.values();
		
		for(int i = texture.ordinal(), j = 0; i < texture.ordinal() + steps && j < steps; i++, j++)
		{
			this.texture[j] = temp[i];
		}
		
		timeBetweenSteps = 60;
		currStep = 0;
		counter = 0;//new Random().nextInt(timeBetweenSteps);
	}
	
	@Override
	public void render(Matrix4f matrix)
	{		
		texture[currStep].bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", matrix);
		BlockMesh.draw();
		texture[currStep].unbind();
		
		if(counter >= timeBetweenSteps)
		{
			counter = 0;
			
			if(currStep < steps - 1)
			{
				currStep++;
			}
			else
			{
				currStep = 0;
			}
		}
		else
		{
			counter++;
		}		
	}

	@Override
	public BlockState setTexture(BlockTexture newTexture)
	{
		if(newTexture.steps != 0)
		{
			return new AnimatedBlockState(newTexture);
		}
		else
		{
			return new SimpleBlockState(newTexture);
		}		
	}

	@Override
	public boolean containsTexture(BlockTexture otherTexture)
	{
		return texture[0] == otherTexture;
	}

	@Override
	public boolean isBackground()
	{
		return false;
	}
	
	public BlockTexture getTexture()
	{
		return texture[0];
	}
}
