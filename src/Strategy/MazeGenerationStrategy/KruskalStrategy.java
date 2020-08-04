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
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        ArrayList<Tile> neighbors = new ArrayList<>();
        
        // Set each tile as a cell with unique id
        Tile[][] grid = model.getGrid();
        int id = 0;
        for(int y = 0; y < model.getYSize(); y += 2)
        {
            for(int x = 0; x < model.getXSize(); x += 2)
            {
                grid[x][y].toggleCoords(true, String.format("%d", id));
                cells.put(grid[x][y], id++);
                unvisited.add(grid[x][y]);
            }
        }
        
        // Stop when all cells have been visited
        while(!unvisited.isEmpty())
        {
            this.getNewRandom();
            
            // Get random tile
            Tile currentTile = unvisited.get(this.random.nextInt(unvisited.size()));
            visited.add(currentTile);
            unvisited.remove(currentTile);
            this.painter.drawTile(currentTile, null, null, Tile.Type.EMPTY, painterWait);
            
            // get it's neighbors
            this.addNeighbors(model, currentTile, neighbors);
            
            // Remove from neighbors all cells that belong in the same set as currentCell 
            Iterator iter = neighbors.iterator();
            while(iter.hasNext())
            {
                Tile tile = (Tile) iter.next();
                if(Objects.equals(cells.get(tile), cells.get(currentTile)))
                    iter.remove();
            }
            if(neighbors.isEmpty()) continue;
            
            //Pick neighbor and change neighbor id to currentTile id
            Tile neighbor = neighbors.get(this.random.nextInt(neighbors.size()));
            
            // If they're different ids, switch them
            this.removeWallBetween(model.getGrid(), currentTile, neighbor);
            this.convertCells(cells, cells.get(currentTile), cells.get(neighbor));
            this.painter.drawTile(neighbor, null, null, Tile.Type.EMPTY, painterWait);
        }
        
    }
    
    /**
     * Converts every key with a value of 'toBeConverted' to the value 'convertToThis'
     * @param cells hashmap mapping the Tiles to their values
     * @param convertToThis id for all cells to be converted to
     * @param toBeConverted id for all cells to be converted to 'convertToThis'
     */
    private void convertCells(HashMap<Tile, Integer> cells, int convertToThis, int toBeConverted)
    {
        Set<Tile> keys = cells.keySet();
        while(cells.containsValue(toBeConverted))
        {
            keys.forEach(key -> 
            {
                if(cells.get(key) == toBeConverted)
                    cells.put(key, convertToThis);
            });
        }
    }
}