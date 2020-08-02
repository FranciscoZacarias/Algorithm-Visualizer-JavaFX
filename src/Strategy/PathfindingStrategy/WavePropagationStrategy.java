/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javafx.print.Collation;

/**
 * Wave propagation pathfinding strategy. 
 * This pathfinding strategy does not support a weighted search!
 * @author frank
 */
public class WavePropagationStrategy extends PathfindingStrategy
{
    private final int DEFAULT_DISTANCE = 1;
    
    public WavePropagationStrategy()
    {
        super();
    }
    
    @Override
    protected int runPathfinder(Grid model, List<Tile> path)
    {
        HashMap<Tile, Integer> tileData = new HashMap<>();
        this.executeWavePropagation(model, tileData);
        
        this.buildPath(path, model, tileData);
        int cost = tileData.get(model.getTarget());
        
        this.statistics.setPathFound(true, cost);
        this.painter.drawPath(path, model);
        
        return cost;
    }
    
    private void executeWavePropagation(Grid model, HashMap<Tile, Integer> tileData)
    {
        // Keeps track of visited tiles
        Set<Tile> visited = new HashSet<>();
        // List of cells to be processed in the while iteration
        List<Tile> toProcess = new LinkedList<>();
        // List of unprocessed cells (collected neighbors of toProcess)
        List<Tile> unprocessed = new LinkedList<>();
        // Current distance from target node to current iterations
        int iteration = 0;
        
        unprocessed.add(model.getRoot());
        visited.add(model.getRoot());
        tileData.put(model.getRoot(), this.DEFAULT_DISTANCE);
        
        while(!unprocessed.isEmpty())
        {
            iteration++;
            // shifts all elements from unprocced to toProcess 
            toProcess.addAll(unprocessed);
            unprocessed.clear();
            
            // Each iterations processed all neighbors from toProcess
            for(Tile tile : toProcess)
            {
                List<Tile> neighbors = model.getTileNeighbors(tile);
                
                for(Tile elem : neighbors)
                {
                    if(elem == null) continue;
                    if(elem.isWall()) continue;
                    if(visited.contains(elem)) continue;
                    
                    unprocessed.add(elem);
                    visited.add(elem);
                    this.statistics.incrementVisited();
                    tileData.put(elem, this.DEFAULT_DISTANCE + iteration);
                    
                    this.painter.drawTile(elem, model.getTarget(), model.getRoot(), Tile.Type.HIGHLIGHT, 2);
                    this.painter.drawTile(elem, model.getTarget(), model.getRoot(), Tile.Type.VISITED, 4);
                }
            }
        }
    }
    
    private void buildPath(List<Tile> path, Grid model, HashMap<Tile, Integer> data)
    {
        Tile currentTile = model.getTarget();
         
        do{
            path.add(currentTile);
            Tile lowestCost = null;
            
            for(Tile tile : model.getTileNeighbors(currentTile))
            {
                if(tile == null) continue;
                if(tile.isWall()) continue;
                
                if(lowestCost == null)
                {
                    lowestCost = tile;
                    continue;
                }
                
                if(data.get(tile) < data.get(lowestCost))
                    lowestCost = tile;
            }
            currentTile = lowestCost;
        } while (currentTile != model.getRoot());
        
        Collections.reverse(path);
    }
}
