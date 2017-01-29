package my2DMinecraft.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

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
	private Texture texture;
	private Vector3f position;
	
	private float movementSpeed;
	private float fallSpeed;
	private float jumpSpeed;
	private float fallAcceleration;
	private float maxFallSpeed;
	
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
		texture = new Texture(0, 103);
		position = new Vector3f();
		
		movementSpeed = 5.0f;
		fallSpeed = movementSpeed;
		jumpSpeed = -30.0f;
		fallAcceleration = 3.0f;
		maxFallSpeed = 20.0f;
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
		
		
		fall();
		handleInput();		
		World.camera.setPosition(position);
	}
	
	public void render()
	{		
		Shader.BLOCK.enable();
		mesh.bind();
		texture.bind();
		Shader.BLOCK.setUniformMatrix4f("vw_matrix", Matrix4f.translate(new Vector3f(-position.x, -position.y, 0.1f)).multiply(Matrix4f.translate(position)));
		BlockMesh.draw();
		texture.unbind();
		mesh.unBind();
		Shader.BLOCK.disable();
	}
	
	private void handleInput()
	{
		if(Input.isKeyDown(GLFW_KEY_D))
		{
			flipTexture(1);
			addPosition(new Vector3f(-movementSpeed, 0.0f, 0.0f));
		}
		
		if(Input.isKeyDown(GLFW_KEY_A))
		{
			flipTexture(-1);
			addPosition(new Vector3f(movementSpeed, 0.0f, 0.0f));
		}
		
		if(Input.isKeyDown(GLFW_KEY_W))
		{
			//addPosition(new Vector3f(0.0f, -movementSpeed, 0.0f));
			if(fallSpeed >= maxFallSpeed)
			{
				fallSpeed = jumpSpeed;
			}			
		}
		
		/*
		if(Input.isKeyDown(GLFW_KEY_S))
		{
			addPosition(new Vector3f(0.0f, movementSpeed, 0.0f));
		}
		*/
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
		if(!colision)
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
		
		if(pos1.x < pos2.x + (Block.BLOCK_SIZE * 2) - 10 && // -10 because the char does not fill the entire width of a block
			pos1.x + (Block.BLOCK_SIZE * 2) - 10 > pos2.x && //-10 because the char does not fill the entire width of a block
			pos1.y < pos2.y + (Block.BLOCK_SIZE * 2) &&
			pos1.y + (Block.BLOCK_SIZE * 2) + Block.BLOCK_SIZE > pos2.y)
		{
			return true;
		}
		
		return false;
	}
	
	private void fall()
	{
		if(fallSpeed <= maxFallSpeed)
		{
			//System.out.println("meh");
			fallSpeed += fallAcceleration;
		}
		
		addPosition(new Vector3f(0.0f, fallSpeed, 0.0f));
		addPosition(new Vector3f(0.0f, 1.0f, 0.0f));
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
}
