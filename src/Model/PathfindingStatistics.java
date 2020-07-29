/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Observable;


/**
 * Keeps track of statistics for the pathfinding algorithms
 * @author frank
 */
public class PathfindingStatistics extends Observable
{
    // Total tiles in grid
    private final int tilesTotal;
    // Tiles visited by the algorithm
    private int tilesVisited;
    // Weather the algorithm found a path between the nodes
    private boolean pathFound;
    // Total cost of found path
    private int pathCost;
    // Time it took for the algorithm to run
    private long elapsedTime;
    
    public PathfindingStatistics(Grid model)
    {
        this.addObserver(model);
        
        this.tilesTotal = model.getTiles().size();
        this.tilesVisited = 0;
        this.pathFound = false;
        this.pathCost = -1;
        this.elapsedTime = 0;
    }
    
    /**
     * Increments one into statistics
     */
    public void incrementVisited()
    {
        this.tilesVisited++;
    }
    
    /**
     * Sets a boolean value to pathFound, which represents if any path was found between two nodes
     * @param pathFound boolean value
     * @param pathCost cost of path (if pathFound is false, this will be -1 regardless of parameter)
     */
    public void setPathFound(boolean pathFound, int pathCost)
    {
        this.pathFound = pathFound;
        this.pathCost = (pathFound) ? pathCost : -1;
    }
    
    /**
     * Sets the elapsed time
     * @param elapsedTime long
     */
    public void setElapsedTime(long elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }
    
    /**
     * Returns total tiles
     * @return int
     */
    public int getTilesTotal()
    {
        return tilesTotal;
    }

    /**
     * Returns amount of tiles visited by the algorithm
     * @return int
     */
    public int getTilesVisited()
    {
        return tilesVisited;
    }
    
    /**
     * Returns if the path was found
     * @return boolean
     */
    public boolean isPathFound()
    {
        return pathFound;
    }

    /**
     * Gets the cost of the path found
     * @return int
     */
    public int getPathCost()
    {
        return pathCost;
    }

    /**
     * Returns elapsed time in nanoseconds.
     * To convert to seconds: 1 nanosecond = 1.0 * 10 ^ -9 seconds
     * To convert to milliseconds: 1 nanosecond = 1.0 * 10 ^ -6 seconds
     * @return elapsed time in nanoseconds
     */
    public double getElapsedTime()
    {
        return elapsedTime;
    }
    
    /**
     * Calls setChanged and notifyObservers to remotely update any observers observing these statistics
     */
    public void updateObservers()
    {
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString()
    {
        String str = String.format("Pathfinding Statistics{ TotalTiles = %d, VisitedTiles = %d, PathFound = %b, PathCost = %d, ElapsedTime = %.4f milliseconds }" , 
                this.getTilesTotal(), this.getTilesVisited(), this.isPathFound(), (this.isPathFound()) ? this.getPathCost() : -1, this.getElapsedTime() * Math.pow(10, -6));
        
        return str;
    }
}
