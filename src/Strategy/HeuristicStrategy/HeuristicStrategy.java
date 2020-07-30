/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.HeuristicStrategy;

import Model.Tile;

/**
 * Signature for a heuristic to be used in the A* algorithm
 * 
 * @author frank
 */
public abstract class HeuristicStrategy
{
    //protected final boolean breakTies;
            
    public HeuristicStrategy()
    {
        
    }
    
    /**
     * Computes an heuristic for the A* algorithm
     * @param root
     * @param target
     * @return 
     */
    public abstract double computeHeuristic(Tile root, Tile target);
}
