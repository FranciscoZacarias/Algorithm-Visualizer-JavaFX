/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.HeuristicStrategy;

import Model.Tile;

/**
 * This heuristic is best used if the grid allows for diagonal movement
 * 
 * @author frank
 */
public class DiagonalStrategy extends HeuristicStrategy
{

    public DiagonalStrategy()
    {
        super();
    }
    
    @Override
    public double computeHeuristic(Tile root, Tile target)
    {
        /**
         * If D2 = 1.0, this is called the Chebyshev distance
         * If D2 = sqrt(2), this is called called Octile distance
         */
        double D1 = 1.0;
        double D2 = 1.0; 
        
        double dx = Math.abs(root.getX() - target.getX());
        double dy = Math.abs(root.getY() - target.getY());
        
        return D1 * (dx + dy) + (D2 - 2 * D1) * Math.min(dx, dy);
    }
    
}
