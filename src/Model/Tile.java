/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.Set;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author frank
 */
public class Tile extends Observable
{
    /**
     * Possible Tile Types for each Tile 
     */
    public static enum Type
    {
        ROOT,
        TARGET,
        WALL,
        EMPTY,
        VISITED,
        PATH
    }
    
    Map<Type, Color> typeMap;
    Map<Integer, Color> weightMap;
    
    // JavaFX Node
    StackPane pane;
    
    // Coordinates
    private final int x;
    private final int y;
    
    // Content
    private final Rectangle rectangle;
    
    // Attributes
    private final int defaultWeight = 1;
    private int weight;
    private Type type;
    
    public Tile(int x, int y, int size)
    {
        pane = new StackPane();
        
        // Type color Mapping
        typeMap = new HashMap<>();
        typeMap.put(Type.ROOT, Color.YELLOW);
        typeMap.put(Type.TARGET, Color.PURPLE);
        typeMap.put(Type.VISITED, Color.RED);
        typeMap.put(Type.EMPTY, Color.WHITE);
        typeMap.put(Type.WALL, Color.BLACK);
        typeMap.put(Type.PATH, Color.DARKGOLDENROD);
        
        // Weight color 
        weightMap = new HashMap<>();
        weightMap.put(this.defaultWeight, Color.WHITE);
        weightMap.put(3, Color.LIGHTCYAN);
        weightMap.put(6, Color.AQUA);
        weightMap.put(9, Color.DEEPSKYBLUE);
        weightMap.put(12, Color.CORNFLOWERBLUE);
        
        // Coordinates
        this.x = x;
        this.y = y;
        
        // Attributes
        this.weight = defaultWeight;
        this.type = Type.EMPTY;
        
        // Tile content
        this.rectangle = new Rectangle(size - 2, size - 2);
        this.rectangle.setFill(Color.WHITE);
        rectangle.setStroke(Color.LIGHTGRAY);
        
        // build this StackPane
        pane.getChildren().add(rectangle);
        pane.setTranslateX(x * size);
        pane.setTranslateY(y * size);
        
        updateTooltip(null);
        setEvents();
    }
    
    /**
     * Returns this Tile's pane
     * @return StackPane
     */
    public StackPane getStackPane()
    {
        return this.pane;
    }
    
    /**
     * Sets both Type and Weight attributes for this tile.
     * @param type of this tile
     * @param weight of this tile
     */
    public void setAttributes(Type type, int weight)
    {   
        // Set default color for given weight
        this.rectangle.setFill(
                (type == Type.EMPTY) ? 
                this.weightMap.get(weight) :
                this.typeMap.get(type)
        );
        
        this.type = type;
        this.weight = weight;
        updateTooltip(null);
    }
    
    /**
     * Returns this tiles weight
     * @return 
     */
    public int getWeight()
    {
        return this.weight;
    }
    
    /**
     * Returns x coordinate of this tile
     * @return 
     */
    public int getX()
    {
        return this.x;
    }
    
    /**
     * Returns y coordinate of this tile
     * @return 
     */
    public int getY()
    {
        return this.y;
    }
    
    /**
     * Returns given Tile to its default state (EMPTY, DEFAULTWEIGHT)
     * @return 
     */
    public void clearTile()
    {
        this.setAttributes(Type.EMPTY, defaultWeight);
    }
    
    /**
     * Returns default weight for an empty tile
     * @return 
     */
    public int getDefaultWeight()
    {
        return this.defaultWeight;
    }
    
    /**
     * Randomizes this tile's weight
     */
    public void randomizeWeight()
    {
        Set<Integer> keys = weightMap.keySet();
        Random random = new Random();
        
        this.setAttributes(Type.EMPTY,
                (int)keys.toArray()[random.nextInt(keys.size())]
                );
    }
    
    public boolean isWall()
    {
        return (this.type == Type.WALL);
    }
    
    /**
     * Updates this tile's Tooltip
     * @param text 
     */
    private void updateTooltip(String text)
    {
        text = (text == null) ? this.toString() : text;
        Tooltip.install(pane, new Tooltip(text));
    }
    
    /**
     * Defines events for this Tile
     */
    private void setEvents()
    {
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent mouseEvent) ->
        {
            // Notifies the @Grid
            setChanged();
            notifyObservers();
        });
    }

    @Override
    public String toString()
    {
        return String.format("%s - (%d,%d) W:%d", this.type, this.x, this.y, this.weight);
    }
}