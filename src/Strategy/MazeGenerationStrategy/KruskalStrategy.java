/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author frank
 */
public class KruskalStrategy extends MazeGenerationStrategy
{
    public KruskalStrategy(){}

    @Override
    public void algorithm(Grid model)
    {
        HashMap<Tile, Integer> cells = new HashMap<>();
        ArrayList<Tile> visited = new ArrayList<>();
        ArrayList<Tile> unvisited = new ArrayList<>();
        
        // Set each tile as a cell with unique id
        List<Tile> tiles = model.getTiles();
        for(int i = 0; i < tiles.size(); i += 2)
        {
            cells.put(tiles.get(i), i);
            unvisited.add(tiles.get(i));
        }
        
        // Stop when all cells have been visited
        while(cells.size() == visited.size())
        {
            
        }
        
    }
}