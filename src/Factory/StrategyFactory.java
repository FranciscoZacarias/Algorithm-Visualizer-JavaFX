/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import MazeGenerationStrategy.BacktrackingStrategy;
import MazeGenerationStrategy.MazeGenerationStrategy;
import Model.Grid;
import PathfindingStrategy.DijkstraStrategy;
import PathfindingStrategy.PathfindingStrategy;

/**
 * Factory that returns a pathfinding strategy
 * @author frank
 */
public class StrategyFactory
{
    // Pathfinding Factory
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
    
    // Maze generation Factory
    public static MazeGenerationStrategy getMazeGenStrategy(Grid.MazeGen strategy)
    {
        switch(strategy)
        {
            case Backtracking:
               return new BacktrackingStrategy();
            default:
                throw new IllegalArgumentException("Pathfinding algorithm not found!");
        }
    }
}
