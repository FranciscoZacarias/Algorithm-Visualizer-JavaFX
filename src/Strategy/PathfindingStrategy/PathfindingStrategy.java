/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.PathfindingStrategy;

import Model.Grid;
import Model.PathfindingStatistics;
import Model.Tile;
import Util.Painter;
import java.util.List;

/**
 * Strategy signature to apply a path finding algorithm
 * 
 * @author frank
 */
public abstract class PathfindingStrategy 
{
    protected Painter painter;
    protected PathfindingStatistics statistics;
    
    // Pathfinding algorithms
    public static enum Algorithms{
        Dijkstra,
        AStar,
        AStarOptimal,
        WavePropagation
    }
    
    public PathfindingStrategy()
    {
        this.painter = Painter.getInstance();
    }
    
    /**
     * Template method for the pathfinding strategy
     * @param model [in] 
     * @param path [out] output parameter with the resulting shortest path
     * @return int cost of the found path 
     */
    public final int algorithm(Grid model, List<Tile> path)
    {
        // (...) Statistics
        long start = System.nanoTime();
        this.statistics = new PathfindingStatistics(model);
        this.statistics.setWallSize(model.getWallsAmount());
        
        // Runs pathfinding algorithm
        int cost = this.runPathfinder(model, path);

        // (...) Statistics
        long end = System.nanoTime();
        this.statistics.setElapsedTime(end - start);
        this.statistics.updateObservers();
        this.painter.drawPath(path, model);
        
        System.out.println(this.statistics);
        
        return cost;
    }
    
    /**
     * Pathfinding algorithm 
     * @param model Grid containing a root Tile and a target Tile
     * @param path path found between tiles
     * @return cost of the path found
     */
    protected abstract int runPathfinder(Grid model, List<Tile> path);
}