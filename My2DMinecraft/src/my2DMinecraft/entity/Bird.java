package my2DMinecraft.entity;

import java.util.Random;

import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.graphics.Texture;
import my2DMinecraft.graphics.VertexArray;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.utils.Window;
import my2DMinecraft.world.World;

public class Bird
{
	private float[] vertices;
	private byte[]  indices;
	private float[] tcs;
	
	private VertexArray mesh;
	private Texture[] texture;
	private Vector3f position;
	
	private int wait;
	private int animationStep;
	private boolean animationTop;
	private boolean animationBottom;
	private boolean walking;
	
	private Vector3f speed;
	private int altitude;
	
	private static float id;
	
	public Bird()
	{
		vertices = new float[]
		{
			-Block.BLOCK_SIZE, -Block.BLOCK_SIZE, 0.0f,
			-Block.BLOCK_SIZE,  Block.BLOCK_SIZE, 0.0f,
			 Block.BLOCK_SIZE,  Block.BLOCK_SIZE, 0.0f,
			 Block.BLOCK_SIZE, -Block.BLOCK_SIZE, 0.0f
		};
		
		indices = new byte[]
		{
			0, 1, 2,
			2, 3, 0
		};
		
		tcs = new float[]
		{
			0.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 0.0f,
			1.0f, 1.0f
		};
		
		mesh = new VertexArray(vertices, indices, tcs);
		texture = new Texture[]{new Texture(0, 6), new Texture(1, 6), new Texture(2, 6)};
		Random rnd = new Random();
		
		id += 0.001;
		
		animationStep = rnd.nextInt(3);
		wait = rnd.nextInt(7);
		animationTop = false;
		animationBottom = false;
		walking = true;
		speed = new Vector3f(Block.BLOCK_SIZE / 4 + (-rnd.nextFloat() + rnd.nextFloat()), 0.0f, 0.0f);
		altitude = rnd.nextInt(5) + 5;		
		
		position = new Vector3f((World.LEFT + (Window.getWidth() / 2) + rnd.nextInt(2000)), (-altitude - 10) * (Block.BLOCK_SIZE * 2), 0.1f + id); //spawn 20 blocks up
		if(!rnd.nextBoolean())
		{
			position.x *= -1;
		}		
		flipTexture(-1);
	}
	
	public void update()
	{
		Vector3f tryPos = new Vector3f(position);
		tryPos.add(speed);
		if(isInBounds(tryPos))
		{
			addPosition(speed);
		}
		else
		{
			speed.x = -speed.x;
			flipTexture((int)-speed.x);
		}
		
		addPosition(new Vector3f(0.0f, checkAltitude() * (Block.BLOCK_SIZE/8), 0.0f));
		
		if(!walking)
		{
			animationStep = 1;
		}
		else
		{
			renderWalking();
		}
	}
	
	public void render()
	{
		Shader.BIRD.enable();
		mesh.bind();
		texture[animationStep].bind();
		Shader.BIRD.setUniformMatrix4f("vw_matrix", Matrix4f.translate(new Vector3f(-position.x + World.camera.getPosition().x, -position.y + World.camera.getPosition().y, position.z)));
		BlockMesh.draw();
		texture[animationStep].unbind();
		mesh.unBind();
		Shader.BIRD.disable();
	}
	
	private int checkAltitude()
	{
		if(World.isPositionSolid(new Vector3f(position.x, position.y + (altitude * (Block.BLOCK_SIZE * 2)), 0)) && World.isPositionSolid(new Vector3f(position.x, position.y + ((altitude + 1) * (Block.BLOCK_SIZE * 2)), 0)))
		{
			return -1;
		}
		else if(!World.isPositionSolid(new Vector3f(position.x, position.y + (altitude * (Block.BLOCK_SIZE * 2)), 0)) && !World.isPositionSolid(new Vector3f(position.x, position.y + ((altitude + 1) * (Block.BLOCK_SIZE * 2)), 0)))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	private void flipTexture(int dir)
	{
		if(dir > 0 && tcs[0] == 1)
		{
			tcs = new float[] //D
			{
					0.0f, 1.0f,
					0.0f, 0.0f,
					1.0f, 0.0f,
					1.0f, 1.0f
			};
			
			mesh = new VertexArray(vertices, indices, tcs);
		}
		else if(dir < 0 && tcs[0] == 0)
		{
			tcs = new float[]	//A
			{
					1.0f, 1.0f,
					1.0f, 0.0f,
					0.0f, 0.0f,
					0.0f, 1.0f
			};
			
			mesh = new VertexArray(vertices, indices, tcs);
		}		
	}
	
	private void renderWalking()
	{
		wait++;
		
		if(wait >= 7)
		{
			if(animationTop)
			{
				//count down
				if(animationStep > 0)
				{
					animationStep--;
					animationBottom = false;
				}
				else
				{
					animationTop = false;
					animationBottom = true;
				}
				
			}
			else if(animationBottom)
			{
				if(animationStep < 2)
				{
					animationStep++;
				}
				else
				{
					animationBottom = false;
					animationTop = true;
				}
			}
			else
			{
				animationBottom = true;
			}			
			wait = 0;
		}
	}
	
	private void addPosition(Vector3f position)
	{		
		this.position.add(position);			
	}
	
	public boolean isInBounds(Vector3f pos)
	{		
		int VIEW_X = Window.getWidth() / 2;
		int VIEW_Y = Window.getHeight() / 2;
		
		Vector3f pos1 = new Vector3f(pos.x, -pos.y - VIEW_Y, 0.0f);
		Vector3f pos2 = new Vector3f(World.LEFT + VIEW_X, World.BOTTOM, 0.0f);
		
		if(pos1.x < pos2.x)
		{
			return false;
		}
		else if(pos1.x + VIEW_X > -World.LEFT)
		{
			return false;
		}
		else if (pos1.y < pos2.y)
		{
			return false;
		}
		else if(pos1.y + (2 * VIEW_Y) > -pos2.y)
		{
			return false;
		}
		return true;
	}
}
