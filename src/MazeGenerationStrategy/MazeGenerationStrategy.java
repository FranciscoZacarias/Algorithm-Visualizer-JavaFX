/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.Random;
/**
 *
 * @author frank
 */
public abstract class MazeGenerationStrategy
{
    Random random;
    
    public MazeGenerationStrategy(){}
    
    /**
     * Generates a random maze in 'model'. This algorithm handles the tile type changes
     * @param model Grid
     */
    public abstract void generate(Grid model);

    /**
     * All 'generate' algorithms must call this function once in the beginning.
     * This function will set all Tiles w/o x AND y coordinate not even to a WALL.
     * Because walls are as thick as the path, we will consider for the maze 'pathing'
     * only Tiles with even coordinates (I.e. x:2,y:4 x:0:y0)
     * The algorithm is responsible then for carving out the path through the walls
     * created
     * @param grid Tile[][]
     * @param x_size grid width
     * @param y_size grid height
     */
    protected void setDefaultWalls(Tile[][] grid, int x_size, int y_size)
    {
        Tile temp;
        for(int y = 0; y < y_size; y++)
        {
            for(int x = 0; x < x_size; x++)
            {
                temp = grid[x][y];
                if((y % 2 != 0) || (x % 2 != 0)) temp.setAttributes(Tile.Type.WALL, temp.getWeight());
            }
        }
    }
    
    /**
     * Generates a new random 
     */
    protected void getNewRandom()
    {
        this.random = new Random();
    }
}
