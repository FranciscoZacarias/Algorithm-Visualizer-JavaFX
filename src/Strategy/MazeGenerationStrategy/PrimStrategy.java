/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
        
        List<Tile> visited = new ArrayList<>();
        visited.add(currentTile);
        List<Node> toVisit = new ArrayList<>();
        this.addNeighbors(model, currentTile, neighbors);
        
        // Key is tile, value is it's parent
        for(Tile tile: neighbors)
        {
            if(tile != null)
                toVisit.add(new Node(tile, currentTile));
        }
        
        while(!toVisit.isEmpty())
        {
            // Grabs random tile from to visit tiles
            Node randomTile = toVisit.get(this.getRandomInt(toVisit.size(), 0));
                    
            toVisit.remove(randomTile);
            visited.add(randomTile.getTile());
            
            this.painter.drawTile(randomTile.getTile(), null, null, Tile.Type.EMPTY, painterWait);
            this.removeWallBetween(grid, randomTile.getTile(), randomTile.getParentTile());
            this.painter.drawTile(randomTile.getParentTile(), null, null, Tile.Type.EMPTY, painterWait);
            
            this.addNeighbors(model, randomTile.getTile(), neighbors);
            for(Tile tile : neighbors)
            {
                if(!nodeListContainsTile(toVisit, tile) && !visited.contains(tile) && tile != null)
                {
                    toVisit.add(new Node(tile, randomTile.getTile()));
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
    private boolean nodeListContainsTile(List<Node> nodes, Tile tile)
    {
        for(Node node : nodes)
        {
            if(node.getTile() == tile)
                return true;
        }
        return false;
    }
    
    private class Node
    {
        private final Tile tile;
        private final Tile parentTile;

        public Node(Tile tile, Tile parentTile)
        {
            this.tile = tile;
            this.parentTile = parentTile;
        }

        public Tile getTile()
        {
            return tile;
        }

        public Tile getParentTile()
        {
            return parentTile;
        }
    }
}
