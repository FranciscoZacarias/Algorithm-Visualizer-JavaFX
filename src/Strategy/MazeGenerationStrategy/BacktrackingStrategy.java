/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

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
    
    @Override
    public void algorithm(Grid model)
    {
        // Grid
        Tile[][] grid = model.getGrid();
        
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
            this.painter.drawTile(randomNeighbor, null, null, Tile.Type.EMPTY, this.painterWait);
            
            // Remove walls in between
            this.removeWallBetween(grid, currentTile, randomNeighbor);
            
            // Set picked neighbor as current tile for next 
            currentTile = randomNeighbor;
            // Set tile as visited
            visited.add(randomNeighbor);
            // Push to stack
            stack.push(randomNeighbor);
            
            //  This is logic for visualization
            this.highlightTile(randomNeighbor);
        }
    }
}