/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 *
 * @author frank
 */
public class DijkstraStrategy extends PathfindingStrategy
{
    public DijkstraStrategy()
    {
        super();
    }

    @Override
    public int runPathfinder(Grid grid, List<Tile> path)
    {
        Tile root = grid.getRoot();
        Tile target = grid.getTarget();
        
        HashMap<Tile, Tile> parents = new HashMap();
        HashMap<Tile, Integer> weights = new HashMap();
        path.clear();
        
        executeDijkstra(grid, parents, weights);
        
        int cost = weights.get(target);
        
        Tile tile = target;
        
        // If cost is infinite, means it didn't find the target
        if(cost != Integer.MAX_VALUE)
        {
            do{
                path.add(0, tile);
                tile = parents.get(tile);
            } while (tile != root);
            
            this.statistics.setPathFound(true, cost);
            painter.drawPath(path, grid);
        }
        
        return cost;
    }
    
    /**
     * Executes Dijkstra's algorithm on the 'grid'
     * @param grid Model with grid consisting of Tile[][]
     * @param parents empty map to store parents of tiles, to allow path reconstruction
     * @param weights empty map to keep track of weights
     */
    private void executeDijkstra(Grid grid,
                    HashMap<Tile, Tile> parents,
                    HashMap<Tile, Integer> weights)
    {
        Tile root = grid.getRoot();
        
        // Init all tiles
        Set<Tile> unvisited = new HashSet();
        
        grid.getTiles().stream().filter((tile) -> !(tile.isWall())).map((tile) ->
        {
            unvisited.add(tile);
            return tile;
        }).map((tile) ->
        {
            weights.put(tile, Integer.MAX_VALUE);
            return tile;
        }).forEachOrdered((tile) ->
        {
            parents.put(tile, null);
        });
        weights.put(root, 0);
        
        // Compute weights
        while(!unvisited.isEmpty())
        {
            Tile lowCostTile = getMinWeight(unvisited, weights);
            
            // If we ever get a lower cost that equals infinity, it means we're stuck
            if(weights.get(lowCostTile) == Integer.MAX_VALUE)
                break;
            
            // Paints current tile and updates statistics for visited tiles
            painter.drawTile(lowCostTile, grid.getTarget(), root, Tile.Type.HIGHLIGHT, 1);
            this.statistics.incrementVisited();
            
            // Remove current tile from unvisited set
            unvisited.remove(lowCostTile);
            
            // Get neighbors
            List<Tile> neighbors = grid.getTileNeighbors(lowCostTile);
            
            for(Tile tile : neighbors)
            {
                if(unvisited.contains(tile))
                {
                    int weight = tile.getWeight() + weights.get(lowCostTile);
                    if(weights.get(tile) > weight)
                    {
                        weights.put(tile, weight);
                        parents.put(tile, lowCostTile);
                    }
                }
            }
            painter.drawTile(lowCostTile, grid.getTarget(), root, Tile.Type.VISITED, 1);
        }
    }
    
    /**
     * Return the min weight Tile in  the unvisited set
     * @param unvisited
     * @param weights
     * @return 
     */
    private Tile getMinWeight(Set<Tile> unvisited, HashMap<Tile, Integer> weights)
    {
        double minWeight = Integer.MAX_VALUE;
        Tile minWeightTile = null;
        
        for(Tile tile : unvisited)
        {
            if(weights.get(tile) <= minWeight)
            {
                minWeightTile = tile;
                minWeight = weights.get(tile);
            }
        }
        
        return minWeightTile;
    }
}