package my2DMinecraft.world;

import static my2DMinecraft.graphics.BlockTexture.*;
import static my2DMinecraft.world.World.*;

import java.util.Random;

import my2DMinecraft.block.Block;
import my2DMinecraft.graphics.BlockTexture;
import my2DMinecraft.utils.SimplexNoise_octave;

public class WorldGenerator
{
	private static Block[] world;
	private static Random rnd = new Random();
	
	private WorldGenerator(){}
	
	public static Block[] createWorld()
	{
		System.out.println("Creating World...");
		
		world = new Block[WORLD_SIZE];
		
		fillWithAir();
		dirtLayer();
		rockLayer();
		simplexStone();
		simplexDirt();
		
		placeGold();
		placeCrystals();
		
		grassLayer();
		lakes();			
		sandUnderLakes();	
		
		generateDeko();		
		
		border();
		
		System.out.println("Done creating world");
		return world;
	}
	
	private static void simplexStone()
	{
		SimplexNoise_octave sn = new SimplexNoise_octave((int)System.currentTimeMillis());
		float[][] simplex = sn.generateSimplexNoise(WORLD_WIDTH, WORLD_HEIGHT / 2);
		for(int y = 0; y < WORLD_HEIGHT / 2; y++)
		{
			for(int x = 0; x < World.WORLD_WIDTH; x++)
			{
				if(simplex[x][y] > 0.36f)
				{
					world[x + y * WORLD_WIDTH].setTexture(STONE);
				}
				else
				{
					world[x + y * WORLD_WIDTH].setTexture(AIR);
				}
				
			}
		}
	}
	
	private static void simplexDirt()
	{
		SimplexNoise_octave sn = new SimplexNoise_octave((int)System.currentTimeMillis());
		float[][] simplex = sn.generateSimplexNoise(WORLD_WIDTH, WORLD_HEIGHT / 2);
		for(int y = 0; y < WORLD_HEIGHT / 2; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if(simplex[x][y] > 0.8f && !world[x + y * WORLD_WIDTH].containsTexture(AIR))
				{
					world[x + y * WORLD_WIDTH].setTexture(DIRT);
				}				
			}
		}
	}
	
	private static void fillWithAir()
	{
		System.out.println("Filling world with air");
		for(int x = 0; x < WORLD_WIDTH; x++)
		{
			for(int y = WORLD_HEIGHT - 1; y > -1; y--)
			{
				world[x + y * WORLD_WIDTH] = new Block();
			}
		}
	}
	
	private static void border()
	{
		for(int x = 0; x < WORLD_WIDTH; x++)
		{
			for(int y = WORLD_HEIGHT - 1; y > -1; y--)
			{
				if(y == 0)
				{
					world[x + y * WORLD_WIDTH].setTexture(LAVA_5);
				}
				if(y == 1)
				{
					world[x + y * WORLD_WIDTH].setTexture(LAVA_4);
				}
				if(y == 2)
				{
					world[x + y * WORLD_WIDTH].setTexture(LAVA_1);
				}
			}
		}
	}
	
	private static void rockLayer()
	{
		System.out.println("Creating rock layer");
		try
		{
			for(int y = WORLD_HEIGHT / 2 + 7; y > WORLD_HEIGHT / 2 - 3; y--)
			{
				for(int x = 0; x < WORLD_WIDTH; x++)
				{
					if (y < WORLD_HEIGHT / 2 + 5 && y > WORLD_HEIGHT / 2 - 5)
					{
						if(rnd.nextInt(100) < 5)
						{
							world[x + y * WORLD_WIDTH].setTexture(STONE);
						}
						if(world[x + (y + 1) * WORLD_WIDTH].containsTexture(STONE))
						{
							
							int mountainWidth = rnd.nextInt(5) + 5;
							int start = mountainWidth / 2;
							int side = rnd.nextInt(2);
							for(int i = x - start + side; i <= x + start; i++)
							{
								world[i + y * WORLD_WIDTH].setTexture(STONE);
							}													
						}
					}
					if(y <= WORLD_HEIGHT / 2 - 5)
					{
						world[x + y * WORLD_WIDTH].setTexture(STONE);
					}
				}
				
			}
			//remove peaks
			for(int y = WORLD_HEIGHT / 2 + 6; y > WORLD_HEIGHT / 2 - 6; y--)
			{
				for(int x = 0; x < WORLD_WIDTH; x++)
				{
					if(world[x + y * WORLD_WIDTH].containsTexture(STONE) && world[(x - 1) + y * WORLD_WIDTH].containsTexture(AIR) && world[(x + 1) + y * WORLD_WIDTH].containsTexture(AIR))
					{
						world[x + y * WORLD_WIDTH].setTexture(AIR);
					}
				}
			}
		}catch(Exception e){System.out.println("RockLayer out of bounds");}
	}
	
	private static void dirtLayer()
	{
		System.out.println("Creating dirt layer");
		//make dirt mountains and fill rock layer
		try
		{
		for(int y = WORLD_HEIGHT / 2 + 12; y > WORLD_HEIGHT / 2 - 6; y--)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if (y < WORLD_HEIGHT / 2 + 8 && y > WORLD_HEIGHT / 2 - 5)
				{
					if(world[x + y * WORLD_WIDTH].containsTexture(AIR) && rnd.nextInt(100) < 5)
					{
						world[x + y * WORLD_WIDTH].setTexture(DIRT);
					}
					if((world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].containsTexture(DIRT)) && world[x + (y + 1) * WORLD_WIDTH].containsTexture(DIRT))
					{
						
							int mountainWidth = rnd.nextInt(5) + 6;
							int start = mountainWidth / 2;
							int side = rnd.nextInt(2);
							for(int i = x - start + side; i <= x + start; i++)
							{
								if(world[i + y * WORLD_WIDTH].containsTexture(STONE))
								{
									if(rnd.nextInt(5) == 0)
									{
										world[i + y * WORLD_WIDTH].setTexture(DIRT);
									}
								}
								else
								{
									world[i + y * WORLD_WIDTH].setTexture(DIRT);
								}								
							}
												
					}
				}
			}
		}
		//remove peaks
		for(int y = WORLD_HEIGHT / 2 + 12; y > WORLD_HEIGHT / 2 - 6; y--)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if(world[x + y * WORLD_WIDTH].containsTexture(DIRT) && world[(x - 1) + y * WORLD_WIDTH].containsTexture(AIR) && world[(x + 1) + y * WORLD_WIDTH].containsTexture(AIR))
				{
					world[x + y * WORLD_WIDTH].setTexture(AIR);
				}
			}
		}
		}catch(Exception e){System.out.println("DirtLayer out of bounds");}
	}
	
	private static void grassLayer()
	{
		System.out.println("Placing grass");
		try{
		for(int y = WORLD_HEIGHT / 2; y < WORLD_HEIGHT / 2 + 20; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				//grass
				if((world[x + (y - 1) * WORLD_WIDTH].containsTexture(DIRT) || world[x + (y - 1) * WORLD_WIDTH].containsTexture(STONE)) && world[x + y * WORLD_WIDTH].containsTexture(AIR))
				{
					world[x + y * WORLD_WIDTH].setTexture(GRASS);
				}
			}
		}
		//fill gaps
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT / 2 + 20; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if(world[x + y * WORLD_WIDTH].containsTexture(AIR) && world[(x - 1) + y * WORLD_WIDTH].containsTexture(GRASS) && world[(x + 1) + y * WORLD_WIDTH].containsTexture(GRASS))
				{
					world[x + y * WORLD_WIDTH].setTexture(GRASS);
					world[x + (y - 1) * WORLD_WIDTH].setTexture(DIRT);
				}
			}
		}
		}catch(Exception e){System.out.println("Grass out of bounds");}
	}
	
	private static void lakes()
	{
		System.out.println("Placing lakes");
		int numLakes = 0;
		try{
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT / 2 + 20; y++)
		{
			for(int x = 4; x < WORLD_WIDTH - 4; x++)
			{
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && rnd.nextInt(100) < 10)
				{
					boolean small = false;
					boolean medium = false;
					boolean large = false;
					
					for(int i = -4; i < 0; i++)
					{
						if(world[(x - i) + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && world[(x + i) + (y - 1) * WORLD_WIDTH].containsTexture(GRASS))
						{
							if(i == - 4)
							{
								large = true;
							}
							else if (i == - 3)
							{
								medium = true;
							}
							else if (i == - 2)
							{
								small = true;
							}
						}
					}
					if(large)
					{
						numLakes++;
						for(int i = x - 3 ; i <= x + 3; i++)
						{
							world[i + (y - 1) * WORLD_WIDTH].setTexture(WATER_1);
						}
						for(int i = x - 2 ; i <= x + 2; i++)
						{
							world[i + (y - 2) * WORLD_WIDTH].setTexture(WATER_4);
						}
						for(int i = x - 1 ; i <= x + 1; i++)
						{
							world[i + (y - 3) * WORLD_WIDTH].setTexture(WATER_5);
						}
					}
					else if(medium)
					{
						numLakes++;
						for(int i = x - 2 ; i <= x + 2; i++)
						{
							world[i + (y - 1) * WORLD_WIDTH].setTexture(WATER_1);
						}
						for(int i = x - 1 ; i <= x + 1; i++)
						{
							world[i + (y - 2) * WORLD_WIDTH].setTexture(WATER_4);
						}
					}
					else if(small)
					{
						//System.out.println("placing lake at x: " + x);
						numLakes++;
						for(int i = x - 1 ; i <= x + 1; i++)
						{
							world[i + (y - 1) * WORLD_WIDTH].setTexture(WATER_1);
						}
					}										
				}		
			}
		}
		//fill gaps
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT / 2 + 20; y++)
		{
			for(int x = 4; x < WORLD_WIDTH - 4; x++)
			{
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && world[(x + 1) + (y - 1) * WORLD_WIDTH].containsTexture(WATER_1) && world[(x - 1) + (y - 1) * WORLD_WIDTH].containsTexture(WATER_1))
				{
					world[x + (y - 1) * WORLD_WIDTH].setTexture(WATER_1);
				}
			}
		}
		}catch(Exception e){System.out.println("Lakes out of bounds");}
		System.out.println("Number of Lakes: " + numLakes);
	}
	
	private static void sandUnderLakes()
	{
		for(int y = WORLD_HEIGHT / 2 - 5; y < WORLD_HEIGHT / 2 + 20; y++)
		{
			for(int x = 4; x < WORLD_WIDTH - 4; x++)
			{
				if((world[x + y * WORLD_WIDTH].containsTexture(WATER_1) || world[x + y * WORLD_WIDTH].containsTexture(WATER_4) || world[x + y * WORLD_WIDTH].containsTexture(WATER_5)) && !(world[x + (y - 1) * WORLD_WIDTH].containsTexture(WATER_1) || world[x + (y - 1) * WORLD_WIDTH].containsTexture(WATER_4) || world[x + (y - 1) * WORLD_WIDTH].containsTexture(WATER_5)))
				{
					world[x + (y - 1) * WORLD_WIDTH].setTexture(SAND);
				}
				if(world[x + y * WORLD_WIDTH].containsTexture(WATER_1) && world[(x - 1) + y * WORLD_WIDTH].containsTexture(GRASS))
				{
					world[(x - 1) + y * WORLD_WIDTH].setTexture(SAND);
				}
				if(world[x + y * WORLD_WIDTH].containsTexture(WATER_1) && world[(x + 1) + y * WORLD_WIDTH].containsTexture(GRASS))
				{
					world[(x + 1) + y * WORLD_WIDTH].setTexture(SAND);
				}
			}
		}
	}
	
	private static void placeGold()
	{
		for(int y = 2; y < WORLD_HEIGHT / 2 + 7; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if(world[x + y * WORLD_WIDTH].containsTexture(STONE) && rnd.nextInt(100) < 1)
				{
					world[x + y * WORLD_WIDTH].setTexture(GOLD);
				}
				
				if(world[x + y * WORLD_WIDTH].containsTexture(STONE) &&  world[x + (y - 1) * WORLD_WIDTH].containsTexture(GOLD) && !isNextToGold(x, y - 1))
				{
					if(rnd.nextBoolean())
					{
						world[x + y * WORLD_WIDTH].setTexture(GOLD);
					}
					else
					{
						world[(x + 1) + (y - 1) * WORLD_WIDTH].setTexture(GOLD);
					}
				}
				else if(world[x + y * WORLD_WIDTH].containsTexture(STONE) &&  world[x + (y - 1) * WORLD_WIDTH].containsTexture(GOLD) && rnd.nextInt(100) < 20)
				{
					world[x + y * WORLD_WIDTH].setTexture(GOLD);
					
					if(rnd.nextBoolean())
					{
						world[(x - 1) + (y - 1) * WORLD_WIDTH].setTexture(GOLD);
					}
					if(rnd.nextBoolean())
					{
						world[(x + 1) + y * WORLD_WIDTH].setTexture(GOLD);
					}
					if(rnd.nextBoolean())
					{
						world[(x - 1) + y * WORLD_WIDTH].setTexture(GOLD);
					}
					if(rnd.nextBoolean())
					{
						world[(x + 1) + (y - 1) * WORLD_WIDTH].setTexture(GOLD);
					}
					if(rnd.nextBoolean())
					{
						world[(x - 1) + (y + 1) * WORLD_WIDTH].setTexture(GOLD);
					}
				}
			}
		}
	}
	
	private static boolean isNextToGold(int x, int y)
	{
		return (world[x + (y - 1) * WORLD_WIDTH].containsTexture(GOLD) ||
				  world[(x + 1) + y * WORLD_WIDTH].containsTexture(GOLD) ||
				  world[(x - 1) + y * WORLD_WIDTH].containsTexture(GOLD));
	}
	
	private static void placeCrystals()
	{
		for(int y = 1; y < WORLD_HEIGHT / 2 + 7; y++)
		{
			for(int x = 0; x < WORLD_WIDTH; x++)
			{
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(STONE) && (world[x + y * WORLD_WIDTH].containsTexture(AIR)) && rnd.nextInt(100) < 5)
				{
					world[x + y * WORLD_WIDTH].setTexture(CRYSTAL);
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
				if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && world[x + y * WORLD_WIDTH].containsTexture(AIR) && (!world[(x - 1) + (y + 1) * WORLD_WIDTH].containsTexture(WOOD) && !world[(x + 1) + (y + 1) * WORLD_WIDTH].containsTexture(WOOD)) && rnd.nextInt(100) < 15)
				{
					for(int ty = y; ty < y + rnd.nextInt(3) + 4; ty++)//tree trunk between 4 and 6 high
					{
						world[x + ty * WORLD_WIDTH].setTexture(WOOD);
					}
				}
				//vines
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(WOOD) && world[x + (y - 2) * WORLD_WIDTH].containsTexture(GRASS) && rnd.nextInt(2) == 0)
				{
					world[x + (y - 1) * WORLD_WIDTH].setTexture(VINE);
					if(rnd.nextInt(2) == 0)
					{
						world[x + y * WORLD_WIDTH].setTexture(VINE);
					}
				}
				//tree leafs
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(WOOD) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].containsTexture(LEAF2)))
				{
					createTreeLeafs(x, y);
				}
				//apples
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 35 && isUnderTree(x, y))
				{
					if(rnd.nextBoolean())
					{
						world[x + y * WORLD_WIDTH].setTexture(APPLE_GREEN);
					}
					else
					{
						world[x + y * WORLD_WIDTH].setTexture(APPLE_RED);
					}					
				}
				//flowers
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 8)
				{
					world[x + y * WORLD_WIDTH].setTexture(FLOWER);
				}
				//carrot
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && !isUnderTree(x, y) && rnd.nextInt(100) < 5)
				{
					world[x + y * WORLD_WIDTH].setTexture(CARROT);
				}
				//signs
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 1)
				{
					world[x + y * WORLD_WIDTH].setTexture(SIGN);
				}
				//potion
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 1)
				{
					if(rnd.nextBoolean())
					{
						world[x + y * WORLD_WIDTH].setTexture(POTION_BLUE);
					}
					else
					{
						world[x + y * WORLD_WIDTH].setTexture(POTION_RED);
					}
				}
				//chest
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && rnd.nextInt(100) < 1)
				{
					world[x + y * WORLD_WIDTH].setTexture(CHEST_CLOSED);
				}
				//thorny plant
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && !isUnderTree(x, y) && rnd.nextInt(100) < 5)
				{
					world[x + y * WORLD_WIDTH].setTexture(THORNY_PLANT);
				}
				//hemp
				else if(world[x + (y - 1) * WORLD_WIDTH].containsTexture(GRASS) && (world[x + y * WORLD_WIDTH].containsTexture(AIR) || world[x + y * WORLD_WIDTH].isBackground()) && !isUnderTree(x, y) && rnd.nextInt(100) < 5)
				{
					if(rnd.nextBoolean())
					{
						world[x + y * WORLD_WIDTH].setTexture(HEMP);
					}
					else
					{
						world[x + y * WORLD_WIDTH].setTexture(HEMP_BOTTOM);
						world[x + (y + 1) * WORLD_WIDTH].setTexture(HEMP_TOP);
					}
				}
			}
		}
	}
	
	private static boolean isUnderTree(int x, int y)
	{
		return (world[(x + 2) + y * WORLD_WIDTH].containsTexture(WOOD) ||
				  world[(x + 1) + y * WORLD_WIDTH].containsTexture(WOOD) ||
				  world[(x - 2) + y * WORLD_WIDTH].containsTexture(WOOD) ||
				  world[(x - 1) + y * WORLD_WIDTH].containsTexture(WOOD) ||
				  world[x + y * WORLD_WIDTH].containsTexture(WOOD));
	}
	
	private static void createTreeLeafs(int x, int y)
	{
		int leafsAbouveTrunk = 2;
		int leafsOnSide = 2;
		int leafsUnderTrunk = 2;
		int treeType = rnd.nextInt(2);
		BlockTexture leaf = LEAF2;
		if(treeType == 0)
		{
			leaf = LEAF1;
		}
		
		for(int ly = y - leafsUnderTrunk; ly < y + leafsAbouveTrunk; ly++)
		{
			if(ly == y + leafsAbouveTrunk - 2)
			{
				leafsOnSide--;
			}
			for(int lx = x - leafsOnSide; lx < x + leafsOnSide + 1; lx++)
			{
				try{
					if((world[lx + ly * WORLD_WIDTH].containsTexture(AIR) || world[lx + ly * WORLD_WIDTH].isBackground()) && !((ly == y + leafsAbouveTrunk - 1 || ly == y - leafsUnderTrunk) && (lx ==  x - leafsOnSide || lx == x + leafsOnSide)))
					{
						world[lx + ly * WORLD_WIDTH].setTexture(leaf);
					}
				}catch(Exception e){System.out.println("Leaf out of bounds");}	
				
			}
		}		
	}
}
