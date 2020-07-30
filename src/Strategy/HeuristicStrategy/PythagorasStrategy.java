/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.HeuristicStrategy;

import Model.Tile;

/**
 *
 * @author frank
 */
public class PythagorasStrategy extends HeuristicStrategy
{
    public PythagorasStrategy()
    {
        super();
    }
    
    @Override
    public double computeHeuristic(Tile root, Tile target)
    {
        return Math.sqrt((root.getX() - target.getX()) * (root.getX() - target.getX()) + (root.getY() - target.getY()) * (root.getY() - target.getY()));
    }
}
