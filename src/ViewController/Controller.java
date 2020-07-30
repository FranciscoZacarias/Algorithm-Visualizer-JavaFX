/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.Grid;
import Model.Tile;
import Strategy.PathfindingStrategy.PathfindingStrategy;
import Factory.StrategyFactory;
import Strategy.HeuristicStrategy.HeuristicStrategy;
import Strategy.MazeGenerationStrategy.MazeGenerationStrategy;
import Strategy.PathfindingStrategy.AStarStrategy;

/**
 *
 * @author frank
 */
public class Controller
{
    private final Grid model;
    private final View view;
    
    public Controller(Grid model, View view)
    {
        this.model = model;
        this.view = view;
        this.view.setTriggers(this);
        this.view.createGrid();
        
        this.model.addObserver(view);
    }
    
    public void doClearGrid()
    {
        this.model.clearGrid();
    }
    
    public void doChangeClickType(Tile.Type type)
    {   
        this.model.changeClickType(type);
    }
    
    public void doAddRandomWeights()
    {
        this.model.addRandomWeights();
    }
    
    public void doAddRandomWalls()
    {
        this.model.addRandomWalls();
    }
    
    public void doGenerateMaze(Grid.MazeGen strategy)
    {
        MazeGenerationStrategy mazeGenerationStrategy = StrategyFactory.getMazeGenStrategy(strategy);
        this.model.generateRandomMaze(mazeGenerationStrategy);
    }
    
    public boolean doShortestPathAlgorithm(Grid.Algorithms algorithm, AStarStrategy.Heuristic heuristic) throws InterruptedException
    {
        HeuristicStrategy heuristicStrategy = StrategyFactory.getHeuristicStrategy(heuristic);
        PathfindingStrategy pathfindingStrategy = StrategyFactory.getPathfindingStrategy(algorithm, heuristicStrategy);
        return this.model.executePathfinding(pathfindingStrategy);
    }
}
