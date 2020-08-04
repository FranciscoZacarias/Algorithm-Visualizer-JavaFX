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
import javafx.scene.text.Text;

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
        PATH,
        HIGHLIGHT,
        VISITED,
        VISITED_LIGHT,
        VISITED_MEDIUM,
        VISITED_DENSE,
        VISITED_MAX
    }
    
    private final Map<Type, Color> typeMap;
    private final Map<Integer, Color> weightMap;
    private final Map<Integer, Color> visitedMap;
    private final int[] WEIGHTS = { this.getDefaultWeight(), 3, 6, 9, 12};
    
    // JavaFX Node
    private final StackPane pane;
    
    // Coordinates
    private final int x;
    private final int y;
    
    // Content
    private final Rectangle rectangle;
    
    // Attributes
    private final int defaultWeight = 1;
    private int weight;
    private Type type;
    private final double tileGap = 0;
    private final int size;
    
    public Tile(int x, int y, int size)
    {
        pane = new StackPane();
        
        // Type color Mapping
        typeMap = new HashMap<>();
        typeMap.put(Type.ROOT, Color.YELLOW);
        typeMap.put(Type.TARGET, Color.PURPLE);
        typeMap.put(Type.EMPTY, Color.WHITE);
        typeMap.put(Type.WALL, Color.BLACK);
        typeMap.put(Type.PATH, Color.DEEPPINK);
        typeMap.put(Type.HIGHLIGHT, Color.RED);
        typeMap.put(Type.VISITED, Color.LIGHTGREEN);
        
        // Visited Color based on weight
        visitedMap = new HashMap<>();
        visitedMap.put(this.WEIGHTS[0], Color.PALEGREEN);
        visitedMap.put(this.WEIGHTS[1], Color.LIGHTGREEN);
        visitedMap.put(this.WEIGHTS[2], Color.SPRINGGREEN);
        visitedMap.put(this.WEIGHTS[3], Color.GREENYELLOW);
        visitedMap.put(this.WEIGHTS[4], Color.GREEN);
        
        // Empty Weight color 
        weightMap = new HashMap<>();
        weightMap.put(this.WEIGHTS[0], Color.WHITE);
        weightMap.put(this.WEIGHTS[1], Color.LIGHTCYAN);
        weightMap.put(this.WEIGHTS[2], Color.AQUA);
        weightMap.put(this.WEIGHTS[3], Color.DEEPSKYBLUE);
        weightMap.put(this.WEIGHTS[4], Color.CORNFLOWERBLUE);
        
        // Coordinates
        this.x = x;
        this.y = y;
        
        // Attributes
        this.weight = defaultWeight;
        this.type = Type.EMPTY;
        this.size = size;
        
        // Tile content
        this.rectangle = new Rectangle(size - tileGap, size - tileGap);
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
     * Toggles coordinates to appear in this tiles' pane
     * @param toAdd true if it's to add coords, false if it's to remove
     * @param text text to add, null if want to add coords
     */
    public void toggleCoords(boolean toAdd, String text)
    {
        if(toAdd)
        {
            Text coords = new Text((text == null) ? String.format("%d,%d", this.x, this.y) : text);
            // Scale: For a tile of size 20, font size is 6
            coords.setStyle(String.format("-fx-font: %d arial;", (size * 6) / 20));
            pane.getChildren().add(coords);
        }
        else
            pane.getChildren().remove(1);
    }
    
    /**
     * Sets both Type and Weight attributes for this tile.
     * @param type of this tile
     * @param weight of this tile
     */
    public void setAttributes(Type type, int weight)
    {   
        Color color;
        
        // Pick color based on type
        switch(type)
        {
            case VISITED:
                color = this.visitedMap.get(this.getWeight());
                break;
            case EMPTY:
                color = this.weightMap.get(weight);
                break;
            default:
                color = this.typeMap.get(type);
                break;
        }
        
        this.rectangle.setFill(color);
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

    public Type getType()
    {
        return this.type;
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