/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PathfindingStrategy;

import Model.Grid;

/**
 * Factory that returns a pathfinding strategy
 * @author frank
 */
public class StrategyFactory
{
    public static PathfindingStrategy getPathfindingStrategy(Grid.Algorithms strategy)
    {
        switch(strategy)
        {
            case Dijkstra:
                return new DijkstraStrategy();
            default:
                throw new IllegalArgumentException("Pathfinding algorithm not found!");
        }
    }
}
