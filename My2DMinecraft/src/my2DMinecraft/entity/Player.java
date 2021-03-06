package my2DMinecraft.entity;

import static org.lwjgl.glfw.GLFW.*;

import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.BlockMesh;
import my2DMinecraft.graphics.Shader;
import my2DMinecraft.graphics.Texture;
import my2DMinecraft.graphics.VertexArray;
import my2DMinecraft.input.Input;
import my2DMinecraft.math.Matrix4f;
import my2DMinecraft.math.Vector3f;
import my2DMinecraft.world.World;

public class Player
{	
	private float[] vertices;
	private byte[]  indices;
	private float[] tcs;
	
	private VertexArray mesh;
	private Texture[] texture;
	private Vector3f position;
	
	private float movementSpeed;
	private float fallSpeed;
	private float jumpSpeed;
	private int jumpCount;
	private boolean isJumping;
	private float lastY;
	
	int wait;
	int animationStep;
	boolean animationTop;
	boolean animationBottom;
	boolean walking;
	
	private Vector3f[] collisionArray;
	
	public Player()
	{
		vertices = new float[]
		{
			-Block.BLOCK_SIZE, -Block.BLOCK_SIZE * 2, 0.0f,
			-Block.BLOCK_SIZE,  Block.BLOCK_SIZE * 2, 0.0f,
			 Block.BLOCK_SIZE,  Block.BLOCK_SIZE * 2, 0.0f,
			 Block.BLOCK_SIZE, -Block.BLOCK_SIZE * 2, 0.0f
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
		texture = new Texture[]{new Texture(0, 103), new Texture(1, 103), new Texture(2, 103)};
		position = new Vector3f();
		
		movementSpeed = 5.0f;
		fallSpeed = movementSpeed * 2;
		jumpSpeed = -movementSpeed * 1.5f;
		jumpCount = 0;
		isJumping = false;
		lastY = position.y;
		
		animationStep = 1;
		wait = 0;
		animationTop = false;
		animationBottom = false;
		walking = false;
	}
	
	private void flipTexture(int dir)
	{
		if(dir == 1 && tcs[0] == 1)
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
		else if(dir == -1 && tcs[0] == 0)
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
	
	public void update(Vector3f[] collisionArray)
	{
		this.collisionArray = collisionArray;
		walking = false;		
		
		fall();
		handleInput();		
		
		World.camera.setPosition(position);
		
		if(!walking)
		{
			animationStep = 1;
		}
		else
		{
			renderWalking();
		}
		
		lastY = position.y;
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
	
	public void render()
	{
		Shader.BLOCK.enable();
		mesh.bind();
		texture[animationStep].bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", Matrix4f.translate(new Vector3f(-position.x, -position.y, 0.1f)).multiply(World.camera.getProjection()));
		BlockMesh.draw();
		texture[animationStep].unbind();
		mesh.unBind();
		Shader.BLOCK.disable();
	}
	
	private void handleInput()
	{
		if(Input.isKeyDown(GLFW_KEY_D))
		{
			flipTexture(1);
			addPosition(new Vector3f(-movementSpeed, 0.0f, 0.0f));
			walking = true;
		}
		
		if(Input.isKeyDown(GLFW_KEY_A))
		{
			flipTexture(-1);
			addPosition(new Vector3f(movementSpeed, 0.0f, 0.0f));
			walking = true;
		}
		
		if(Input.isKeyDown(GLFW_KEY_W))
		{
			//addPosition(new Vector3f(0.0f, -movementSpeed, 0.0f));
			if(position.y == lastY)
			{
				isJumping = true;
			}			
		}		
		/*
		if(Input.isKeyDown(GLFW_KEY_S))
		{
			addPosition(new Vector3f(0.0f, movementSpeed, 0.0f));
		}
		*/		
	}
	
	private void fall()
	{
		if(isJumping)
		{
			if(jumpCount >= 15)
			{
				isJumping = false;
				jumpCount = 0;
				addPosition(new Vector3f(0.0f, 1.0f, 0.0f));
			}
			else
			{
				jumpCount++;
				addPosition(new Vector3f(0.0f, jumpSpeed, 0.0f));
			}
		}
		else
		{
			addPosition(new Vector3f(0.0f, fallSpeed, 0.0f));
			addPosition(new Vector3f(0.0f, 1.0f, 0.0f));
			addPosition(new Vector3f(0.0f, 0.1f, 0.0f));
		}
	}
	
	private void addPosition(Vector3f position)
	{
		Vector3f check = new Vector3f(this.position.x, this.position.y, 0.0f);
		check.add(position);
		
		boolean colision = false;
		for(int i = 0; i < collisionArray.length; i++)
		{
			if(collisionArray[i] != null)
			{
				if(isColliding(check, new Vector3f(collisionArray[i])))
				{
					colision = true;					
				}
			}			
		}
		if(!colision && inBounds(check))
		{
			this.position.add(position);
		}				
	}
	
	private boolean isColliding(Vector3f pos1 , Vector3f pos2)
	{
		//get pixel coordinates		
		pos2.x = -(World.LEFT + pos2.x * (Block.BLOCK_SIZE * 2));
		pos2.y = -(World.BOTTOM + pos2.y * (Block.BLOCK_SIZE * 2));
		
		//System.out.println("Player x: " + pos1.x + " Player y: " + pos1.y);
		//System.out.println("Block x: " + pos2.x + " Block y: " + pos2.y);
		
		if(pos1.x < pos2.x + (Block.BLOCK_SIZE * 2) - (Block.BLOCK_SIZE / 6) && // - because the char does not fill the entire width of a block
			pos1.x + (Block.BLOCK_SIZE * 2) - (Block.BLOCK_SIZE / 6) > pos2.x && //-10 because the char does not fill the entire width of a block
			pos1.y < pos2.y + (Block.BLOCK_SIZE * 2) &&
			pos1.y + (Block.BLOCK_SIZE * 2) + Block.BLOCK_SIZE > pos2.y)
		{
			return true;
		}
		
		return false;
	}
	
	public void setPosition(Vector3f position)
	{
		this.position = position;
		this.position.add(new Vector3f(00.f, 00.f, 0.1f));
	}
	
	public Vector3f getPosition()
	{
		return this.position;
	}
	
	public boolean inBounds(Vector3f pos)
	{
		
		Vector3f pos1 = new Vector3f(pos.x, -pos.y - ((Block.BLOCK_SIZE * 2) + 1), 0.0f);
		Vector3f pos2 = new Vector3f(World.LEFT, World.BOTTOM, 0.0f);
		
		if(pos1.x < pos2.x + (Block.BLOCK_SIZE * 2)) //right
		{
			return false;
		}
		else if(pos1.x > -World.LEFT) //left
		{
			return false;
		}
		else if (pos1.y < pos2.y)
		{
			return false;
		}
		else if(pos1.y + ((Block.BLOCK_SIZE * 4) + 2) > -pos2.y)
		{
			return false;
		}
		return true;
	}
}
