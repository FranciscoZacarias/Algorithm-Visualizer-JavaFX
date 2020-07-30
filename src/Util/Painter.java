/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import Model.Grid;
import Model.Tile;
import Strategy.PathfindingStrategy.PathfindingStrategy;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class that handles the coloring of any element in the grid.
 * All methods of this class run on an independent thread
 */
public final class Painter
{
    private static final Painter INSTANCE = new Painter();
    private final Executor executor;
    
    private Painter()
    {
        executor = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Returns the single instance of this class
     * @return 
     */
    public static Painter getInstance()
    {
        return INSTANCE;
    }
    
    /**
     * Draws a path from root to target, given a list of tiles
     * @param path
     * @param model
     */
    public void drawPath(List<Tile> path, Grid model) 
    {
        this.executor.execute(
        () ->
        {
            path.stream().filter((tile) -> !(tile == model.getTarget() || tile == model.getRoot())).map((tile) ->
            {
                tile.setAttributes(Tile.Type.PATH, tile.getWeight());
                return tile;                
            }).forEachOrdered((_item) ->
            {
                try
                {
                    Thread.sleep(20);
                }
                catch (InterruptedException ex)
                {
                    Logger.getLogger(Grid.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }
    
    /**
     * Returns a runnable object that colors a single Tile and sleeps for 'sleep' ms
     * @param tile Tile to color
     * @param target We pass this to avoid overriding it
     * @param root We pass this to avoid overriding it
     * @param color Color to paint the tile
     * @param sleep time to wait before next task in thread
     */
    public void drawTile(Tile tile, Tile target, Tile root, Tile.Type color, long sleep)
    {
        this.executor.execute(()->
        {
            if(tile != target && tile != root)
                tile.setAttributes(color, tile.getWeight());
           
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(PathfindingStrategy.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    /**
     * Clears the previously assigned visited and path tiles
     * @param model 
     */
    public void clearPath(Grid model)
    {
        this.executor.execute(()->
        {
            Tile tile;
            for(int y = 0; y < model.getYSize(); y++)
            {
                for(int x = 0; x < model.getXSize(); x++)
                {
                    tile = model.getGrid()[x][y]; 
                    if(tile.getType() == Tile.Type.PATH || tile.getType() == Tile.Type.VISITED)
                    {
                        tile.setAttributes(Tile.Type.EMPTY, tile.getWeight());
                    }
                }
            }
        });
    }
}
