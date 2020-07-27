/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Strategy signature to apply a path finding algorithm
 * 
 * @author frank
 */
public abstract class PathfindingStrategy 
{
    protected final ExecutorService executor;
    public PathfindingStrategy()
    {
        executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }
    
    /**
     * Signature for a pathfinding algorithm
     * @param model [in] 
     * @param path [out] output parameter with the resulting shortest path
     * @return int cost of the found path
     */
    public abstract int algorithm(Grid model, List<Tile> path);
    
    /**
     * Draws a path from root to target, given a list of tiles
     * @param path
     * @param target
     * @param root
     */
    protected void drawPath(List<Tile> path, Tile target, Tile root) 
    {
        Thread t = new Thread(() ->
        {
            path.stream().filter((tile) -> !(tile == target || tile == root)).map((tile) ->
            {
                tile.setAttributes(Tile.Type.PATH, tile.getWeight());
                return tile;                
            }).forEachOrdered((_item) ->
            {
                try
                {
                    Thread.sleep(25);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }, "PathColor");
        
        t.start();
    }
    
    /**
     * Returns a runnable object that colors a single Tile and sleeps for 'sleep' ms
     * @param tile
     * @param color
     * @param sleep
     * @return 
     */
    protected Runnable colorTile(Tile tile, Tile.Type color, long sleep)
    {
        return () ->
        {
            tile.setAttributes(color, tile.getWeight());
            
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(PathfindingStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
    }
    
}
