/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
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
    }

    @Override
    public int algorithm(Grid grid, List<Tile> path)
    {
        Tile root = grid.getRoot();
        Tile target = grid.getTarget();
        
        HashMap<Tile, Tile> parents = new HashMap();
        HashMap<Tile, Integer> weights = new HashMap();
        path.clear();
        
        executeDijkstra(grid, parents, weights);
        
        int cost = weights.get(target);
        
        Tile tile = target;
        do {
            path.add(0, tile);
            tile = parents.get(tile);
        } while (tile != root);
        
        return cost;
    }
    
    private void executeDijkstra(Grid grid,
                    HashMap<Tile, Tile> parents,
                    HashMap<Tile, Integer> weights)
    {
        Tile root = grid.getRoot();
        
        // Init all tiles
        Set<Tile> unvisited = new HashSet();
        for (Tile tile : grid.getTiles())
        {
            if(tile.isWall()) continue;
            unvisited.add(tile);
            weights.put(tile, Integer.MAX_VALUE);
            parents.put(tile, null);
        }
        weights.put(root, 0);
        
        // Compute weights
        while(!unvisited.isEmpty())
        {
            Tile lowCostTile = getMinWeight(unvisited, weights);
            unvisited.remove(lowCostTile);
            
            List<Tile> neighbors = new ArrayList<>();
            Tile temp;
            
            // add north, south, east, west tile of lowCostTile to neighbors, if they exist
            temp = grid.getNorthTile(lowCostTile);
            if(temp != null) neighbors.add(temp);
            temp = grid.getSouthTile(lowCostTile);
            if(temp != null) neighbors.add(temp);
            temp = grid.getEastTile(lowCostTile);
            if(temp != null) neighbors.add(temp);
            temp = grid.getWestTile(lowCostTile);
            if(temp != null) neighbors.add(temp);
            
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
        }
    }
    
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