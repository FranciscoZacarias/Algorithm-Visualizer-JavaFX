/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Strategy.MazeGenerationStrategy;

import Model.Grid;
import Model.Tile;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author frank
 */
public class SidewinderStrategy extends MazeGenerationStrategy
{

    public SidewinderStrategy()
    {
        super();
    }

    @Override
    public void algorithm(Grid model)
    {
        /**
         * Due to the nature of this implementation, this algorithm is implemented from 
         * left to right, instead of top to bottom. The fundamentals are the same and despite
         * it's orientation, the implementation still is the one of a sidewinder algorithm
         */
        
        Tile[][] grid = model.getGrid();
        Tile currentTile;
        boolean firstCol = true;
        Set<Tile> run = new HashSet<>();
        int colY = 1; // because column 0 will always be skipped, so first check will be on column 1
        
        for(Tile[] col : model.getGrid())
        {
            // First column has to be fully clear
            if(firstCol)
            {
                for(Tile tile : col)
                    this.painter.highlightTile(tile, painterWait);
                firstCol = false;
                continue;
            }
            
            // Skip if it's not an even column (Due to this implementation design, not because the algorithm demands)
            if(colY++ % 2 != 0) continue;
            currentTile = col[0];
            run.add(currentTile);
            this.painter.drawTile(currentTile, null, null, Tile.Type.EMPTY, painterWait);
            
            while(true)
            {
                boolean carveSouth = new Random().nextBoolean();
                
                Tile southTile = model.getSouthTile(currentTile);
                if(southTile == null) 
                {
                    this.carveToWest(model, currentTile, currentTile);
                    break;
                }
                Tile southsouth = model.getSouthTile(southTile);
                 
                // Either carve south and add to set, or carve west by picking a random
                // tile from the set.
                if(carveSouth)
                {
                    run.add(currentTile);
                    run.add(southTile);
                    this.removeWallBetween(grid, currentTile, southsouth);
                }
                else
                {
                    // Pick random tile from current set
                    int i = 0, randomTile = new Random().nextInt(run.size());
                    for(Tile tile : run)
                    {
                        if (i++ == randomTile)
                        {
                            this.carveToWest(model, tile, currentTile);
                            break;
                        }
                    }
                    run.clear();
                }
                
                currentTile = southsouth;
                run.add(southsouth);
                this.painter.drawTile(currentTile, null, null, Tile.Type.EMPTY, painterWait);
            }
        }
    }
    
    /**
     * Carves a path from current tile to west tile (skips one tile because it's a wall)
     * @param model 
     * @param westFrom
     * @param currentTile 
     */
    private void carveToWest(Grid model, Tile westFrom, Tile currentTile)
    {
        Tile westTile = model.getWestTile(westFrom);
        Tile westwest = model.getWestTile(westTile);
        this.removeWallBetween(model.getGrid(), currentTile, westwest);
    }
}
