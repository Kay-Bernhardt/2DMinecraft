package my2DMinecraft.gui;

import static my2DMinecraft.graphics.BlockTexture.*;
import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;
import my2DMinecraft.world.World;

public class CrystalCount
{
	private Block crystalLeft;
	private Block crystalRight;
	
	public CrystalCount()
	{
		crystalLeft = new Block();
		crystalRight = new Block();
		
		crystalLeft.setTexture(CRYSTAL_COUNT_LEFT);
		crystalRight.setTexture(CRYSTAL_COUNT_RIGHT);
	}
	
	public void render()
	{
		Shader.BLOCK.enable();
		BlockMesh.bind();		
		
			Matrix4f matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x + (Window.WIDTH / 2) - (Block.BLOCK_SIZE * 4),-World.camera.getPosition().y + (Window.HEIGHT / 2) - (Block.BLOCK_SIZE * 2), 0.9f)).multiply(World.camera.getProjection());
			crystalLeft.render(matrix);
			
			matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x + (Window.WIDTH / 2) - (Block.BLOCK_SIZE * 2) ,-World.camera.getPosition().y + (Window.HEIGHT / 2) - (Block.BLOCK_SIZE * 2), 0.9f)).multiply(World.camera.getProjection());
			crystalRight.render(matrix);
		
		BlockMesh.unbind();
		Shader.BLOCK.disable();
	}
}
