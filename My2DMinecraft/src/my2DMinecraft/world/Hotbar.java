package my2DMinecraft.world;

import static my2DMinecraft.graphics.BlockTexture.*;
import my2DMinecraft.block.Block;
import my2DMinecraft.block.BlockIcon;
import my2DMinecraft.graphics.BlockIconMesh;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;

public class Hotbar
{
	private static int selected;
	private Block frame;
	private Block selectedFrame;
	private BlockIcon[] icons;
	
	private final static int HOTBAR_SIZE = 6;
	
	public Hotbar()
	{
		selected = 0;
		
		frame = new Block();
		frame.setTexture(HOTBAR_1);
		selectedFrame = new Block();
		selectedFrame.setTexture(HOTBAR_2);
		
		icons = new BlockIcon[HOTBAR_SIZE];
		for (int i = 0; i < icons.length; i++)
		{
			icons[i] = new BlockIcon(AIR);
		}
		
		icons[0].setTexture(DIRT);
		icons[1].setTexture(GRASS);
		icons[2].setTexture(STONE);
		icons[3].setTexture(WOOD);
		icons[4].setTexture(SAND);
	}
	
	public void render()
	{
		Shader.BLOCK.enable();
		BlockMesh.bind();
		
		for(int i = 0; i < HOTBAR_SIZE; i++)
		{
			Matrix4f matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x - (((HOTBAR_SIZE / 2) - 1) * (Block.BLOCK_SIZE * 2) + Block.BLOCK_SIZE) + (i * (Block.BLOCK_SIZE * 2)),-World.camera.getPosition().y - (Window.HEIGHT / 2) + (Block.BLOCK_SIZE * 3), 0.9f)).multiply(World.camera.getProjection());
			frame.render(matrix);
		}
		
		//selected
		Matrix4f matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x - (((HOTBAR_SIZE / 2) - 1) * (Block.BLOCK_SIZE * 2) + Block.BLOCK_SIZE) + (selected * (Block.BLOCK_SIZE * 2)),-World.camera.getPosition().y - (Window.HEIGHT / 2) + (Block.BLOCK_SIZE * 3), 0.91f)).multiply(World.camera.getProjection());
		selectedFrame.render(matrix);
		
		BlockMesh.unbind();
		BlockIconMesh.bind();		
		
		for(int i = 0; i < HOTBAR_SIZE; i++)
		{
			matrix = Matrix4f.translate(new Vector3f(-World.camera.getPosition().x - (((HOTBAR_SIZE / 2) - 1) * (Block.BLOCK_SIZE * 2) + Block.BLOCK_SIZE) + (i * (Block.BLOCK_SIZE * 2)),-World.camera.getPosition().y - (Window.HEIGHT / 2) + (Block.BLOCK_SIZE * 3), 0.92f)).multiply(World.camera.getProjection());
			icons[i].render(matrix);
		}		
		
		BlockIconMesh.unbind();
		Shader.BLOCK.disable();
	}
	
	public void update()
	{
		
	}
	
	public static void scroll(double dir)
	{
		selected += dir;
		
		if(selected > HOTBAR_SIZE - 1)
		{
			selected = 0;
		}
		if(selected < 0)
		{
			selected = HOTBAR_SIZE - 1;
		}
	}
	
	public BlockTexture getSelectedTexture()
	{
		return icons[selected].getTexture();		
	}
}