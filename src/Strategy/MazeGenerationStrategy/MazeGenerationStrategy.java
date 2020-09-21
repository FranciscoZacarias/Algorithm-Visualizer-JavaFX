/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import Util.Painter;
import java.util.List;
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
        Backtracker,
        Kruskal,
        Prim,
        Sidewinder
    }
    
    public MazeGenerationStrategy()
    {
        this.painter = Painter.getInstance();
        this.painterWait = 4; //4
    }
    
    /**
     * Generates a random maze in 'model'. This algorithm handles the tile type changes
     * @param model Grid
     */
    public final void generate(Grid model)
    {
        // template
        model.clearGrid();
        this.setDefaultWalls(model.getGrid(), model.getXSize(), model.getYSize());
        
        // maze gen algorithm
        this.algorithm(model);
    }

    
    public abstract void algorithm(Grid model);
    
    /**
     * Adds to 'neighbors' the list of valid neighbors of given 'tile'
     * @param model [in] model containing the grid
     * @param tile [in] tile to get neighbors from
     * @param neighbors [out] empty list to fill with neighbors
     */
    protected void addNeighbors(Grid model, Tile tile, List<Tile> neighbors)
    {
        // Clear neighbors to make sure we work with an empty list
        neighbors.clear();
        // Temporary holder for each neighbor
        Tile temp;
        
        temp = model.getNorthTile(tile);
        if(temp != null)
            neighbors.add((temp.getY() % 2 != 0) ? model.getGrid()[temp.getX()][temp.getY() - 1] : temp);

        temp = model.getSouthTile(tile);
        if(temp != null)
            neighbors.add((temp.getY() % 2 != 0) ? model.getGrid()[temp.getX()][temp.getY() + 1] : temp);

        temp = model.getWestTile(tile);
        if(temp != null)
            neighbors.add((temp.getX() % 2 != 0) ? model.getGrid()[temp.getX() - 1][temp.getY()] : temp);

        temp = model.getEastTile(tile);
        if(temp != null) 
            neighbors.add((temp.getX() % 2 != 0) ? model.getGrid()[temp.getX() + 1][temp.getY()] : temp);
    }
    
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
        for(int y = 0; y < y_size; y++)
        {
            for(int x = 0; x < x_size; x++)
            {
                grid[x][y].setAttributes(Tile.Type.WALL, grid[x][y].getDefaultWeight());
            }
        }
    }
    
    /**
     * Removes a WALL placed between Tile a and Tile b
     * @param grid Tile[][]
     * @param a Tile 
     * @param b Tile
     */
    protected void removeWallBetween(Tile[][] grid, Tile a, Tile b)
    {
        int x = a.getX();
        int y = a.getY();
        
        // Remove wall between currentTile and randomNeighbor
        if     (a.getX() < b.getX()) x += 1;
        else if(a.getY() < b.getY()) y += 1;
        else if(a.getX() > b.getX()) x -= 1;
        else if(a.getY() > b.getY()) y -= 1;
        
        // This logic is for visualization
        this.painter.highlightTile(grid[x][y], this.painterWait);
    }
    
    /**
     * Generates a new random between min (as minimum value) and max (as maximum value)
     * @param max value
     * @param min value
     * @return (pseudo)random number generated 
     */
    protected int getRandomInt(int max, int min)
    {
        this.random = new Random();
        return ((int) (Math.random()*(max - min))) + min; 
    }
}
