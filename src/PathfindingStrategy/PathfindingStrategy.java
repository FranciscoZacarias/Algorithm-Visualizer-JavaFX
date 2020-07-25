/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;
import Model.Tile;
import java.util.List;

/**
 * Strategy signature to apply a path finding algorithm
 * 
 * @author frank
 */
public abstract class PathfindingStrategy 
{
    
    public PathfindingStrategy(){}
    
    /**
     * Signature for a pathfinding algorithm
     * @param model [in] 
     * @param path [out] output parameter with the resulting shortest path
     * @return 
     */
    public abstract int algorithm(Grid model, List<Tile> path);
}
