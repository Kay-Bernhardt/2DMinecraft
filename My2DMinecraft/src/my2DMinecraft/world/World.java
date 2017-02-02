package my2DMinecraft.world;

import static my2DMinecraft.graphics.BlockTexture.*;

import my2DMinecraft.block.Block;
import my2DMinecraft.entity.Bird;
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
	
	private static Block[] world;
	
	public static Camera camera;
	public Player player;
	public Bird[] birds;
	
	private Vector3f mouseBlockpos;
	private Vector3f trueMousepos;
	private boolean isBreaking;
	private int wait;
	private Block breakBlock;	
	
	public World()
	{
		System.out.println(LEFT + " " + BOTTOM);
		
		new BlockMesh();
		camera = new Camera();
		world = WorldGenerator.createWorld();
		
		player = new Player();
		birds = new Bird[100];
		
		for(int i = 0; i < birds.length; i++)
		{
			birds[i] = new Bird();
		}
		
		breakBlock = new Block();
		breakBlock.setTexture(BREAKING);
		
		Vector3f pos =	calculateBlockPos(camera.getPosition());
		boolean validLocation = false;
		while (!validLocation)
		{
			if(world[(int)pos.x + (int)pos.y * WORLD_WIDTH].containsTexture(AIR))
			{
				validLocation = true;
			}
			camera.addPosition(new Vector3f(0.0f, -(Block.BLOCK_SIZE * 2), 0.0f));
			pos = calculateBlockPos(camera.getPosition());
		}
		camera.addPosition(new Vector3f(0, -Block.BLOCK_SIZE, 0));
		player.setPosition(camera.getPosition());
	}
	
	public void update()
	{
		player.update(getSolidBlocksAroundPlayerPosition());
		for(int i = 0; i < birds.length; i++)
		{
			birds[i].update();
		}
		
		if(MouseButtonInput.leftClicked)
		{			
			trueMousepos = new Vector3f(camera.getPosition().x - ((float)MouseInput.mouseX - (Window.WIDTH /2)), (camera.getPosition().y - (Window.HEIGHT/ 2) + (float)MouseInput.mouseY) + (Block.BLOCK_SIZE * 2), 0.0f);
			
			mouseBlockpos = calculateBlockPos(new Vector3f(trueMousepos));
			Vector3f absBlockpos = calculateBlockPos(trueMousepos);
			
			if(!world[(int)absBlockpos.x + (int)absBlockpos.y * WORLD_WIDTH].containsTexture(AIR))
			{
				//render block breakTexture
				wait++;
				isBreaking = true;
				if(wait == 20)
				{
					world[(int)absBlockpos.x + (int)absBlockpos.y * WORLD_WIDTH].setTexture(AIR);
					isBreaking = false;
					wait = 0;
				}				
			}
		}
		else
		{
			isBreaking = false;
			wait = 0;
		}
	}
	
	private static Vector3f calculateBlockPos(Vector3f position)
	{		
		Vector3f pos = new Vector3f(position);				
		pos.x = -((LEFT + pos.x) - Block.BLOCK_SIZE) / (Block.BLOCK_SIZE * 2);		
		pos.y = -((BOTTOM + pos.y) - (Block.BLOCK_SIZE * 3)) / (Block.BLOCK_SIZE * 2);		
		return pos;
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
	
	
	public void render()
	{		
		renderBlocks();
		player.render();
		
		for(int i = 0; i < birds.length; i++)
		{
			birds[i].render();
		}
	}
	
	private void renderBlocks()
	{
		Vector3f pos = calculateBlockPos(camera.getPosition());		
		//render blocks in world		
		Shader.BLOCK.enable();
		BlockMesh.bind();
		
		Block b = null;
		
		for(int x = 0; x < VIEW_X; x++)
		{
			for(int y = 0; y < VIEW_Y; y++)
			{
				//only render blocks in view				
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
		
		//block breaking texture
		if(isBreaking)
		{			
			Matrix4f matrix = Matrix4f.translate(new Vector3f((LEFT + (int)mouseBlockpos.x * (Block.BLOCK_SIZE * 2)) + camera.getPosition().x, (BOTTOM + (int)mouseBlockpos.y * (Block.BLOCK_SIZE * 2)) + camera.getPosition().y, 0));
			breakBlock.render(matrix);
		}				
		
		BlockMesh.unbind();
		Shader.BLOCK.disable();
	}
	
	public Camera getCamera()
	{
		return camera;
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
	
	public static boolean isPositionSolid(Vector3f position)
	{
		Vector3f pos = position;
		pos = calculateBlockPos(position);
		
		return world[(int)pos.x + (int)pos.y * WORLD_WIDTH].getTexture().isSolid;
	}
}
