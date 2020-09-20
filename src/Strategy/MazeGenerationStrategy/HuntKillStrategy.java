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
public class HuntKillStrategy extends MazeGenerationStrategy
{

    public HuntKillStrategy()
    {
        super();
    }

    @Override
    public void algorithm(Grid model)
    {
        // Grid
        Tile[][] grid = model.getGrid();
        
        // Keeps track of current tile's neighbors
        List<Tile> neighbors = new ArrayList<>();
        // Keeps track of visited Tiles
        Set<Tile> visited = new HashSet<>();
        // Keeps track of current Tile
        Tile startTile = grid[0][0];
        this.painter.drawTile(startTile, null, null, Tile.Type.EMPTY, painterWait);
        
        visited.add(startTile);
        
        while(!isComplete(model, neighbors))
        {
            this.addNeighbors(model, startTile, neighbors);
            
            // Removes neighbors that have been visited
            for(Iterator<Tile> iter = neighbors.iterator(); iter.hasNext();)
                if(visited.contains(iter.next()))
                    iter.remove();
            
            // If there are no available neighbors, hunt for unvisited tile adjacent to a visited cell.
            if(neighbors.isEmpty())
            {
                if(hunt(model, startTile, visited))
                    continue;
                
                System.out.println("broke");
                break;
            }
            
            // Pick random neighbor from not visited neighbors
            Tile randomNeighbor = neighbors.get(this.getRandomInt(neighbors.size(), 0));
            this.painter.drawTile(randomNeighbor, null, null, Tile.Type.EMPTY, this.painterWait);
            
            // Remove walls in between
            this.removeWallBetween(grid, startTile, randomNeighbor);
            
            // Set picked neighbor as current tile for next 
            startTile = randomNeighbor;
            // Set tile as visited
            visited.add(randomNeighbor);
            
            //  This is logic for visualization
            this.painter.highlightTile(randomNeighbor, this.painterWait);
        }
    }
    
    /**
     * Iterates every tile in the grid to 'hunt' for an unvisited tile that is
     * adjacent to a visited tile. If found, open a wall between both and makes
     * the startTile the unvisited tile.
     * @param model
     * @param startTile
     * @param visited
     * @return true if found and open passage between two tiles, false if maze is complete (no unvisited tiles)
     */
    private boolean hunt(Grid model, Tile startTile, Set<Tile> visited)
    {
       
        return false;
    }
    
    /**
     * Checks if the maze has been carved by seeing if every tile is in visited.
     * If any tile is not yet in visited, returns false, thus the algorithm is not complete.
     * @param model grid
     * @param visited list that keeps track of visited tiles
     * @return boolean true if complete, false if not complete
     */
    private boolean isComplete(Grid model, List<Tile> visited)
    {
        for(Tile tile : model.getTiles())
        {
            if(!visited.contains(tile))
            {
                return false;
            }
        }
        
        return true;
    }
}
