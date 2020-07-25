/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ViewController;

import Model.Grid;
import Model.Tile;
import PathfindingStrategy.PathfindingStrategy;
import PathfindingStrategy.StrategyFactory;

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
    
    public void doGenerateMaze()
    {
        this.model.generateRandomMaze();
    }
    
    public boolean doShortestPathAlgorithm(Grid.Algorithms strategy) throws InterruptedException
    {
        PathfindingStrategy pathfindingStrategy = StrategyFactory.getPathfindingStrategy(strategy);
        return this.model.findShortestPath(pathfindingStrategy);
    }
}
