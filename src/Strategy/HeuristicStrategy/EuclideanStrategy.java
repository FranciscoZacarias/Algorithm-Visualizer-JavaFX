/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.HeuristicStrategy;

import Model.Tile;

/**
 * This heuristic is best used if you can move at any angle,
 * instead of just following grid directions
 * 
 * @author frank
 */
public class EuclideanStrategy extends HeuristicStrategy
{

    public EuclideanStrategy()
    {
        super();
    }
    
    @Override
    public double computeHeuristic(Tile root, Tile target)
    {
        double D = 1.0;
        double dx = Math.abs(root.getX() - target.getX());
        double dy = Math.abs(root.getY() - target.getY());
        
        return D * Math.sqrt(dx * dx + dy * dy);
    }
    
}
