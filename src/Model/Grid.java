/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Strategy.MazeGenerationStrategy.MazeGenerationStrategy;
import Strategy.PathfindingStrategy.PathfindingStrategy;
import Util.Painter;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 *
 * @author frank
 */
public class Grid extends Observable implements Observer
{    
    // Dimensions
    private int x_size;
    private int y_size;
    
    // Grid
    private Tile[][] grid;
    Tile root, target;
    
    // What change will happen to the tile on click
    private Tile.Type clickType;
    
    // Attributes
    private final Painter painter;
    
    public Grid()
    {
        this.root = null;
        this.target = null;
        this.clickType = Tile.Type.ROOT;
        painter = Painter.getInstance();
    }
    
    /**
     * Runs an algorithm specified as 'pathfindingStrategy' to pathfind from root to target
     * @param pathfindingStrategy
     * @return 
     * @throws java.lang.InterruptedException 
     */
    public boolean executePathfinding(PathfindingStrategy pathfindingStrategy) throws InterruptedException
    {
        if(root == null || target == null) return false;
        this.painter.clearPath(this);
        
        List<Tile> path = new ArrayList<>();
        
        pathfindingStrategy.algorithm(this, path);
        
        return true;
    }
    
    /**
     * Initializes the grid with all it's tiles in an empty state.
     * This method is only meant to be called once before runtime.
     * @param x_tiles
     * @param y_tiles
     * @param tile_size 
     */
    public void gridInit(int x_tiles, int y_tiles, int tile_size)
    {
        this.x_size = x_tiles;
        this.y_size = y_tiles;
        this.grid = new Tile[x_tiles][y_tiles];
        
        // Initializes all tiles
        for(int y = 0; y < y_tiles; y++)
        {
            for(int x = 0; x < x_tiles; x++)
            {
                Tile tile = new Tile(x, y, tile_size); 
                tile.addObserver(this);
                grid[x][y] = tile;
            }
        }
    }
    
    /**
     * Returns the Tile 2Dimensional vector, representing the grid
     * @return Tile[][] grid
     */
    public Tile[][] getGrid()
    {
        return this.grid;
    }
    
    /**
     * Checks if the grid is ready to run. (E.g. If a root and target node were selected)
     * @return true if grid is ready to run an algorithm, false if root and target are not set
     */
    public boolean isReady()
    {
        return !(root == null || target == null);
    }
    
    /**
     * Returns a list with all the tiles in this grid
     * @return all tiles
     */
    public List<Tile> getTiles()
    {
        List<Tile> tiles = new ArrayList<>();
        
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                tiles.add(grid[x][y]);
            }
        }
        
        return tiles;
    }
    
    /**
     * Sets all Tiles in the grid to default (Empty, defaultWeight)!
     */
    public void clearGrid()
    {
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                grid[x][y].clearTile();
            }
        }
    }
    
    /**
     * Returns total amount of walls in the grid
     * @return 
     */
    public int getWallsAmount()
    {
        int totalWalls = 0;
        
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                if(grid[x][y].isWall()) totalWalls++;
            }
        }
        
        return totalWalls;
    }
    
    /**
     * Return neighbors of 'tile'
     * @param tile
     * @return 
     */
    public List<Tile> getTileNeighbors(Tile tile)
    {
        List<Tile> neighbors = new ArrayList<>();
        
        neighbors.add(this.getNorthTile(tile));
        neighbors.add(this.getSouthTile(tile));
        neighbors.add(this.getEastTile(tile));
        neighbors.add(this.getWestTile(tile));
        
        return neighbors;
    }
    
    /**
     * Returns tile to the north of 'tile'
     * @param tile north tile
     * @return Tile
     */
    public Tile getNorthTile(Tile tile)
    {
        return (tile.getY() - 1 >= 0) ? grid[tile.getX()][tile.getY() - 1] : null;
    }
    
    /**
     * Returns tile to the south of 'tile'
     * @param tile south tile
     * @return Tile
     */
    public Tile getSouthTile(Tile tile)
    {
        return (tile.getY() + 1 <= y_size - 1) ? grid[tile.getX()][tile.getY() + 1] : null;
    }
    
    /**
     * Returns tile to the west (left) of 'tile'
     * @param tile west tile
     * @return Tile
     */
    public Tile getWestTile(Tile tile)
    {
        return (tile.getX() - 1 >= 0) ? grid[tile.getX() - 1][tile.getY()] : null;
    }
    
    /**
     * Returns tile to the East (right) of 'tile'
     * @param tile east tile
     * @return Tile
     */
    public Tile getEastTile(Tile tile)
    {
        return (tile.getX() + 1  <= x_size - 1) ? grid[tile.getX() + 1][tile.getY()] : null;
    }
    
    /**
     * Returns true jf 'tile' is directly north to 'compare' (same x coord)
     * @param tile
     * @param compare
     * @return 
     */
    public boolean isOnNorth(Tile tile, Tile compare)
    {
        if(tile.getX() != compare.getX()) 
            return false;
        
        return tile.getY() < compare.getY();
    }
    
    /**
     * Returns true jf 'tile' is directly south to 'compare' (same x coord)
     * @param tile
     * @param compare
     * @return 
     */
    public boolean isOnSouth(Tile tile, Tile compare)
    {
        if(tile.getX() != compare.getX()) 
            return false;
        
        return tile.getY() > compare.getY();
    }
    
    /**
     * Returns true jf 'tile' is directly west to 'compare' (same y coord)
     * @param tile
     * @param compare
     * @return 
     */
    public boolean isOnWest(Tile tile, Tile compare)
    {
        if(tile.getY() != compare.getY()) 
            return false;
        
        return tile.getX() < compare.getX();
    }
    
    /**
     * Returns true jf 'tile' is directly north to 'compare' (same y coord)
     * @param tile
     * @param compare
     * @return 
     */
    public boolean isOnEast(Tile tile, Tile compare)
    {
        if(tile.getY() != compare.getY()) 
            return false;
        
        return tile.getX() > compare.getX();
    }
    
    /**
     * Returns height of maze
     * @return int
     */
    public int getYSize()
    {
        return this.y_size;
    }
    
    /**
     * Returns width of maze
     * @return int
     */
    public int getXSize()
    {
        return this.x_size;
    }
    
    /**
     * Changes the current click type
     * @param type 
     */
    public void changeClickType(Tile.Type type)
    {
        this.clickType = type;
    }
    
    /**
     * Returns start Tile
     * @return Tile
     */
    public Tile getRoot()
    {
        return this.root;
    }
    
    /**
     * Returns target tile
     * @return Tile
     */
    public Tile getTarget()
    {
        return this.target;
    }
    
    /**
     * Adds random weights to all tiles in the grid
     * This makes the pathfinding less linear than if all were weighted the same
     */
    public void addRandomWeights()
    {
        this.clearGrid();
        
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                grid[x][y].randomizeWeight();
            }
        }
    }
    
    /**
     * resets previous walls and adds new random walls to some tiles in the grid
     */
    public void addRandomWalls()
    {
        //this.clearGrid();
        Random random = new Random();
        Tile tile;
        
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                tile = grid[x][y];
                if(tile.getType() == Tile.Type.WALL)
                    tile.setAttributes(Tile.Type.EMPTY, tile.getWeight());
                
                if((random.nextInt(2 - 0 + 1) + 0) == 1)
                    tile.setAttributes(Tile.Type.WALL, tile.getWeight());
            }
        }
    }
    
    /**
     * TODO: Extract this method into a maze generating strategy
     * Generates a random maze using a recursive backtracker algorithm
     * @param mazeGenerationStrategy maze generation algorithm
     */
    public void generateRandomMaze(MazeGenerationStrategy mazeGenerationStrategy)
    {
        mazeGenerationStrategy.generate(this);
    }
    
    @Override
    public void update(Observable o, Object arg)
    {
        // If the update came from a Tile Object
        if(o instanceof Tile)
        {
            Tile tile = (Tile)o;
            
            // User can't override a wall, unless he's setting it as empty first.
            if(tile.isWall())
            {
                if(this.clickType == Tile.Type.EMPTY)
                    tile.setAttributes(clickType, tile.getDefaultWeight());
                return;
            }
            
            // What happens when you click a tile with a specific clickType (I.e. root, target, wall...)
            switch(this.clickType)
            {
                // Tiles that  can only have one ocurrence throughout the grid
                case ROOT: case TARGET:

                    // Clear old tiles
                    if(clickType == Tile.Type.ROOT) 
                    {
                        if(this.root != null) root.clearTile();
                        this.root = tile;
                    }
                    else if(clickType == Tile.Type.TARGET) 
                    {
                        if(this.target != null) target.clearTile();
                        this.target = tile;
                    }
                    
                    tile.setAttributes(clickType, tile.getDefaultWeight());
                    
                    break;
                // Tiles that allow multiple ocurrences of themselfs
                default:
                    tile.setAttributes(clickType, tile.getWeight());
                    break;
            }
        }
        
        // If the update came from statistics
        if(o instanceof PathfindingStatistics)
        {
            PathfindingStatistics stats = (PathfindingStatistics)o;
            setChanged();
            notifyObservers(stats);
        }
    }
}
