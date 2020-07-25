/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import PathfindingStrategy.PathfindingStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author frank
 */
public class Grid extends Observable implements Observer
{
    public static enum Algorithms{
        Dijkstra
    }
    
    // Dimensions
    private int x_size;
    private int y_size;
    
    // Grid
    private Tile[][] grid;
    Tile root, target;
    
    // What change will happen to the tile on click
    private Tile.Type clickType;
    
    public Grid()
    {
        this.root = null;
        this.target = null;
        this.clickType = Tile.Type.ROOT;
    }
    
    /**
     * Runs an algorithm specified as 'pathfindingStrategy' to pathfind from root to target
     * @param pathfindingStrategy
     * @return 
     * @throws java.lang.InterruptedException 
     */
    public boolean findShortestPath(PathfindingStrategy pathfindingStrategy) throws InterruptedException
    {
        if(root == null || target == null) return false;
        
        List<Tile> shortestPath = new ArrayList<>();
        int cost = pathfindingStrategy.algorithm(this, shortestPath);
        
        this.drawPath(shortestPath);
        
        return true;
    }
    
    /**
     * Draws a path from root to target, given a list of tiles
     */
    private void drawPath(List<Tile> path) throws InterruptedException
    {
        for(Tile tile : path)
        {
            if (tile == target) continue;
            tile.setAttributes(Tile.Type.PATH, tile.getWeight());
        }
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
     * Sets all Tiles in the grid to default (Empty, defaultWeight)
     */
    public void clearGrid()
    {
        this.root = null;
        this.target = null;
        
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                grid[x][y].clearTile();
            }
        }
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
     * TODO: Extract this method into a maze generating strategy
     * Generates a random maze using a recursive backtracker algorithm
     */
    public void generateRandomMaze()
    {
        this.clearGrid();
        
        // Set default walls
        Tile temp;
        for(int y = 0; y < this.y_size; y++)
        {
            for(int x = 0; x < this.x_size; x++)
            {
                temp = grid[x][y];
                if((y % 2 != 0) || (x % 2 != 0)) temp.setAttributes(Tile.Type.WALL, temp.getWeight());
            }
        }
        
        // This stack backtracks the maze
        Stack<Tile> stack = new Stack<>();
        // Keeps track of current tile's neighbors
        List<Tile> neighbors = new ArrayList<>();
        // Keeps track of visited Tiles
        Set<Tile> visited = new HashSet<>();
        // Keeps track of the path of the maze
        Set<Tile> path = new HashSet<>();
        // To pick a random neighbor
        Random random = new Random();
        // Keeps track of current Tile
        Tile currentTile = grid[0][0];
        
        stack.push(currentTile);
        visited.add(currentTile);
        
        
        while(!stack.isEmpty())
        {
            // TODO: Cleanup code...
            // add north, south, east, west tile of lowCostTile
            temp = this.getNorthTile(currentTile);
            if(temp != null)
                neighbors.add((temp.getY() % 2 != 0) ? grid[temp.getX()][temp.getY() - 1] : temp);
            
            temp = this.getSouthTile(currentTile);
            if(temp != null)
                neighbors.add((temp.getY() % 2 != 0) ? grid[temp.getX()][temp.getY() + 1] : temp);
            
            temp = this.getWestTile(currentTile);
            if(temp != null)
                neighbors.add((temp.getX() % 2 != 0) ? grid[temp.getX() - 1][temp.getY()] : temp);

            temp = this.getEastTile(currentTile);
            if(temp != null) 
                neighbors.add((temp.getX() % 2 != 0) ? grid[temp.getX() + 1][temp.getY()] : temp);

            
            
            if(!neighbors.isEmpty())
            {
                // Get random neighbor
                Tile randomNeighb = neighbors.get(random.nextInt(neighbors.size()));
                
                //Create path between current and neighbor
                
            }
            else
            {
                stack.pop();
            }
            
        }
        
        System.out.println(visited);
    }
    
    @Override
    public void update(Observable o, Object arg)
    {
        if(o instanceof Tile)
        {
            Tile tile = (Tile)o;
            
            switch(this.clickType)
            {
                // Tiles that  can only have one ocurrence throughout the grid
                case ROOT: case TARGET:
                    
                    // Clear old tiles
                    if(clickType == Tile.Type.ROOT) 
                    {
                        if(root != null) root.clearTile();
                        this.root = tile;
                    }
                    else if(clickType == Tile.Type.TARGET) 
                    {
                        if(target != null) target.clearTile();
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
    }
}
