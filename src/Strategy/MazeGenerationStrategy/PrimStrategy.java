/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author frank
 */
public class PrimStrategy extends MazeGenerationStrategy
{

    public PrimStrategy()
    {
        super();
    }
    

    @Override
    public void algorithm(Grid model)
    {
        Tile[][] grid = model.getGrid();
        Tile currentTile = grid[0][0];
        List<Tile> neighbors = new ArrayList<>();
        
        List<Pair<Tile, Tile>> visited = new ArrayList<>();
        visited.add(new Pair(currentTile, null));
        List<Pair<Tile, Tile>> toVisit = new ArrayList<>();
        this.addNeighbors(model, currentTile, neighbors);
        
        // Key is tile, value is it's parent
        for(Tile tile: neighbors)
        {
            if(tile != null)
                toVisit.add(new Pair(tile, currentTile));
        }
        
        Tile current, parent;
        while(!toVisit.isEmpty())
        {
            // Grabs random tile from to visit tiles
            // For each pair: key is current tile, value is it's parent
            Pair randomTile = toVisit.get(this.getRandomInt(toVisit.size(), 0));
                    
            toVisit.remove(randomTile);
            visited.add(randomTile);
            
            current = (Tile) randomTile.getKey();
            parent = (Tile) randomTile.getValue();
            
            this.painter.drawTile(current, null, null, Tile.Type.EMPTY, painterWait);
            this.removeWallBetween(grid, current, parent);
            this.painter.drawTile(parent, null, null, Tile.Type.EMPTY, painterWait);
            
            this.addNeighbors(model, current, neighbors);
            for(Tile tile : neighbors)
            {
                if(!pairListContainsTile(toVisit, tile) && !pairListContainsTile(visited, tile) && tile != null)
                {
                    toVisit.add(new Pair(tile, current));
                }
            }
        }
    }
    
    /**
     * Checks if a Node (private inner class of this class) list contains a given tile
     * @param nodes
     * @param tile
     * @return 
     */
    private boolean pairListContainsTile(List<Pair<Tile, Tile>> nodes, Tile tile)
    {
        for(Pair pair : nodes)
        {
            if(pair.getKey() == tile)
                return true;
        }
        return false;
    }
}
