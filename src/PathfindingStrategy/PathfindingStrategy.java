/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import Util.Painter;
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
    protected Painter painter;
    
    public PathfindingStrategy()
    {
        painter = Painter.getInstance();
    }
    
    /**
     * Signature for a pathfinding algorithm
     * @param model [in] 
     * @param path [out] output parameter with the resulting shortest path
     * @return int cost of the found path
     */
    public abstract int algorithm(Grid model, List<Tile> path);
}
