package my2DMinecraft.gui;

import static my2DMinecraft.graphics.BlockTexture.*;

import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;
import my2DMinecraft.world.World;

public class HealthBar
{
	private int healthbarSize = 5;
	private Block[] healthbar = new Block[healthbarSize];
	
	public HealthBar()
	{
		for (int i = 0; i < healthbar.length; i++)
		{
			healthbar[i] = new Block();
			healthbar[i].setTexture(HEALTH_HEART);
		}
	}
	
	public void render()
	{
		Shader.BLOCK.enable();
		BlockMesh.bind();
		
		for(int i = 0; i < healthbarSize; i++)
		{
			Matrix4f matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x - (Window.WIDTH / 2) + (Block.BLOCK_SIZE * 2) + (i * (Block.BLOCK_SIZE * 2)),-World.camera.getPosition().y + (Window.HEIGHT / 2) - (Block.BLOCK_SIZE * 2), 0.9f)).multiply(World.camera.getProjection());
			healthbar[i].render(matrix);
		}
		
		BlockMesh.unbind();
		Shader.BLOCK.disable();
	}
	
	public void takeDamage()
	{
		boolean tookDmg = false;
		
		for (int i = healthbarSize - 1; i >= 0 && !tookDmg; i--)
		{
			if(healthbar[i].getTexture() == HEALTH_HEART)
			{
				healthbar[i].setTexture(HEALTH_EMPTY);
				tookDmg = true;
			}
		}
	}
	
	public void gainHealth()
	{
		boolean healed = false;
		
		for (int i = 0; i < healthbarSize && !healed; i++)
		{
			if(healthbar[i].getTexture() == HEALTH_EMPTY)
			{
				healthbar[i].setTexture(HEALTH_HEART);
				healed = true;
			}
		}
	}
}
