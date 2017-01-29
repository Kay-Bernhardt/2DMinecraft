package my2DMinecraft.world;

import static my2DMinecraft.graphics.BlockTexture.AIR;
import static my2DMinecraft.graphics.BlockTexture.DIRT;
import static my2DMinecraft.graphics.BlockTexture.FLOWER;
import static my2DMinecraft.graphics.BlockTexture.GRASS;
import static my2DMinecraft.graphics.BlockTexture.LAVA_1;
import static my2DMinecraft.graphics.BlockTexture.LEAF2;
import static my2DMinecraft.graphics.BlockTexture.STONE;
import static my2DMinecraft.graphics.BlockTexture.WATER_1;
import static my2DMinecraft.graphics.BlockTexture.WATER_4;
import static my2DMinecraft.graphics.BlockTexture.WATER_5;
import static my2DMinecraft.graphics.BlockTexture.WOOD;
import static my2DMinecraft.world.World.*;

import java.util.Random;

import my2DMinecraft.block.Block;

public class WorldGenerator
{
	private static Block[] world;
	private static Random rnd = new Random();
	
	private WorldGenerator(){}
	
	public static Block[] createWorld()
	{
		world = new Block[WORLD_SIZE];
		
		fillWithAir();
		dirtAndRock();
		grassAndLakes();
		generateDeko();
		
		
		lavaBorder();
		
		return world;
	}
	
	private static void fillWithAir()
	{
		for(int x = 0; x < WORLD_WIDTH; x++)
		{
			for(int y = WORLD_HEIGHT - 1; y > -1; y--)
			{
				world[x + y * WORLD_WIDTH] = new Block();
			}
		}
	}
	
	private static void lavaBorder()
	{
		for(int x = 0; x < WORLD_WIDTH; x++)
		{
			for(int y = WORLD_HEIGHT - 1; y > -1; y--)
			{
				if(x == 0)
				{
					world[x + y * WORLD_WIDTH].setTexture(LAVA_1);;
				}
			}
		}
	}
	
	private static void dirtAndRock()
	{
		//make dirt mountains and fill rock layer
		for(int y = WORLD_HEIGHT - 1; y > -1; y--)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if (y < WORLD_HEIGHT / 2 + 5 && y > WORLD_HEIGHT / 2 - 5)
				{
					if(rnd.nextInt(100) < 5)
					{
						world[x + y * WORLD_WIDTH].setTexture(DIRT);
					}
					if(world[x + (y + 1) * WORLD_WIDTH].containsTexture(DIRT))
					{
						try
						{
							int mountainWidth = rnd.nextInt(5) + 3;
							int start = mountainWidth / 2;
							int side = rnd.nextInt(2);
							for(int i = x - start + side; i <= x + start; i++)
							{
								world[i + y * WORLD_WIDTH].setTexture(DIRT);
							}
						}catch(Exception e){}						
					}
				}
				if(y == WORLD_HEIGHT / 2 - 5)
				{
					world[x + y * WORLD_WIDTH].setTexture(DIRT);
				}
				
				if(y < WORLD_HEIGHT / 2 - 5)
				{
					world[x + y * WORLD_WIDTH].setTexture(STONE);
				}
			}
		}
	}
	
	private static void grassAndLakes()
	{
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				//grass
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(DIRT) && world[x + y * WORLD_WIDTH].containsTexture(AIR))
				{
					world[x + y * WORLD_WIDTH].setTexture(GRASS);
				}
				//lake
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS))
				{
					boolean validArea = true;
					if(!world[(x - 2) + (y - 1) * WORLD_WIDTH].containsTexture(AIR) && !world[(x + 2) + (y - 1) * WORLD_WIDTH].containsTexture(AIR))
					{
						//System.out.println("checking potential lake spot");
						for(int i = x - 1 ; i <= x + 1 && validArea; i++)
						{
							//System.out.println(i);
							if(!world[i + (y - 1) * WORLD_WIDTH].containsTexture(GRASS))
							{
								validArea = false;
								//System.out.println("fail");
							}
						}
						if(validArea)
						{
							//System.out.println("placing lake at x: " + x);
							for(int i = x - 1 ; i <= x + 1 && validArea; i++)
							{
								world[i + (y - 1) * WORLD_WIDTH].setTexture(WATER_1);
							}
							for(int i = x - 1 ; i <= x + 1 && validArea; i++)
							{
								world[i + (y - 2) * WORLD_WIDTH].setTexture(WATER_4);
							}
							for(int i = x - 1 ; i <= x + 1 && validArea; i++)
							{
								world[i + (y - 3) * WORLD_WIDTH].setTexture(WATER_5);
							}
						}
					}					
				}		
			}
		}
	}
	
	private static void generateDeko()
	{
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{						
				//tree trunks
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && world[x + y * WORLD_WIDTH].containsTexture(AIR) && rnd.nextInt(100) < 10)
				{
					for(int ty = y; ty < y + rnd.nextInt(2) + 4; ty++)//tree trunk between 5 and 10 high
					{
						world[x + ty * WORLD_WIDTH].setTexture(WOOD);
					}
				}
				//tree leafs
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(WOOD) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].containsTexture(LEAF2)))
				{
					int leafsAbouveTrunk = 2;
					int leafsOnSide = 2;
					for(int ly = y - 2; ly < y + leafsAbouveTrunk + 1; ly++)
					{
						for(int lx = x - leafsOnSide; lx < x + leafsOnSide + 1; lx++)
						{
							try{
								if((world[lx + ly * WORLD_WIDTH].containsTexture(AIR) || world[lx + ly * WORLD_WIDTH].isBackground()) && !((ly == y + leafsAbouveTrunk) && (lx ==  x - leafsOnSide || lx == x + leafsOnSide)))
								{
									world[lx + ly * WORLD_WIDTH].setTexture(LEAF2);
								}
							}catch(Exception e){}	
							
						}
					}
				}
				//flowers
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 8)
				{
					world[x + y * WORLD_WIDTH].setTexture(FLOWER);
				}
			}
		}
	}
}
