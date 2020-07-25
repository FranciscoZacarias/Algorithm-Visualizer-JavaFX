/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 *
 * @author frank
 */
public class BacktrackingStrategy extends MazeGenerationStrategy
{

    public BacktrackingStrategy(){}
    
    /**
     * Adds to 'neighbors' the list of valid neighbors of given 'tile'
     * @param model [in] model containing the grid
     * @param tile [in] tile to get neighbors from
     * @param neighbors [out] empty list to fill with neighbors
     */
    private void addNeighbors(Grid model, Tile tile, List<Tile> neighbors)
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
     * Removes a WALL places between Tile a and Tile b
     * @param grid Tile[][]
     * @param a Tile 
     * @param b Tile
     */
    private void removeWallBetween(Tile[][] grid, Tile a, Tile b)
    {
        // Remove wall between currentTile and randomNeighbor
        if(a.getX() < b.getX())
        {
            grid[a.getX() + 1][a.getY()].setAttributes(Tile.Type.EMPTY, a.getDefaultWeight());
        }
        else if(a.getX() > b.getX())
        {
            grid[a.getX() - 1][a.getY()].setAttributes(Tile.Type.EMPTY, a.getDefaultWeight());
        }
        else if(a.getY() > b.getY())
        {
            grid[a.getX()][a.getY() - 1].setAttributes(Tile.Type.EMPTY, a.getDefaultWeight());
        }
        else if(a.getY() < b.getY())
        {
            grid[a.getX()][a.getY() + 1].setAttributes(Tile.Type.EMPTY, a.getDefaultWeight());
        }
    }
    
    @Override
    public void generate(Grid model)
    {
        // Clear grid first
        model.clearGrid();
        
        // Grid
        Tile[][] grid = model.getGrid();
        
        // Set default walls
        this.setDefaultWalls(grid, model.getXSize(), model.getYSize());
        
        // Sets new random
        this.getNewRandom();
        
        // This stack backtracks the maze
        Stack<Tile> stack = new Stack<>();
        // Keeps track of current tile's neighbors
        List<Tile> neighbors = new ArrayList<>();
        // Keeps track of visited Tiles
        Set<Tile> visited = new HashSet<>();
        // Keeps track of current Tile
        Tile currentTile = grid[0][0];
        
        stack.push(currentTile);
        visited.add(currentTile);
        
        while(!stack.isEmpty())
        {
            this.addNeighbors(model, currentTile, neighbors);
            
            // Removes neighbors that have been visited
            for(Iterator<Tile> iter = neighbors.iterator(); iter.hasNext();)
                if(visited.contains(iter.next()))
                    iter.remove();
            
            // If there are no available neighbors, backtrack.
            if(neighbors.isEmpty())
            {
                currentTile = stack.pop();
                continue;
            }
            
            // Pick random neighbor from not visited neighbors
            Tile randomNeighbor = neighbors.get(this.random.nextInt(neighbors.size()));
            
            // Remove walls in between
            this.removeWallBetween(grid, currentTile, randomNeighbor);
            
            // Set picked neighbor as current tile for next 
            currentTile = randomNeighbor;
            // Set tile as visited
            visited.add(randomNeighbor);
            // Push to stack
            stack.push(randomNeighbor);
            
        }
    }
}
