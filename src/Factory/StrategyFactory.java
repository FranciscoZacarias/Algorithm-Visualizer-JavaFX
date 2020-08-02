/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Factory;

import Strategy.MazeGenerationStrategy.BacktrackingStrategy;
import Strategy.MazeGenerationStrategy.MazeGenerationStrategy;
import Strategy.HeuristicStrategy.DiagonalStrategy;
import Strategy.HeuristicStrategy.EuclideanStrategy;
import Strategy.HeuristicStrategy.HeuristicStrategy;
import Strategy.HeuristicStrategy.ManhattanStrategy;
import Strategy.HeuristicStrategy.PythagorasStrategy;
import Strategy.MazeGenerationStrategy.KruskalStrategy;
import Strategy.PathfindingStrategy.AStarStrategy;
import Strategy.PathfindingStrategy.DijkstraStrategy;
import Strategy.PathfindingStrategy.PathfindingStrategy;
import Strategy.PathfindingStrategy.WavePropagationStrategy;

/**
 * Factory that returns a pathfinding strategy
 * @author frank
 */
public class StrategyFactory
{
    // Pathfinding Factory
    public static PathfindingStrategy getPathfindingStrategy(PathfindingStrategy.Algorithms algorithmStrategy, HeuristicStrategy heuristicStrategy)
    {
        switch(algorithmStrategy)
        {
            case Dijkstra:
                return new DijkstraStrategy();
            case AStar:
                return new AStarStrategy(false, heuristicStrategy);
            case AStarOptimal:
                return new AStarStrategy(true, heuristicStrategy);
            case WavePropagation:
                return new WavePropagationStrategy();
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
    public static MazeGenerationStrategy getMazeGenStrategy(MazeGenerationStrategy.MazeGen strategy)
    {
        switch(strategy)
        {
            case Backtracker:
               return new BacktrackingStrategy();
            case Kruskal:
                return new KruskalStrategy();
            default:
                throw new IllegalArgumentException("Pathfinding algorithm not found!");
        }
    }
}
