/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import Strategy.MazeGenerationStrategy.BacktrackingStrategy;
import Strategy.MazeGenerationStrategy.MazeGenerationStrategy;
import Model.Grid;
import Strategy.HeuristicStrategy.DiagonalStrategy;
import Strategy.HeuristicStrategy.EuclideanStrategy;
import Strategy.HeuristicStrategy.HeuristicStrategy;
import Strategy.HeuristicStrategy.ManhattanStrategy;
import Strategy.HeuristicStrategy.PythagorasStrategy;
import Strategy.PathfindingStrategy.AStarStrategy;
import Strategy.PathfindingStrategy.DijkstraStrategy;
import Strategy.PathfindingStrategy.PathfindingStrategy;

/**
 * Factory that returns a pathfinding strategy
 * @author frank
 */
public class StrategyFactory
{
    // Pathfinding Factory
    public static PathfindingStrategy getPathfindingStrategy(Grid.Algorithms algorithmStrategy, HeuristicStrategy heuristicStrategy)
    {
        switch(algorithmStrategy)
        {
            case Dijkstra:
                return new DijkstraStrategy();
            case AStar:
                return new AStarStrategy(false, heuristicStrategy);
            case AStarOptimal:
                return new AStarStrategy(true, heuristicStrategy);
            default:
                throw new IllegalArgumentException("Pathfinding algorithm not found!");
        }
    }
    
    // Heuristic for A* Factory
    public static HeuristicStrategy getHeuristicStrategy(AStarStrategy.Heuristic strategy)
    {
        switch(strategy)
        {
            case Pythagoras:
                return new PythagorasStrategy();
            case Manhattan:
                return new ManhattanStrategy();
            case Diagonal:
                return new DiagonalStrategy();
            case Eudclidean:
                return new EuclideanStrategy();
            default:
                throw new IllegalArgumentException("Heuristic strategy not found!");
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
