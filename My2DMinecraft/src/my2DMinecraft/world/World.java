package my2DMinecraft.world;

import static my2DMinecraft.graphics.BlockTexture.*;

import my2DMinecraft.block.Block;
import my2DMinecraft.entity.Player;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.input.MouseButtonInput;
import my2DMinecraft.input.MouseInput;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;

public class World
{
	public static final int WORLD_WIDTH = 500;
	public static final int WORLD_HEIGHT = 128;
	public static final int WORLD_SIZE = WORLD_WIDTH * WORLD_HEIGHT;
	public static final int LEFT = -(WORLD_WIDTH * (Block.BLOCK_SIZE * 2)) / 2;
	public static final int BOTTOM = -(WORLD_HEIGHT * (Block.BLOCK_SIZE * 2)) / 2;
	private final int VIEW_X = Window.getWidth() / (Block.BLOCK_SIZE * 2) + 4;
	private final int VIEW_Y = Window.getHeight() / (Block.BLOCK_SIZE * 2) + 4;
	
	private Block[] world;
	
	public static Camera camera;
	public Player player;
	
	public World()
	{
		new BlockMesh();
		camera = new Camera();
		world = WorldGenerator.createWorld();
		player = new Player();
		
		
		
		Vector3f pos =	calculatePositionInBlocks(camera.getPosition());
		boolean validLocation = false;
		while (!validLocation)
		{
			if(world[(int)pos.x + (int)pos.y * WORLD_WIDTH].containsTexture(AIR))
			{
				validLocation = true;
			}
			camera.addPosition(new Vector3f(0.0f, -(Block.BLOCK_SIZE * 2), 0.0f));
			pos = calculatePositionInBlocks(camera.getPosition());
		}
		player.setPosition(camera.getPosition());
	}
	
	public void update()
	{
		player.update(getSolidBlocksAroundPlayerPosition());		
		
		if(MouseButtonInput.leftClicked)
		{
			Vector3f vec = new Vector3f(((camera.getPosition().x + ((Window.WIDTH / 2) - (float)MouseInput.mouseX))),
												 ((camera.getPosition().y - (Window.HEIGHT/ 2)) + (float)MouseInput.mouseY), 0.0f);	
			
			Vector3f blockpos = calculateAbsoluteBlockPos(vec);
			
			if(!world[(int)blockpos.x + (int)blockpos.y * WORLD_WIDTH].containsTexture(AIR))
			{
				world[(int)blockpos.x + (int)blockpos.y * WORLD_WIDTH].setTexture(AIR);
			}			
		}		
	}
	
	private Vector3f calculateAbsoluteBlockPos(Vector3f pos)
	{		
		pos.x = (((LEFT - pos.x) - Block.BLOCK_SIZE) / (Block.BLOCK_SIZE * 2));		
		pos.y = -(((BOTTOM + pos.y) - (Block.BLOCK_SIZE * 3)) / (Block.BLOCK_SIZE * 2));		
		return pos;
	}
	
	
	public void render()
	{		
		renderBlocks();
		
		player.render(/*Matrix4f.translate(new Vector3f(-camera.getPosition().x, -camera.getPosition().y,0)).multiply(camera.getProjection())*/);
		
		
	}
	
	private void renderBlocks()
	{
		Vector3f pos = calculatePositionInBlocks(camera.getPosition());
		//render blocks in world		
		Shader.BLOCK.enable();
		BlockMesh.bind();
		
		for(int x = 0; x < VIEW_X; x++)
		{
			for(int y = 0; y < VIEW_Y; y++)
			{
				//only render blocks in view
				Block b = null;
				int bx = (x + (int)pos.x - (VIEW_X / 2));
				int by = (y + (int)pos.y - (VIEW_Y / 2));
				
				try
				{					
					b = world[bx + by * WORLD_WIDTH];
				}catch(ArrayIndexOutOfBoundsException e) {}
				
				if(b != null)
				{
					Matrix4f matrix = Matrix4f.translate(new Vector3f(LEFT + bx * (Block.BLOCK_SIZE * 2), BOTTOM + by * (Block.BLOCK_SIZE * 2), 0)).multiply(camera.getProjection());
					b.render(matrix);
				}
			}
		}
		
		/*
		Block b = world[0];
		b.setTexture(STONE);
		Matrix4f matrix = Matrix4f.translate(new Vector3f(-camera.getPosition().x - (Window.WIDTH / 2) + (float)MouseInput.mouseX,
																		  -camera.getPosition().y + (Window.HEIGHT/ 2) - (float)MouseInput.mouseY, 0.2f)).multiply(Matrix4f.translate(camera.getPosition()));
		b.render(matrix);
		
		BlockMesh.unbind();
		Shader.BLOCK.disable();	
		*/
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	private Vector3f calculatePositionInBlocks(Vector3f position)
	{
		Vector3f pos = new Vector3f();
		//the block the position is currently pointing at
		pos.x = ((int) position.x / (Block.BLOCK_SIZE * 2));
		pos.x = WORLD_WIDTH / 2 - pos.x;
		
		pos.y = ((int) position.y / (Block.BLOCK_SIZE * 2));
		pos.y = WORLD_HEIGHT / 2 - pos.y;
		
		return pos;
	}
	
	
	private Vector3f[] getBlocksAroundPosition(Vector3f pos)
	{		
		int arrayWidth = 3;
		int arrayHeight = 4;
		Vector3f[] vectorArray = new Vector3f[arrayWidth * arrayHeight];
		for(int x = 0; x < arrayWidth; x++) //one block on either side
		{
			for(int y = 0; y < arrayHeight; y++) //one block above and below
			{
				Vector3f vector = new Vector3f();
				vector.x = (x + pos.x - (arrayWidth / 2));
				vector.y = (y + pos.y - (arrayHeight / 2));
				
				//System.out.println(vector.x);
				
				vectorArray[x + y * arrayWidth] = vector;							
			}
		}
		
		return vectorArray;
	}
	
	private Vector3f[] getSolidBlocksAroundPlayerPosition()
	{
		Vector3f pos = calculatePositionInBlocks(player.getPosition());		
		
		int arrayWidth = 3;
		int arrayHeight = 4;
		Vector3f[] vectorArray = new Vector3f[arrayWidth * arrayHeight];
		for(int x = 0; x < arrayWidth; x++) //one block on either side
		{
			for(int y = 0; y < arrayHeight; y++) //one block above and below
			{
				Vector3f vector = new Vector3f();
				vector.x = (x + pos.x - (arrayWidth / 2));
				vector.y = (y + pos.y - (arrayHeight / 2));
				
				//System.out.println(vector.x);
				
				if(world[(int)vector.x + (int)vector.y * WORLD_WIDTH].getTexture().isSolid)
				{
					vectorArray[x + y * arrayWidth] = vector;
				}
				else
				{
					vectorArray[x + y * arrayWidth] = null;
				}				
			}
		}
		
		return vectorArray;
	}
	
	
}
