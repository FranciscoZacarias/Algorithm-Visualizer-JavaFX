/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.HeuristicStrategy;

import Model.Tile;

/**
 * Standard Heuristic for a square grid.
 * 
 * @author frank
 */
public class ManhattanStrategy extends HeuristicStrategy
{

    public ManhattanStrategy()
    {
        super();
    }
    
    @Override
    public double computeHeuristic(Tile root, Tile target)
    {
        double D = 1.0;
        double dx = Math.abs(root.getX() - target.getX());
        double dy = Math.abs(root.getY() - target.getY());
        
        return D * (dx + dy);
    }
}
