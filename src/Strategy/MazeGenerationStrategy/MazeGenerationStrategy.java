/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import Util.Painter;
import java.util.Random;
/**
 *
 * @author frank
 */
public abstract class MazeGenerationStrategy
{
    protected Random random;
    protected Painter painter;
    protected long painterWait;
    
    // Maze generation algorithms
    public static enum MazeGen{
        Backtracking,
        Eller
    }
    
    public MazeGenerationStrategy()
    {
        this.painter = Painter.getInstance();
        this.painterWait = 4;
    }
    
    /**
     * Generates a random maze in 'model'. This algorithm handles the tile type changes
     * @param model Grid
     */
    public final void generate(Grid model)
    {
        model.clearGrid();
        this.setDefaultWalls(model.getGrid(), model.getXSize(), model.getYSize());
        
        this.algorithm(model);
    }

    
    public abstract void algorithm(Grid model);
    
    /**
     * All 'generate' algorithms must call this function once in the beginning.
     * This function will set any not even Tiles to a WALL.
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
                if((y % 2 != 0) || (x % 2 != 0)) 
                {
                    this.painter.drawTile(temp, null, null, Tile.Type.WALL, 1);
                }
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
